import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttributComponent } from '../list/attribut.component';
import { AttributDetailComponent } from '../detail/attribut-detail.component';
import { AttributUpdateComponent } from '../update/attribut-update.component';
import { AttributRoutingResolveService } from './attribut-routing-resolve.service';

const attributRoute: Routes = [
  {
    path: '',
    component: AttributComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttributDetailComponent,
    resolve: {
      attribut: AttributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttributUpdateComponent,
    resolve: {
      attribut: AttributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttributUpdateComponent,
    resolve: {
      attribut: AttributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attributRoute)],
  exports: [RouterModule],
})
export class AttributRoutingModule {}
