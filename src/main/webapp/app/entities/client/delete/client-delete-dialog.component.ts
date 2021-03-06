import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';

@Component({
  templateUrl: './client-delete-dialog.component.html',
})
export class ClientDeleteDialogComponent {
  client?: IClient;

  constructor(protected clientService: ClientService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.clientService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
