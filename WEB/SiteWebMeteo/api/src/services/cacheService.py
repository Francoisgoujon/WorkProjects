import copy

from pymongo import MongoClient

client = MongoClient("mongodb://localhost/weather?retryWrites=true&w=majority")
stations = client.weather.stations
forecasts = client.weather.forecasts

# Pour faire un clean de la db stations
#stations.remove()

stations.create_index("TC ID", expireAfterSeconds=86400)
forecasts.create_index("TC ID", expireAfterSeconds=3600)

def get_station_data(airport_id, year, month, day):
  print("get station data from cache")
  if int(month) < 10 :
    month = "0" + month
  if int(day) < 10 :
    day = "0"+ day
  cursor = stations.find({"TC ID": airport_id, "Year": year, "Month": month, "Day": day})
  res = []
  for elem in cursor :
    del elem['_id']
    res.append(elem)
  #print(res)
  return res

def get_forecast(airport_id):
  print("get forecast cache")
  res = forecasts.find_one({"TC ID": airport_id}, {'_id': False})
  return res

def set_station_data(station_data):
  print("update station data cache")
  # On fait une deep_copy car l'insertion dans stations ajoute un champ dans le dictionnaire
  station_data_copy = copy.deepcopy(station_data)
  stations.insert_many(station_data_copy)

def set_forecast(result):
  print("update forecast cache")
  forecasts.insert_one(result)
