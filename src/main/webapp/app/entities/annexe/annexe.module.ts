import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnnexeComponent } from './list/annexe.component';
import { AnnexeDetailComponent } from './detail/annexe-detail.component';
import { AnnexeUpdateComponent } from './update/annexe-update.component';
import { AnnexeDeleteDialogComponent } from './delete/annexe-delete-dialog.component';
import { AnnexeRoutingModule } from './route/annexe-routing.module';

@NgModule({
  imports: [SharedModule, AnnexeRoutingModule],
  declarations: [AnnexeComponent, AnnexeDetailComponent, AnnexeUpdateComponent, AnnexeDeleteDialogComponent],
  entryComponents: [AnnexeDeleteDialogComponent],
})
export class AnnexeModule {}
