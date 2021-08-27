import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AnnexeOutService } from '../service/annexe-out.service';

import { AnnexeOutComponent } from './annexe-out.component';

describe('Component Tests', () => {
  describe('AnnexeOut Management Component', () => {
    let comp: AnnexeOutComponent;
    let fixture: ComponentFixture<AnnexeOutComponent>;
    let service: AnnexeOutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnexeOutComponent],
      })
        .overrideTemplate(AnnexeOutComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnnexeOutComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AnnexeOutService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 'ABC' }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.annexeOuts?.[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });
  });
});
