import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HardDeleteConfirmation } from './hard-delete-confirmation.component';

describe('HardDeleteConfirmation', () => {
  let component: HardDeleteConfirmation;
  let fixture: ComponentFixture<HardDeleteConfirmation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HardDeleteConfirmation]
    })
      .compileComponents();

    fixture = TestBed.createComponent(HardDeleteConfirmation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
