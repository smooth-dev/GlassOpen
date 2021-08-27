jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAttribut, Attribut } from '../attribut.model';
import { AttributService } from '../service/attribut.service';

import { AttributRoutingResolveService } from './attribut-routing-resolve.service';

describe('Service Tests', () => {
  describe('Attribut routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AttributRoutingResolveService;
    let service: AttributService;
    let resultAttribut: IAttribut | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AttributRoutingResolveService);
      service = TestBed.inject(AttributService);
      resultAttribut = undefined;
    });

    describe('resolve', () => {
      it('should return IAttribut returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttribut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAttribut).toEqual({ id: 'ABC' });
      });

      it('should return new IAttribut if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttribut = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAttribut).toEqual(new Attribut());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAttribut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAttribut).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
