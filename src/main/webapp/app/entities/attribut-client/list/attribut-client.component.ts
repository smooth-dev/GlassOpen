import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttributClient } from '../attribut-client.model';
import { AttributClientService } from '../service/attribut-client.service';
import { AttributClientDeleteDialogComponent } from '../delete/attribut-client-delete-dialog.component';

@Component({
  selector: 'jhi-attribut-client',
  templateUrl: './attribut-client.component.html',
})
export class AttributClientComponent implements OnInit {
  attributClients?: IAttributClient[];
  isLoading = false;

  constructor(protected attributClientService: AttributClientService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.attributClientService.query().subscribe(
      (res: HttpResponse<IAttributClient[]>) => {
        this.isLoading = false;
        this.attributClients = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAttributClient): string {
    return item.id!;
  }

  delete(attributClient: IAttributClient): void {
    const modalRef = this.modalService.open(AttributClientDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attributClient = attributClient;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
