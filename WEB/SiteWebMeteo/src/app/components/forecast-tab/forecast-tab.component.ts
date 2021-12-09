import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {WeatherService} from "../../services/weather.service";
import {tap} from "rxjs/operators";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-forecast-tab',
  templateUrl: './forecast-tab.component.html',
  styleUrls: ['./forecast-tab.component.css']
})
export class ForecastTabComponent implements OnInit, OnChanges {

  @Input()
  stationCode: string;
  stationForecast: any = undefined;

  constructor(private weatherService: WeatherService) { }

  ngOnInit(): void {
    if (this.stationCode) {
      this.weatherService.getStationForecast(this.stationCode)
        .subscribe(
        (data: any) => this.stationForecast = JSON.parse(data),
        (error: HttpErrorResponse) => this.stationForecast = JSON.parse(error.error)
      );
    }
  }

  ngOnChanges(): void {
    this.ngOnInit();
  }
}
