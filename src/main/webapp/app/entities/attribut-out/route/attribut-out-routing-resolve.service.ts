import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttributOut, AttributOut } from '../attribut-out.model';
import { AttributOutService } from '../service/attribut-out.service';

@Injectable({ providedIn: 'root' })
export class AttributOutRoutingResolveService implements Resolve<IAttributOut> {
  constructor(protected service: AttributOutService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttributOut> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((attributOut: HttpResponse<AttributOut>) => {
          if (attributOut.body) {
            return of(attributOut.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AttributOut());
  }
}
