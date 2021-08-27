import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnexeComponent } from '../list/annexe.component';
import { AnnexeDetailComponent } from '../detail/annexe-detail.component';
import { AnnexeUpdateComponent } from '../update/annexe-update.component';
import { AnnexeRoutingResolveService } from './annexe-routing-resolve.service';

const annexeRoute: Routes = [
  {
    path: '',
    component: AnnexeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnnexeDetailComponent,
    resolve: {
      annexe: AnnexeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnnexeUpdateComponent,
    resolve: {
      annexe: AnnexeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnexeUpdateComponent,
    resolve: {
      annexe: AnnexeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(annexeRoute)],
  exports: [RouterModule],
})
export class AnnexeRoutingModule {}
