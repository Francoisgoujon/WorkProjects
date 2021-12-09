import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from "@ng-select/ng-select";
import { OrderModule } from 'ngx-order-pipe';
import { AppComponent } from './app.component';
import { DataTabComponent } from './components/data-tab/data-tab.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { StationDetailsComponent } from './components/station-details/station-details.component';
import { StationListComponent } from './components/station-list/station-list.component';
import { StatisticsTabComponent } from './components/statistics-tab/statistics-tab.component';
import { HistoryTabComponent } from './components/history-tab/history-tab.component';
import { ForecastTabComponent } from './components/forecast-tab/forecast-tab.component';
import { MapTabComponent } from './components/map-tab/map-tab.component';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    StationListComponent,
    StationDetailsComponent,
    DataTabComponent,
    StatisticsTabComponent,
    HistoryTabComponent,
    ForecastTabComponent,
    MapTabComponent
  ],
  imports: [
    BrowserModule,
    NgbModule,
    HttpClientModule,
    OrderModule,
    NgSelectModule,
    FormsModule,
    LeafletModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
