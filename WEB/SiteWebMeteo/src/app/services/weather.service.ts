import * as Papa from 'papaparse';
import {parse} from 'papaparse';
import {EventEmitter, Injectable} from '@angular/core';
import {map, mergeAll} from 'rxjs/internal/operators';
import {HttpClient} from '@angular/common/http';
import {Station, StationDetails} from '../../common';
import {forkJoin, Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root',
})
export class WeatherService {
  private papaParseConfig = {
    delimiter: ',',
    skipEmptyLines: true,
    header: true,
    dynamicTyping: true,
  };

  private selectedStation = new EventEmitter<number>();

  constructor(private http: HttpClient) {
    console.log('Service loaded');
  }

  public getStations(): any {
    return this.http
      .get('assets/csv/Station Inventory EN.csv', {
        responseType: 'text',
      })
      .pipe(
        map((data: any) => {
          let stationsDict: any = {};
          let provinceNames: string[] = [];
          let lines = data.split('\n');

          lines.splice(0, 3);
          data = lines.join('\n');
          let parsedData: any = parse(data, this.papaParseConfig).data;
          const stations: Station[] = [];

          for (let i in parsedData) {
            let s: Station = parsedData[i];

            if (s["TC ID"] != null) {
              if (stationsDict[s['Province']] == undefined) {
                provinceNames.push(s['Province']);
                stationsDict[s['Province']] = [];
                console.log('init');
              }

              stationsDict[s['Province']].push(s);
              stations.push(s);
            }
          }
          const result: any = {};
          result.provinceNames = provinceNames.filter(
            (val) => val !== undefined
          );
          result.stationsDict = stationsDict;
          result.allStations = stations;

          return result;
        })
      );
  }

  public getStationDetails(stationId: number): Observable<any> {
    return forkJoin({
      code: this.getStationCode(stationId),
      details: this.http
        .get('assets/csv/' + stationId + '.csv', {
          responseType: 'text',
        }).pipe(
          map((stationDetails: string) => Papa.parse(stationDetails, {
            delimiter: ',',
            skipEmptyLines: true,
            header: true
          }).data as any),
          catchError((err: string) => of(console.error(err)))
        )
    });
  }

  public getStationCode(stationId: number): Observable<string> {
    return this.http
      .get('assets/csv/Station Inventory EN.csv', {
        responseType: 'text',
      })
      .pipe(
        map((csvData: any) => {
          csvData = csvData.split('\n');
          csvData.splice(0, 3);
          return parse(csvData.join('\n'), this.papaParseConfig).data as any;
        }),
        map((stations: Station[]) =>
          stations.filter(
            (station: Station) => station['Station ID'] === stationId
          )
        ),
        mergeAll(),
        map((station: Station) => station['TC ID'])
      );
  }

  public getStationForecast(stationCode: string): Observable<any> {
    return this.http
      .get(`http://localhost:5000/api/weather/forecast?airport=${stationCode}`, {
        responseType: 'text',
      });
  }

  public getStationDetailsEmitter(): EventEmitter<number> {
    return this.selectedStation;
  }

  public getStationHistoric(stationId: any, date: any): Observable<any> {
    return this.http.get( `http://localhost:5000/api/weather/station/day?airport=${stationId}&year=${date.year}&month=${date.month}&day=${date.day}`, { observe: 'response'});
  }
}

