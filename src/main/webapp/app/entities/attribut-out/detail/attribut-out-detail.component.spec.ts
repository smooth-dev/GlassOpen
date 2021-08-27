import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttributOutDetailComponent } from './attribut-out-detail.component';

describe('Component Tests', () => {
  describe('AttributOut Management Detail Component', () => {
    let comp: AttributOutDetailComponent;
    let fixture: ComponentFixture<AttributOutDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AttributOutDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ attributOut: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AttributOutDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttributOutDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attributOut on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attributOut).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
