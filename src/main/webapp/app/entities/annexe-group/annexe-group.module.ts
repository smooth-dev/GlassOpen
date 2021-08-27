import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnnexeGroupComponent } from './list/annexe-group.component';
import { AnnexeGroupDetailComponent } from './detail/annexe-group-detail.component';
import { AnnexeGroupUpdateComponent } from './update/annexe-group-update.component';
import { AnnexeGroupDeleteDialogComponent } from './delete/annexe-group-delete-dialog.component';
import { AnnexeGroupRoutingModule } from './route/annexe-group-routing.module';

@NgModule({
  imports: [SharedModule, AnnexeGroupRoutingModule],
  declarations: [AnnexeGroupComponent, AnnexeGroupDetailComponent, AnnexeGroupUpdateComponent, AnnexeGroupDeleteDialogComponent],
  entryComponents: [AnnexeGroupDeleteDialogComponent],
})
export class AnnexeGroupModule {}
