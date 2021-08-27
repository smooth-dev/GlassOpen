import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttributOut } from '../attribut-out.model';
import { AttributOutService } from '../service/attribut-out.service';

@Component({
  templateUrl: './attribut-out-delete-dialog.component.html',
})
export class AttributOutDeleteDialogComponent {
  attributOut?: IAttributOut;

  constructor(protected attributOutService: AttributOutService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attributOutService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
