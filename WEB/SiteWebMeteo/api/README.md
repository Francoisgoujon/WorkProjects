# TP1

## How to run the app
```
export FLASK_APP=src/main.py
flask run
```
or 

```
FLASK_APP=src/main.py flask run
```

## Setup for Visual Studio Code

In order to run the project on VSCode you will need to edit the `.vscode/launch.json`. You can create one if it does not already exist.
```json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "name": "Python: Flask",
            "type": "python",
            "request": "launch",
            "module": "flask",
            "env": {
                "FLASK_APP": "src/main.py",
                "FLASK_ENV": "development"
            },
            "args": [
                "run",
                "--no-debugger"
            ],
            "jinja": true
        }
    ]
}
```