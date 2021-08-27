jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDossier, Dossier } from '../dossier.model';
import { DossierService } from '../service/dossier.service';

import { DossierRoutingResolveService } from './dossier-routing-resolve.service';

describe('Service Tests', () => {
  describe('Dossier routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DossierRoutingResolveService;
    let service: DossierService;
    let resultDossier: IDossier | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DossierRoutingResolveService);
      service = TestBed.inject(DossierService);
      resultDossier = undefined;
    });

    describe('resolve', () => {
      it('should return IDossier returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDossier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultDossier).toEqual({ id: 'ABC' });
      });

      it('should return new IDossier if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDossier = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDossier).toEqual(new Dossier());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDossier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultDossier).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
