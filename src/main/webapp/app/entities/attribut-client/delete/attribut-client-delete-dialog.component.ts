import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttributClient } from '../attribut-client.model';
import { AttributClientService } from '../service/attribut-client.service';

@Component({
  templateUrl: './attribut-client-delete-dialog.component.html',
})
export class AttributClientDeleteDialogComponent {
  attributClient?: IAttributClient;

  constructor(protected attributClientService: AttributClientService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attributClientService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
