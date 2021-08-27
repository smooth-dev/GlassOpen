import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnexe } from '../annexe.model';
import { AnnexeService } from '../service/annexe.service';

@Component({
  templateUrl: './annexe-delete-dialog.component.html',
})
export class AnnexeDeleteDialogComponent {
  annexe?: IAnnexe;

  constructor(protected annexeService: AnnexeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.annexeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
