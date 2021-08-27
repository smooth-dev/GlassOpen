import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProduitOut } from '../produit-out.model';
import { ProduitOutService } from '../service/produit-out.service';

@Component({
  templateUrl: './produit-out-delete-dialog.component.html',
})
export class ProduitOutDeleteDialogComponent {
  produitOut?: IProduitOut;

  constructor(protected produitOutService: ProduitOutService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.produitOutService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
