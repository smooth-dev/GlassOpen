import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDossier } from '../dossier.model';
import { DossierService } from '../service/dossier.service';
import { DossierDeleteDialogComponent } from '../delete/dossier-delete-dialog.component';

@Component({
  selector: 'jhi-dossier',
  templateUrl: './dossier.component.html',
})
export class DossierComponent implements OnInit {
  dossiers?: IDossier[];
  isLoading = false;

  constructor(protected dossierService: DossierService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dossierService.query().subscribe(
      (res: HttpResponse<IDossier[]>) => {
        this.isLoading = false;
        this.dossiers = res.body ?? [];
        console.log('========>', this.dossiers);
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDossier): string {
    return item.id!;
  }

  delete(dossier: IDossier): void {
    const modalRef = this.modalService.open(DossierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dossier = dossier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
