import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnnexeDetailComponent } from './annexe-detail.component';

describe('Component Tests', () => {
  describe('Annexe Management Detail Component', () => {
    let comp: AnnexeDetailComponent;
    let fixture: ComponentFixture<AnnexeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnnexeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ annexe: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AnnexeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnnexeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load annexe on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.annexe).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
