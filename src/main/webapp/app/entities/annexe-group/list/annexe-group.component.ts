import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnexeGroup } from '../annexe-group.model';
import { AnnexeGroupService } from '../service/annexe-group.service';
import { AnnexeGroupDeleteDialogComponent } from '../delete/annexe-group-delete-dialog.component';

@Component({
  selector: 'jhi-annexe-group',
  templateUrl: './annexe-group.component.html',
})
export class AnnexeGroupComponent implements OnInit {
  annexeGroups?: IAnnexeGroup[];
  isLoading = false;

  constructor(protected annexeGroupService: AnnexeGroupService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.annexeGroupService.query().subscribe(
      (res: HttpResponse<IAnnexeGroup[]>) => {
        this.isLoading = false;
        this.annexeGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAnnexeGroup): string {
    return item.id!;
  }

  delete(annexeGroup: IAnnexeGroup): void {
    const modalRef = this.modalService.open(AnnexeGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.annexeGroup = annexeGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
