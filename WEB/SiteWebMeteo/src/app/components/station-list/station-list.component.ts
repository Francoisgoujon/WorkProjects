import {Component, OnInit} from '@angular/core';
import {OrderPipe} from 'ngx-order-pipe';
import {Station} from 'src/common';
import {WeatherService} from '../../services/weather.service';

@Component({
  selector: 'app-station-list',
  templateUrl: './station-list.component.html',
  styleUrls: ['./station-list.component.css']
})

export class StationListComponent implements OnInit {

  allStations: Station[] = [];
  StationsDict: any = {};
  ProvinceNames: string[] = [];
  buttonList: any[] = [];
  active = '';

  constructor(private orderPipe: OrderPipe, private weatherService: WeatherService) {
  }

  ngOnInit(): void {
    this.weatherService.getStations().subscribe(
      (data: any) => {
        this.ProvinceNames = data.provinceNames;
        this.ProvinceNames.sort();
        this.StationsDict = data.stationsDict;
        this.allStations = data.allStations;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  /**
   * Cette méthode est associé au évènement (click) du HTML (station-list-component.html)
   * La fonction change le fond de couleur lorsque c'est sélectionné.
   * @param value station sélectionnée
   */
  onClick(value: Station): void {
    // Changer le value dans le (emit), Value.Station ID
    this.weatherService.getStationDetailsEmitter().emit(value['Station ID']);
  }

  changeProvColor($event: any): void {
    this.buttonList.unshift($event.target.getAttribute('aria-controls'));
    if ($event.target.style.backgroundColor == '') {
      $event.target.style.background = '#007bff';
      $event.target.style.color = 'white';
    } else {
      $event.target.style.background = '';
      $event.target.style.color = '';
    }
    if (this.buttonList.length > 1) {
      if (this.buttonList[0] != this.buttonList[1]) {
        const Num: number = Number(this.buttonList[1].slice(10.12));
        document.querySelectorAll<HTMLElement>('.btn')[Num].style.background = '';
        document.querySelectorAll<HTMLElement>('.btn')[Num].style.color = '';
      }
    }
  }

}
