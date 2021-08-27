import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProduitOut, ProduitOut } from '../produit-out.model';
import { ProduitOutService } from '../service/produit-out.service';

@Injectable({ providedIn: 'root' })
export class ProduitOutRoutingResolveService implements Resolve<IProduitOut> {
  constructor(protected service: ProduitOutService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProduitOut> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((produitOut: HttpResponse<ProduitOut>) => {
          if (produitOut.body) {
            return of(produitOut.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProduitOut());
  }
}
