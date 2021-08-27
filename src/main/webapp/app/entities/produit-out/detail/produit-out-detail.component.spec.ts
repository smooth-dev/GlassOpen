import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProduitOutDetailComponent } from './produit-out-detail.component';

describe('Component Tests', () => {
  describe('ProduitOut Management Detail Component', () => {
    let comp: ProduitOutDetailComponent;
    let fixture: ComponentFixture<ProduitOutDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProduitOutDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ produitOut: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(ProduitOutDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProduitOutDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load produitOut on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.produitOut).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
