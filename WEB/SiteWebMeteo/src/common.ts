export interface Station {
  Name: string;
  Province: string;
  'Climate ID': number;
  'Station ID': number;
  'WMO ID': number;
  'TC ID': string;
  'Latitude (Decimal Degrees)': number;
  'Longitude (Decimal Degrees)': number;
  Latitude: number;
  Longitude: number;
  'Elevation (m)': number;
  'First Year': number;
  'Last Year': number;
  'HLY First Year': number;
  'HLY Last Year': number;
  'DLY First Year': number;
    'DLY Last Year': number;
    'MLY First Year': number;
    'MLY Last Year': number;
}

export interface StationsPerProvince{
    Province: string;
    Stations: Station[];
}

export interface StationDetails {
  'Longitude (x)': number;
  'Latitude (y)': number;
  'Station Name': string;
  'Climate ID': number;
  'TC ID'?: string;
  'Date/Time': string;
  'Year': string;
  'Month': string;
  'Mean Max Temp (°C)': number;
  'Mean Max Temp Flag': string;
  'Mean Min Temp (°C)': number;
  'Mean Min Temp Flag': string;
  'Mean Temp (°C)': number;
  'Mean Temp Flag': string;
  'Extr Max Temp (°C)': number;
  'Extr Max Temp Flag': string;
  'Extr Min Temp (°C)': number;
  'Extr Min Temp Flag': string;
  'Total Rain (mm)': number;
  'Total Rain Flag': string;
  'Total Snow (cm)': number;
  'Total Snow Flag': string;
  'Total Precip (mm)': number;
  'Total Precip Flag': string;
  'Snow Grnd Last Day (cm)': number;
  'Snow Grnd Last Day Flag': string;
  'Dir of Max Gust (10\'s deg)': string;
  'Dir of Max Gust Flag': string;
  'Spd of Max Gust (km/h)': number;
  'Spd of Max Gust Flag': string;
}

export class CommonConstants {

  public static readonly MONTHS = {
    Janvier: '01',
    Février: '02',
    Mars: '03',
    Avril: '04',
    Mai: '05',
    Juin: '06',
    Juillet: '07',
    Août: '08',
    Septembre: '09',
    Octobre: '10',
    Novembre: '11',
    Décembre: '12'
  };

  public static readonly CITY = [
    "YYC",
    "YEG",
    "YQX",
    "YQM",
    "YHZ",
    "YHM",
    "YXU",
    "YUL",
    "YOW",
    "YQB",
    "YQR",
    "YXE",
    "YYT",
    "YYZ",
    "YVR",
    "YYJ",
    "YWG",
  ]

}
