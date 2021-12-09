import { Component, Input, OnInit } from '@angular/core';
import { icon, latLng, Map, marker, tileLayer } from 'leaflet';
import { forkJoin } from 'rxjs';
import { CommonConstants } from '../../../common';
import { WeatherService } from "../../services/weather.service";
@Component({
  selector: 'app-map-tab',
  templateUrl: './map-tab.component.html',
  styleUrls: ['./map-tab.component.css']
})
export class MapTabComponent implements OnInit {

  @Input()
  stationCode: string;
  stationForecast: any = undefined;

  map: Map = null;
  selection: string = 'Aucune informations';


  //Assuming there's data for this date. API require data field.
  date = {
      day: 1,
      month: 12,
      year: 2020,
  }

  weatherSummary = [];
  forecast = [];
  forecastTooltip = [];
  cityAvailableCode = [];
  cityName = [];
  forecastTooltip2 = [];
  readonly cityList = CommonConstants.CITY;

  constructor(private weatherService: WeatherService) {
  }

  //https://asymmetrik.com/ngx-leaflet-tutorial-angular-cli/

  options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
      })
    ],
    zoom: 4,
    center: latLng([ 45.6269695, -99.6312198 ])
  };

  layers = [];

  //https://github.com/Asymmetrik/ngx-leaflet/issues/104
  onMapReady(map: L.Map) {
      setTimeout(() => {
        map.invalidateSize();
      }, 300);
  }

  ngOnInit(): void {
    this.getStationLocation();
  }

  getStationLocation(): void {
    for(let i = 0; i < this.cityList.length; i++) {
        forkJoin({
        requestForecast: this.weatherService.getStationForecast(this.cityList[i])
        ,
        requestLocation: this.weatherService.getStationHistoric(this.cityList[i], this.date)
        }).subscribe(({requestForecast, requestLocation}) => {

            //Change to JSON forecast data.
            let forecastData = JSON.parse(requestForecast);

            //Array of summary, loop through for each cities popup(Actual information)
            this.weatherSummary.push(forecastData.currentWeather.summary);
            /*Array of forecastData, loop through for each cities popup(Futur information)
            Add the title of the week in a Array.
            */
            let temp: string = "";
            for(let i = 0; i < forecastData.forecast.length; i++) {
                temp = temp + forecastData.forecast[i].title + "<br/>"
            }
            //Array of everyday forecast data (Tooltip)
            this.forecastTooltip.push(forecastData.forecast[0].title);
            this.forecast.push(temp);
            temp = "";
            /*End of algo*/

            //City name
            this.forecastTooltip2.push(forecastData.currentWeather.title);
            let currentLocationName = requestLocation.body[0]["Station Name"].split(' ')[0]

            //Get the location from observable.
            let currentlat = requestLocation.body[0]["Latitude (y)"];
            let currentlong = requestLocation.body[0]["Longitude (x)"];

            //No selection information selected (tooltip)
            this.cityName.push("<b>City: </b>" + requestLocation.body[0]["Station Name"].split(' ')[0]);


            /*Can be removed if all city work. Push all the existant city(status 200).(For the tooltip)*/
            this.cityAvailableCode.push("<b>Aéroport: </b>"  + this.cityList[i])
            /*Remove*/

            this.addMarkerMap(currentlat, currentlong, "<b>City: </b>" + currentLocationName, "Aucune séléction d'information");
        });
    }
  }

  changeDropdownName(value: number) {
      if(value == 0) {
          this.changeMarkerPopup(this.weatherSummary);
          this.changeMarkerTooltip(this.forecastTooltip2);
          this.selection = "Informations actuelles";
        } else if(value == 1) {
          this.changeMarkerPopup(this.forecast);
          this.changeMarkerTooltip(this.forecastTooltip);
          this.selection = "Prévisions";
        } else if(value == 2) {
          this.changeMarkerPopup("Aucune séléction d'information");
          this.changeMarkerTooltip(this.cityName);
          this.selection = "Aucune informations";
      }
  }

  addMarkerMap(lat, long, tooltipText, popupText): void {
        this.layers.push(marker([ Number(lat), Number(long)], {
        icon: icon({
                iconUrl: 'assets/marker-icon.png',
                shadowUrl: 'assets/marker-shadow.png'
        })
      }).bindPopup(popupText).bindTooltip(tooltipText, {direction:"right", opacity: 1, permanent: true, className: "my-label", offset: [20, 35] }))
  }


  changeMarkerPopup(popupContent): void {
      if(Array.isArray(popupContent)){
         this.layers.forEach((data, index) => {data.setPopupContent(popupContent[index])})
      } else {
         this.layers.forEach((data) => {data.setPopupContent(popupContent)})
      }
  }

  changeMarkerTooltip(tooltipContent): void {
      this.layers.forEach((data, index) => {data.setTooltipContent(tooltipContent[index])})
  }

}





