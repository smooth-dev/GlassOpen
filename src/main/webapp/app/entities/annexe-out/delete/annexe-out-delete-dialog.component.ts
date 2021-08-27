import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnexeOut } from '../annexe-out.model';
import { AnnexeOutService } from '../service/annexe-out.service';

@Component({
  templateUrl: './annexe-out-delete-dialog.component.html',
})
export class AnnexeOutDeleteDialogComponent {
  annexeOut?: IAnnexeOut;

  constructor(protected annexeOutService: AnnexeOutService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.annexeOutService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
