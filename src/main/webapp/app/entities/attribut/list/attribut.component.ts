import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttribut } from '../attribut.model';
import { AttributService } from '../service/attribut.service';
import { AttributDeleteDialogComponent } from '../delete/attribut-delete-dialog.component';

@Component({
  selector: 'jhi-attribut',
  templateUrl: './attribut.component.html',
})
export class AttributComponent implements OnInit {
  attributs?: IAttribut[];
  isLoading = false;

  constructor(protected attributService: AttributService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.attributService.query().subscribe(
      (res: HttpResponse<IAttribut[]>) => {
        this.isLoading = false;
        this.attributs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAttribut): string {
    return item.id!;
  }

  delete(attribut: IAttribut): void {
    const modalRef = this.modalService.open(AttributDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attribut = attribut;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
