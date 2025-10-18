import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductPurchasePage } from './product-purchase-page';

describe('ProductPurchasePage', () => {
  let component: ProductPurchasePage;
  let fixture: ComponentFixture<ProductPurchasePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductPurchasePage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductPurchasePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
