import { Component, OnInit, Input } from '@angular/core';
import { Station, StationDetails } from 'src/common';
import {WeatherService} from '../../services/weather.service';

@Component({
  selector: 'app-data-tab',
  templateUrl: './data-tab.component.html',
  styleUrls: ['./data-tab.component.css']
})
export class DataTabComponent implements OnInit {
  @Input() station: StationDetails[];
  constructor(private weatherService: WeatherService) { }

  ngOnInit(): void {
  }

}