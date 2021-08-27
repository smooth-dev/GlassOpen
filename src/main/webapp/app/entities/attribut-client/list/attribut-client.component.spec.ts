import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AttributClientService } from '../service/attribut-client.service';

import { AttributClientComponent } from './attribut-client.component';

describe('Component Tests', () => {
  describe('AttributClient Management Component', () => {
    let comp: AttributClientComponent;
    let fixture: ComponentFixture<AttributClientComponent>;
    let service: AttributClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributClientComponent],
      })
        .overrideTemplate(AttributClientComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributClientComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AttributClientService);

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
      expect(comp.attributClients?.[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });
  });
});
