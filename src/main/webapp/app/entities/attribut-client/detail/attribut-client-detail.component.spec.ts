import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttributClientDetailComponent } from './attribut-client-detail.component';

describe('Component Tests', () => {
  describe('AttributClient Management Detail Component', () => {
    let comp: AttributClientDetailComponent;
    let fixture: ComponentFixture<AttributClientDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AttributClientDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ attributClient: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AttributClientDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttributClientDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attributClient on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attributClient).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
