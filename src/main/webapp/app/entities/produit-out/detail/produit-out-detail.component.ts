import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProduitOut } from '../produit-out.model';

@Component({
  selector: 'jhi-produit-out-detail',
  templateUrl: './produit-out-detail.component.html',
})
export class ProduitOutDetailComponent implements OnInit {
  produitOut: IProduitOut | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produitOut }) => {
      this.produitOut = produitOut;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
