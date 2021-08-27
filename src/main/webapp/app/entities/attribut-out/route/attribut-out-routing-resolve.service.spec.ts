jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAttributOut, AttributOut } from '../attribut-out.model';
import { AttributOutService } from '../service/attribut-out.service';

import { AttributOutRoutingResolveService } from './attribut-out-routing-resolve.service';

describe('Service Tests', () => {
  describe('AttributOut routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AttributOutRoutingResolveService;
    let service: AttributOutService;
    let resultAttributOut: IAttributOut | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AttributOutRoutingResolveService);
      service = TestBed.inject(AttributOutService);
      resultAttributOut = undefined;
    });

    describe('resolve', () => {
      it('should return IAttributOut returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttributOut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAttributOut).toEqual({ id: 'ABC' });
      });

      it('should return new IAttributOut if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttributOut = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAttributOut).toEqual(new AttributOut());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttributOut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAttributOut).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
