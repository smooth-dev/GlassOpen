import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDossier } from '../dossier.model';
import { DossierService } from '../service/dossier.service';

@Component({
  templateUrl: './dossier-delete-dialog.component.html',
})
export class DossierDeleteDialogComponent {
  dossier?: IDossier;

  constructor(protected dossierService: DossierService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.dossierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
