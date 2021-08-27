jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAttributClient, AttributClient } from '../attribut-client.model';
import { AttributClientService } from '../service/attribut-client.service';

import { AttributClientRoutingResolveService } from './attribut-client-routing-resolve.service';

describe('Service Tests', () => {
  describe('AttributClient routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AttributClientRoutingResolveService;
    let service: AttributClientService;
    let resultAttributClient: IAttributClient | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AttributClientRoutingResolveService);
      service = TestBed.inject(AttributClientService);
      resultAttributClient = undefined;
    });

    describe('resolve', () => {
      it('should return IAttributClient returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttributClient = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAttributClient).toEqual({ id: 'ABC' });
      });

      it('should return new IAttributClient if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttributClient = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAttributClient).toEqual(new AttributClient());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttributClient = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAttributClient).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
