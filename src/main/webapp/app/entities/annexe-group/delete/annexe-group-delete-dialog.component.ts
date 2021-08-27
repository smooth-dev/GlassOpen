import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnexeGroup } from '../annexe-group.model';
import { AnnexeGroupService } from '../service/annexe-group.service';

@Component({
  templateUrl: './annexe-group-delete-dialog.component.html',
})
export class AnnexeGroupDeleteDialogComponent {
  annexeGroup?: IAnnexeGroup;

  constructor(protected annexeGroupService: AnnexeGroupService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.annexeGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
