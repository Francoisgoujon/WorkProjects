import {Component} from '@angular/core';
import {WeatherService} from './services/weather.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [WeatherService],
})
export class AppComponent {
  title = 'gti525-labo';

  StationsDict: any = {};
  ProvinceNames: string[] = [];
  CurrentStation: any = {};

  constructor(private weatherService: WeatherService) {}

  getStationDetails(stationId: number) {
    this.weatherService.getStationDetails(stationId).subscribe(
      (data) => {
        console.log(data);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  ngOnInit() {
    this.weatherService.getStations().subscribe(
      (data: any) => {
        this.ProvinceNames = data['provinceNames'];
        this.StationsDict = data['stationsDict'];
      },
      (error) => {
        console.log(error);
      }
    );
    console.log(this.StationsDict);
  }
}
