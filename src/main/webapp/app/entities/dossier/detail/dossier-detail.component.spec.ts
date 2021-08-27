import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DossierDetailComponent } from './dossier-detail.component';

describe('Component Tests', () => {
  describe('Dossier Management Detail Component', () => {
    let comp: DossierDetailComponent;
    let fixture: ComponentFixture<DossierDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DossierDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dossier: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(DossierDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DossierDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dossier on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dossier).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
