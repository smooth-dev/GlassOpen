jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAnnexeOut, AnnexeOut } from '../annexe-out.model';
import { AnnexeOutService } from '../service/annexe-out.service';

import { AnnexeOutRoutingResolveService } from './annexe-out-routing-resolve.service';

describe('Service Tests', () => {
  describe('AnnexeOut routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AnnexeOutRoutingResolveService;
    let service: AnnexeOutService;
    let resultAnnexeOut: IAnnexeOut | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AnnexeOutRoutingResolveService);
      service = TestBed.inject(AnnexeOutService);
      resultAnnexeOut = undefined;
    });

    describe('resolve', () => {
      it('should return IAnnexeOut returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnnexeOut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAnnexeOut).toEqual({ id: 'ABC' });
      });

      it('should return new IAnnexeOut if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnnexeOut = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAnnexeOut).toEqual(new AnnexeOut());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnnexeOut = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultAnnexeOut).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
