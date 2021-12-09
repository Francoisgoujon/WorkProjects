import {Component, OnChanges, OnInit} from '@angular/core';
import {CommonConstants, StationDetails} from '../../../common';
import {WeatherService} from '../../services/weather.service';
import {catchError, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import { NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-station-details',
  templateUrl: './station-details.component.html',
  styleUrls: ['./station-details.component.css']
})
export class StationDetailsComponent implements OnInit, OnChanges {

  stationCode = '';
  station: StationDetails[] = [];
  // this attribute will be used by tabs to display the filtered data
  filteredStation: StationDetails[] = [];

  // value ranges for ng-select inputs
  years: string[];
  months: string[] = Object.keys(CommonConstants.MONTHS);

  // selected values from ng-select inputs
  fromYear = '';
  fromMonth = '';
  toYear = '';
  toMonth = '';

  // selected tab
  active = 'ngb-nav-0';

  // data for datePicker
  datePicker: NgbDateStruct;
  date: {year: number, month: number};

  constructor(private weatherService: WeatherService) {
  }

  ngOnInit(): void {
    this.weatherService.getStationDetailsEmitter() // we get the stationId via the emitter
      .pipe(
        switchMap((stationId: number) => this.weatherService.getStationDetails(stationId)) // we get station details
      ).subscribe(
      (resultJson: any) => {
        this.station = resultJson.details;
        this.stationCode = resultJson.code;
        this.initFields();

        // we generate the array of years to be displayed in ng-select based on csv data
        this.years = Array(Number(this.toYear) - Number(this.fromYear) + 1)
          .fill('')
          .map((_, i) => String(Number(this.fromYear) + i));
      });
  }

  ngOnChanges(): void {
    this.ngOnInit();
  }

  /**
   * Everytime the user updates a ng-select, the filtered station has to be regenerated to update children views
   */
  onChange(): void {
    const start = new Date(this.fromYear + '-' + CommonConstants.MONTHS[this.fromMonth]);
    const end = new Date(this.toYear + '-' + CommonConstants.MONTHS[this.toMonth]);

    this.filteredStation = this.station.filter(line => { // we filter the station based on start and end date
      if (line['Date/Time'] === '') {
        return true; // line does not contain any date, accept
      } else {
        const lineDate = new Date(line['Date/Time']);
        return lineDate >= start && lineDate <= end;
      }
    });
  }

  reset(): void {
    this.filteredStation = this.station;
    this.initFields();
  }

  /**
   * This method initializes the station details to be passed to children views
   * and the default values to be displayed in ng-select
   * @private
   */
  private initFields(): void {
    if (this.station && this.station.length > 0) {
      this.filteredStation = this.station;
      this.fromYear = this.station[0].Year; // first line of csv
      this.fromMonth = Object.keys(CommonConstants.MONTHS)[0]; // first line of csv
      this.toYear = this.station[this.station.length - 1].Year; // last line of csv
      this.toMonth = Object.keys(CommonConstants.MONTHS)[11]; // last line of csv
    } else {
      this.filteredStation = [];
      this.fromYear = '';
      this.fromMonth = '';
      this.toYear = '';
      this.toMonth = '';
    }
  }
}
