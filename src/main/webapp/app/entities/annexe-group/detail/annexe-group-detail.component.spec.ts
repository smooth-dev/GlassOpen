import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnnexeGroupDetailComponent } from './annexe-group-detail.component';

describe('Component Tests', () => {
  describe('AnnexeGroup Management Detail Component', () => {
    let comp: AnnexeGroupDetailComponent;
    let fixture: ComponentFixture<AnnexeGroupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnnexeGroupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ annexeGroup: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AnnexeGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnnexeGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load annexeGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.annexeGroup).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
