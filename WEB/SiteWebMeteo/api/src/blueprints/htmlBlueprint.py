from flask import (Blueprint, request,jsonify, send_from_directory)
from werkzeug.utils import send_from_directory

bp = Blueprint('html', __name__, url_prefix="/")

@bp.route('/<path:path>', methods=["GET"])
def static_files(path):
    return send_from_directory(directory="static/", filename=path)

@bp.route('/')
def index(file):
    return send_from_directory(directory="static/", filename="index.html")