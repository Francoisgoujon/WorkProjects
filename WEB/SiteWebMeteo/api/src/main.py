from dotenv import load_dotenv
from flask import (Flask, send_from_directory)
from blueprints import htmlBlueprint,apiBlueprint


load_dotenv()
app = Flask(__name__)

app.register_blueprint(apiBlueprint.bp)

@app.route('/<path:path>', methods=["GET"])
def static_files(path):
    return send_from_directory(directory="public/", path=path)

@app.route('/')
def index():
    return send_from_directory(directory="public/", path="index.html")