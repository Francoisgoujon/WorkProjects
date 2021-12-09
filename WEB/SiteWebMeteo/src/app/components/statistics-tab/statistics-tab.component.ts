import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { WeatherService } from 'src/app/services/weather.service';
import { CommonConstants, StationDetails } from 'src/common';

@Component({
  selector: 'app-statistics-tab',
  templateUrl: './statistics-tab.component.html',
  styleUrls: ['./statistics-tab.component.css'],
  providers: [WeatherService],
})


export class StatisticsTabComponent implements OnInit, OnChanges {

  @Input()
  station: StationDetails[] = [];

  TempMoyMens: any;
  TempExtMax: any;
  TempExtMin: any;
  QtPluie: any;
  QtNeige: any;
  VitVent: any;

  TempMoyMois: any;
  TempMaxMois: any;
  TempMinMois: any;
  QtpluieMois: any;
  QtNeigeMois: any;
  VitventMois: any;

  readonly MONTHS = CommonConstants.MONTHS;

  constructor(private weatherService: WeatherService) {}

  getMaxMinDonneePeriode(station: StationDetails[], donnee: string): any {
    let max: number;
    let moisMax: number;
    let anneeMax: number;
    let min: number;
    let moisMin: number;
    let anneeMin: number;
    if (this.station && this.station.length > 0) {
      max = -1000000;
      min = 1000000;
    }
    for (let i = 0; i < this.station.length; i++) {
      let valueString: string = this.station[i][donnee];
      if (valueString != "") {
        let value: number = Number(valueString);
        if (value > max) {
          max = value;
          moisMax = Number(this.station[i]['Month']);
          anneeMax = Number(this.station[i]['Year']);
        }
        if (value <= min) {
          min = value;
          moisMin = Number(this.station[i]['Month']);
          anneeMin = Number(this.station[i]['Year']);
        }
      }
    }

    let result: any = {};
    result['ValeurMax'] = max;
    result['MoisMax'] = moisMax;
    result['AnneeMax'] = anneeMax;
    result['ValeurMin'] = min;
    result['MoisMin'] = moisMin;
    result['AnneeMin'] = anneeMin;

    return result;
  }

  getMaxMinDonneePeriodeMois(station: StationDetails[], donnee: string): any {
    const mois: string[] = Object.values(CommonConstants.MONTHS);
    let max: any = {};
    let min: any = {};
    if (this.station && this.station.length > 0) {
      for (let m = 0 ; m< mois.length ; m++) {
        let mo:string = mois[m];
        max[mo] = -100000000;
        min[mo] = 100000000;
      }
    }
    let anneeMax: any = {};
    let anneeMin: any = {};
    for (let i = 0 ; i < this.station.length ; i++) {
      let m:string = this.station[i]['Month'];
      let valueString: string = this.station[i][donnee];
      if (valueString != "") {
        let value: number = Number(valueString);
        if (value > max[m]) {
          max[m] = value;
          anneeMax[m] = Number(this.station[i]['Year']);
        }
        if (value <= min[m]) {
          min[m] = value;
          anneeMin[m] = Number(this.station[i]['Year']);
        }
      }
    }

    let result: any = {};
    result['ValeurMax'] = max;
    result['AnneeMax'] = anneeMax;
    result['ValeurMin'] = min;
    result['AnneeMin'] = anneeMin;

    return result;
  }

  private initFields(): void {
    this.TempMoyMens = this.getMaxMinDonneePeriode(this.station,'Mean Temp (°C)');
    this.TempExtMax = this.getMaxMinDonneePeriode(this.station,'Extr Max Temp (°C)');
    this.TempExtMin = this.getMaxMinDonneePeriode(this.station,'Extr Min Temp (°C)' );
    this.QtPluie = this.getMaxMinDonneePeriode(this.station, 'Total Rain (mm)' );
    this.QtNeige = this.getMaxMinDonneePeriode(this.station,'Total Snow (cm)' );
    this.VitVent = this.getMaxMinDonneePeriode(this.station,'Spd of Max Gust (km/h)');

    this.TempMoyMois = this.getMaxMinDonneePeriodeMois(this.station,'Mean Temp (°C)');
    this.TempMaxMois = this.getMaxMinDonneePeriodeMois(this.station,'Extr Max Temp (°C)');
    this.TempMinMois = this.getMaxMinDonneePeriodeMois(this.station,'Extr Min Temp (°C)' );
    this.QtpluieMois = this.getMaxMinDonneePeriodeMois(this.station, 'Total Rain (mm)' );
    this.QtNeigeMois = this.getMaxMinDonneePeriodeMois(this.station,'Total Snow (cm)' );
    this.VitventMois = this.getMaxMinDonneePeriodeMois(this.station,'Spd of Max Gust (km/h)');

  }

  ngOnInit(): void {
    this.initFields();
  }

  ngOnChanges(): void {
    this.initFields();
  }
}
