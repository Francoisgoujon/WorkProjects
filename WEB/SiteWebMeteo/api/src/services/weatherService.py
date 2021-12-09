import requests
import csv
import json
import pandas as pd
import xmltodict

def get_station_data(airportId, year, month, day):
    response_text = ""
    station_not_found = True

    stations = get_stations_for_airport(airportId)

    if stations is None:
        return None
    index = 0

    while station_not_found and index < len(stations):
        station_id = stations[index]
        index += 1

        url = "https://climate.weather.gc.ca/climate_data/bulk_data_e.html?format=csv&stationID={station}&Year={year}&Month={month}&Day={day}&timeframe=1&submit=%20Download+Data".format(
            station=station_id, year=year, month=month, day=day)

        payload = {}
        headers = {
            'Content-Type': 'text/csv'
        }

        response = requests.request("GET", url, headers=headers, data=payload)
        station_not_found = (response.status_code == 404 or response.status_code == 302)

        if response.status_code == 200 and response.headers["Content-Type"] == "application/force-download" and len(response.text) > 0:
            result = parse_csv_to_dict(response.text, airportId)
            if all(k not in dict for dict in result for k in ("Weather", "Temp (°C)")):
              station_not_found = True
            else:
              return result

    if station_not_found and index >= len(stations):
        return None

def get_forecast(airport_id):

    url = get_rssfeed_for_airport(airport_id)

    if url is None:
      return None

    payload = {}
    headers = {}

    response = requests.request("GET", url, headers=headers, data=payload)

    print(response.text)

    if response.status_code == 404:
        return None

    result = dict()
    data = xmltodict.parse(response.text)

    result["TC ID"] = airport_id
    result["lastUpdate"] = data["feed"]["updated"]
    result["stationName"] = data["feed"]["title"]
    result["link"] = next((l for l in data["feed"]["link"]
                          if l["@type"] == "text/html"), None)["@href"]
    rawWarnings = next(
        (e for e in data["feed"]["entry"] if e["category"]["@term"] == "Veilles et avertissements"), None)
    rawCurrentWeather = next(
        (e for e in data["feed"]["entry"] if e["category"]["@term"] == "Conditions actuelles"), None)

    result["currentWeather"] = dict()
    result["currentWeather"]["title"] = rawCurrentWeather["title"]
    result["currentWeather"]["summary"] = rawCurrentWeather["summary"]["#text"]
    result["currentWeather"]["updated"] = rawCurrentWeather["updated"]

    result["warnings"] = dict()
    result["warnings"]["title"] = rawWarnings["title"]
    result["warnings"]["summary"] = rawWarnings["summary"]["#text"]
    result["warnings"]["updated"] = rawWarnings["updated"]
    result["warnings"]["link"] = rawWarnings["link"]

    rawWeatherForecast = list(
        (e for e in data["feed"]["entry"] if e["category"]["@term"] == "Prévisions météo"))

    weatherForecast = []

    for f in rawWeatherForecast:
        entry = dict()
        entry["title"] = f["title"]
        entry["summary"] = f["summary"]["#text"]
        weatherForecast.append(entry)

    result["forecast"] = weatherForecast

    return result

def get_stations_for_airport(airport_id):
    airport_details = get_airport_details(airport_id)
    if airport_details is None:
        return None
    else:
        return airport_details["station_ids"]

def get_rssfeed_for_airport(airport_id):
    airport_details = get_airport_details(airport_id)
    if airport_details is None:
        return None
    else:
      return airport_details["rss_feed"]

def get_airport_details(airport_id):
    with open('station_mapping.json', 'r') as mapping_file:
        data = mapping_file.read()

    parsed_data = json.loads(data)
    if airport_id not in parsed_data:
        return None

    return parsed_data[airport_id]

def parse_csv_to_dict(data, airport_id):
    result = []
    data = data.split('\r\n')

    reader = csv.DictReader(data, quotechar='\"', delimiter=",")

    for line in reader:
        filtered_line = {key: value for key, value in line.items() if value is not None and len(value) != 0}
        filtered_line["TC ID"] = airport_id
        result.append(filtered_line)

    return result
