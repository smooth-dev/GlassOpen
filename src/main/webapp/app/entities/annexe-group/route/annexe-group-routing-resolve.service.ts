import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnnexeGroup, AnnexeGroup } from '../annexe-group.model';
import { AnnexeGroupService } from '../service/annexe-group.service';

@Injectable({ providedIn: 'root' })
export class AnnexeGroupRoutingResolveService implements Resolve<IAnnexeGroup> {
  constructor(protected service: AnnexeGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnnexeGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((annexeGroup: HttpResponse<AnnexeGroup>) => {
          if (annexeGroup.body) {
            return of(annexeGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnnexeGroup());
  }
}
