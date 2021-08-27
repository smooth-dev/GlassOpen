import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnexeGroupComponent } from '../list/annexe-group.component';
import { AnnexeGroupDetailComponent } from '../detail/annexe-group-detail.component';
import { AnnexeGroupUpdateComponent } from '../update/annexe-group-update.component';
import { AnnexeGroupRoutingResolveService } from './annexe-group-routing-resolve.service';

const annexeGroupRoute: Routes = [
  {
    path: '',
    component: AnnexeGroupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnnexeGroupDetailComponent,
    resolve: {
      annexeGroup: AnnexeGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnnexeGroupUpdateComponent,
    resolve: {
      annexeGroup: AnnexeGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnexeGroupUpdateComponent,
    resolve: {
      annexeGroup: AnnexeGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(annexeGroupRoute)],
  exports: [RouterModule],
})
export class AnnexeGroupRoutingModule {}
