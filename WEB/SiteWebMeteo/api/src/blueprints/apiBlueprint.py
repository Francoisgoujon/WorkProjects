import json
from flask import (Blueprint)
from blueprints import weatherBlueprint

bp = Blueprint('api', __name__, url_prefix="/api")

bp.register_blueprint(weatherBlueprint.bp)

@bp.route('/')
def index():
    return {'msg':'this is basic route for apiBlueprint'}
