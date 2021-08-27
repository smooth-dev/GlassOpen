import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnexeOutComponent } from '../list/annexe-out.component';
import { AnnexeOutDetailComponent } from '../detail/annexe-out-detail.component';
import { AnnexeOutUpdateComponent } from '../update/annexe-out-update.component';
import { AnnexeOutRoutingResolveService } from './annexe-out-routing-resolve.service';

const annexeOutRoute: Routes = [
  {
    path: '',
    component: AnnexeOutComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnnexeOutDetailComponent,
    resolve: {
      annexeOut: AnnexeOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnnexeOutUpdateComponent,
    resolve: {
      annexeOut: AnnexeOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnexeOutUpdateComponent,
    resolve: {
      annexeOut: AnnexeOutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(annexeOutRoute)],
  exports: [RouterModule],
})
export class AnnexeOutRoutingModule {}
