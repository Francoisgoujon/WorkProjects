import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForecastTabComponent } from './forecast-tab.component';

describe('ForecastTabComponent', () => {
  let component: ForecastTabComponent;
  let fixture: ComponentFixture<ForecastTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ForecastTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ForecastTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
