import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttributOut } from '../attribut-out.model';
import { AttributOutService } from '../service/attribut-out.service';
import { AttributOutDeleteDialogComponent } from '../delete/attribut-out-delete-dialog.component';

@Component({
  selector: 'jhi-attribut-out',
  templateUrl: './attribut-out.component.html',
})
export class AttributOutComponent implements OnInit {
  attributOuts?: IAttributOut[];
  isLoading = false;

  constructor(protected attributOutService: AttributOutService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.attributOutService.query().subscribe(
      (res: HttpResponse<IAttributOut[]>) => {
        this.isLoading = false;
        this.attributOuts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAttributOut): string {
    return item.id!;
  }

  delete(attributOut: IAttributOut): void {
    const modalRef = this.modalService.open(AttributOutDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attributOut = attributOut;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
