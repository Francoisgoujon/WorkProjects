import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {WeatherService} from "../../services/weather.service";
import {catchError, switchMap} from 'rxjs/operators';
import { AbstractWebDriver } from 'protractor/built/browser';
import { StationListComponent } from '../station-list/station-list.component';
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-history-tab',
  templateUrl: './history-tab.component.html',
  styleUrls: ['./history-tab.component.css']
})
export class HistoryTabComponent implements OnInit, OnChanges {

  @Input()
  date: NgbDateStruct;

  @Input()
  stationId: string;
  allStation: any;
  status: boolean;

  constructor(private weatherService: WeatherService) { }

  ngOnInit(): void {
      if(this.date){
        this.weatherService.getStationHistoric(this.stationId, this.date).subscribe((station: any) => {
          this.allStation = station.body;
          this.status = false;
        }, (error: HttpErrorResponse) => {
          console.error(error);
          this.status = true;
          this.allStation = error.error;
        })
      }
  }

  ngOnChanges(): void {
      this.ngOnInit();
  }

}
