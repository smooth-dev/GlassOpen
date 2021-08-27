import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProduitOutComponent } from '../list/produit-out.component';
import { ProduitOutDetailComponent } from '../detail/produit-out-detail.component';
import { ProduitOutUpdateComponent } from '../update/produit-out-update.component';
import { ProduitOutRoutingResolveService } from './produit-out-routing-resolve.service';

const produitOutRoute: Routes = [
  {
    path: '',
    component: ProduitOutComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProduitOutDetailComponent,
    resolve: {
      produitOut: ProduitOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProduitOutUpdateComponent,
    resolve: {
      produitOut: ProduitOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProduitOutUpdateComponent,
    resolve: {
      produitOut: ProduitOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(produitOutRoute)],
  exports: [RouterModule],
})
export class ProduitOutRoutingModule {}
