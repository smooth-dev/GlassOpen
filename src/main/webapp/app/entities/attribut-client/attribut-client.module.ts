import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AttributClientComponent } from './list/attribut-client.component';
import { AttributClientDetailComponent } from './detail/attribut-client-detail.component';
import { AttributClientUpdateComponent } from './update/attribut-client-update.component';
import { AttributClientDeleteDialogComponent } from './delete/attribut-client-delete-dialog.component';
import { AttributClientRoutingModule } from './route/attribut-client-routing.module';

@NgModule({
  imports: [SharedModule, AttributClientRoutingModule],
  declarations: [
    AttributClientComponent,
    AttributClientDetailComponent,
    AttributClientUpdateComponent,
    AttributClientDeleteDialogComponent,
  ],
  entryComponents: [AttributClientDeleteDialogComponent],
})
export class AttributClientModule {}
