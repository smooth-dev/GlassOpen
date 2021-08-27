import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttribut } from '../attribut.model';
import { AttributService } from '../service/attribut.service';

@Component({
  templateUrl: './attribut-delete-dialog.component.html',
})
export class AttributDeleteDialogComponent {
  attribut?: IAttribut;

  constructor(protected attributService: AttributService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attributService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
