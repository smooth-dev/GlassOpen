import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnnexe, Annexe } from '../annexe.model';
import { AnnexeService } from '../service/annexe.service';

@Injectable({ providedIn: 'root' })
export class AnnexeRoutingResolveService implements Resolve<IAnnexe> {
  constructor(protected service: AnnexeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnnexe> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((annexe: HttpResponse<Annexe>) => {
          if (annexe.body) {
            return of(annexe.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Annexe());
  }
}
