import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AttributOutComponent } from './list/attribut-out.component';
import { AttributOutDetailComponent } from './detail/attribut-out-detail.component';
import { AttributOutUpdateComponent } from './update/attribut-out-update.component';
import { AttributOutDeleteDialogComponent } from './delete/attribut-out-delete-dialog.component';
import { AttributOutRoutingModule } from './route/attribut-out-routing.module';

@NgModule({
  imports: [SharedModule, AttributOutRoutingModule],
  declarations: [AttributOutComponent, AttributOutDetailComponent, AttributOutUpdateComponent, AttributOutDeleteDialogComponent],
  entryComponents: [AttributOutDeleteDialogComponent],
})
export class AttributOutModule {}
