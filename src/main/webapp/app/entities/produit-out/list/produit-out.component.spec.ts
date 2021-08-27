import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProduitOutService } from '../service/produit-out.service';

import { ProduitOutComponent } from './produit-out.component';

describe('Component Tests', () => {
  describe('ProduitOut Management Component', () => {
    let comp: ProduitOutComponent;
    let fixture: ComponentFixture<ProduitOutComponent>;
    let service: ProduitOutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProduitOutComponent],
      })
        .overrideTemplate(ProduitOutComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProduitOutComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProduitOutService);

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
      expect(comp.produitOuts?.[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });
  });
});
