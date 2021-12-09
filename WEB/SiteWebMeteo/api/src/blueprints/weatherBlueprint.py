import json

from flask import Blueprint, jsonify, request
from flask.wrappers import Response
from services import cacheService, weatherService

bp = Blueprint('weather', __name__, url_prefix="/weather")


@bp.route('/')
def index():
    return {'msg': 'this is basic route for weatherBlueprint'}

# GET /api/weather/station/day?airport=${airport}&year=${year}month=${month}&day=${day}
# Exemple: GET /api/weather/station/day?airport=YUL&year=2020&month=01&day=07


@bp.route('/station/day', methods=["GET"])
def stationData():
    airportId = request.args.get("airport").strip()
    year = request.args.get("year").strip()
    month = request.args.get("month").strip()
    day = request.args.get("day").strip()

    result = cacheService.get_station_data(airportId, year, month, day)
    if result is None or result == []:
        result = weatherService.get_station_data(airportId, year, month, day)
        if result is not None and result != []:
          cacheService.set_station_data(result)

    if result is None:
        msg = '{\"msg\":\"Data unavailable for ' + \
            airportId + '. Please try with another date.\"}'
        return Response(msg, status=404, mimetype="application/json")

    return jsonify(result)


# GET /api/weather/forecast?airport=${airport}
@bp.route('/forecast', methods=["GET"])
def forecast():
    airportId = request.args.get("airport")

    result = cacheService.get_forecast(airportId)
    if result is None:
        result = weatherService.get_forecast(airportId)
        cacheService.set_forecast(result)
        result.pop('_id', None)

    if result is not None:
        return jsonify(result)
    else:
        msg = '{\"msg\":\"station ' + \
              airportId + ' not found. Please try with another station.\"}'
        return Response(msg, status=404, mimetype="application/json")
