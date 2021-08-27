import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttributDetailComponent } from './attribut-detail.component';

describe('Component Tests', () => {
  describe('Attribut Management Detail Component', () => {
    let comp: AttributDetailComponent;
    let fixture: ComponentFixture<AttributDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AttributDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ attribut: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AttributDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttributDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attribut on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attribut).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
