import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnexe } from '../annexe.model';
import { AnnexeService } from '../service/annexe.service';
import { AnnexeDeleteDialogComponent } from '../delete/annexe-delete-dialog.component';

@Component({
  selector: 'jhi-annexe',
  templateUrl: './annexe.component.html',
})
export class AnnexeComponent implements OnInit {
  annexes?: IAnnexe[];
  isLoading = false;

  constructor(protected annexeService: AnnexeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.annexeService.query().subscribe(
      (res: HttpResponse<IAnnexe[]>) => {
        this.isLoading = false;
        this.annexes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAnnexe): string {
    return item.id!;
  }

  delete(annexe: IAnnexe): void {
    const modalRef = this.modalService.open(AnnexeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.annexe = annexe;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
