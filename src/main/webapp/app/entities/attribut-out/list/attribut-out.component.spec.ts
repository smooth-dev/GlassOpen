import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AttributOutService } from '../service/attribut-out.service';

import { AttributOutComponent } from './attribut-out.component';

describe('Component Tests', () => {
  describe('AttributOut Management Component', () => {
    let comp: AttributOutComponent;
    let fixture: ComponentFixture<AttributOutComponent>;
    let service: AttributOutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributOutComponent],
      })
        .overrideTemplate(AttributOutComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributOutComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AttributOutService);

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
      expect(comp.attributOuts?.[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });
  });
});
