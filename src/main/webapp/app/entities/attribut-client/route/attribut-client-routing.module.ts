import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttributClientComponent } from '../list/attribut-client.component';
import { AttributClientDetailComponent } from '../detail/attribut-client-detail.component';
import { AttributClientUpdateComponent } from '../update/attribut-client-update.component';
import { AttributClientRoutingResolveService } from './attribut-client-routing-resolve.service';

const attributClientRoute: Routes = [
  {
    path: '',
    component: AttributClientComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttributClientDetailComponent,
    resolve: {
      attributClient: AttributClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttributClientUpdateComponent,
    resolve: {
      attributClient: AttributClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/new',
    component: AttributClientUpdateComponent,
    resolve: {
      attributClient: AttributClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttributClientUpdateComponent,
    resolve: {
      attributClient: AttributClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attributClientRoute)],
  exports: [RouterModule],
})
export class AttributClientRoutingModule {}
