import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnnexeOut, AnnexeOut } from '../annexe-out.model';
import { AnnexeOutService } from '../service/annexe-out.service';

@Injectable({ providedIn: 'root' })
export class AnnexeOutRoutingResolveService implements Resolve<IAnnexeOut> {
  constructor(protected service: AnnexeOutService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnnexeOut> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((annexeOut: HttpResponse<AnnexeOut>) => {
          if (annexeOut.body) {
            return of(annexeOut.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnnexeOut());
  }
}
