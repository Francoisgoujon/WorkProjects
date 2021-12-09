# Gti525Labo

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 11.2.11.

## Set up development environment

- Install Node.js LTS version 10.x from https://nodejs.org/en/ , check the installed nodejs version with `node -v` and the npm version with `npm -v`
- Install the latest version of VS Code for Linux by following https://code.visualstudio.com/docs/setup/linux
- In VS Code, install the Angular Essentials - Extension Pack for VS Code (by John Papa, which is a bundle of essential extensions to develop Angular apps in VS Code).
- Configure Prettier extension as the default formatter for .ts, .js, .json and .css files.
- Configure VS Code built in HTML Language Feature as the default formatter for .html files (Since Prettier doesn't format html files nicely)
- Configure automatically organize imports when save file: in VS Code, File->Preferences->Settings, search for "Code Actions On Save", then click "Edit in settings.json", make sure you have "source.organizeImports": true
- Run `npm install` to install all the npm modules for this project.
- Note that below Git hooks exist:
  When you commit: For staged files: 1) Organizing imports will be automatically performed on .ts and .js files, and
  2) Formatting will be done by Prettier on files not listed in .prettierignore. So .ts, js and .css files will be formatted, but .html files won't.
  When you push: ng lint will run on the .ts and .js files in your LAST commit, and if there are any lint errors, your push will be blocked.


## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.
