import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnnexeOutDetailComponent } from './annexe-out-detail.component';

describe('Component Tests', () => {
  describe('AnnexeOut Management Detail Component', () => {
    let comp: AnnexeOutDetailComponent;
    let fixture: ComponentFixture<AnnexeOutDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnnexeOutDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ annexeOut: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AnnexeOutDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnnexeOutDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load annexeOut on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.annexeOut).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
