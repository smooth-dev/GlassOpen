import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProduitOut } from '../produit-out.model';
import { ProduitOutService } from '../service/produit-out.service';
import { ProduitOutDeleteDialogComponent } from '../delete/produit-out-delete-dialog.component';

@Component({
  selector: 'jhi-produit-out',
  templateUrl: './produit-out.component.html',
})
export class ProduitOutComponent implements OnInit {
  produitOuts?: IProduitOut[];
  isLoading = false;

  constructor(protected produitOutService: ProduitOutService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.produitOutService.query().subscribe(
      (res: HttpResponse<IProduitOut[]>) => {
        this.isLoading = false;
        this.produitOuts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProduitOut): string {
    return item.id!;
  }

  delete(produitOut: IProduitOut): void {
    const modalRef = this.modalService.open(ProduitOutDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.produitOut = produitOut;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
