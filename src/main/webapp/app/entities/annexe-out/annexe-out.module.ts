import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnnexeOutComponent } from './list/annexe-out.component';
import { AnnexeOutDetailComponent } from './detail/annexe-out-detail.component';
import { AnnexeOutUpdateComponent } from './update/annexe-out-update.component';
import { AnnexeOutDeleteDialogComponent } from './delete/annexe-out-delete-dialog.component';
import { AnnexeOutRoutingModule } from './route/annexe-out-routing.module';

@NgModule({
  imports: [SharedModule, AnnexeOutRoutingModule],
  declarations: [AnnexeOutComponent, AnnexeOutDetailComponent, AnnexeOutUpdateComponent, AnnexeOutDeleteDialogComponent],
  entryComponents: [AnnexeOutDeleteDialogComponent],
})
export class AnnexeOutModule {}
