import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AttributComponent } from './list/attribut.component';
import { AttributDetailComponent } from './detail/attribut-detail.component';
import { AttributUpdateComponent } from './update/attribut-update.component';
import { AttributDeleteDialogComponent } from './delete/attribut-delete-dialog.component';
import { AttributRoutingModule } from './route/attribut-routing.module';

@NgModule({
  imports: [SharedModule, AttributRoutingModule],
  declarations: [AttributComponent, AttributDetailComponent, AttributUpdateComponent, AttributDeleteDialogComponent],
  entryComponents: [AttributDeleteDialogComponent],
})
export class AttributModule {}
