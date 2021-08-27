import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttributOutComponent } from '../list/attribut-out.component';
import { AttributOutDetailComponent } from '../detail/attribut-out-detail.component';
import { AttributOutUpdateComponent } from '../update/attribut-out-update.component';
import { AttributOutRoutingResolveService } from './attribut-out-routing-resolve.service';

const attributOutRoute: Routes = [
  {
    path: '',
    component: AttributOutComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttributOutDetailComponent,
    resolve: {
      attributOut: AttributOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttributOutUpdateComponent,
    resolve: {
      attributOut: AttributOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttributOutUpdateComponent,
    resolve: {
      attributOut: AttributOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attributOutRoute)],
  exports: [RouterModule],
})
export class AttributOutRoutingModule {}
