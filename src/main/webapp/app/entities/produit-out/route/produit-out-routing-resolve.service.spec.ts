jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProduitOut, ProduitOut } from '../produit-out.model';
import { ProduitOutService } from '../service/produit-out.service';

import { ProduitOutRoutingResolveService } from './produit-out-routing-resolve.service';

describe('Service Tests', () => {
  describe('ProduitOut routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProduitOutRoutingResolveService;
    let service: ProduitOutService;
    let resultProduitOut: IProduitOut | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProduitOutRoutingResolveService);
      service = TestBed.inject(ProduitOutService);
      resultProduitOut = undefined;
    });

    describe('resolve', () => {
      it('should return IProduitOut returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProduitOut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultProduitOut).toEqual({ id: 'ABC' });
      });

      it('should return new IProduitOut if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProduitOut = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultProduitOut).toEqual(new ProduitOut());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProduitOut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultProduitOut).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
