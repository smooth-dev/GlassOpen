import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ProduitOutComponent } from './list/produit-out.component';
import { ProduitOutDetailComponent } from './detail/produit-out-detail.component';
import { ProduitOutUpdateComponent } from './update/produit-out-update.component';
import { ProduitOutDeleteDialogComponent } from './delete/produit-out-delete-dialog.component';
import { ProduitOutRoutingModule } from './route/produit-out-routing.module';

@NgModule({
  imports: [SharedModule, ProduitOutRoutingModule],
  declarations: [ProduitOutComponent, ProduitOutDetailComponent, ProduitOutUpdateComponent, ProduitOutDeleteDialogComponent],
  entryComponents: [ProduitOutDeleteDialogComponent],
})
export class ProduitOutModule {}
