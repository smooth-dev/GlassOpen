import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduitOut, ProduitOut } from '../produit-out.model';
import { ProduitOutService } from '../service/produit-out.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

@Component({
  selector: 'jhi-produit-out-update',
  templateUrl: './produit-out-update.component.html',
})
export class ProduitOutUpdateComponent implements OnInit {
  isSaving = false;

  produitsSharedCollection: IProduit[] = [];

  editForm = this.fb.group({
    id: [],
    nomAnnexe: [null, [Validators.required]],
    produit: [],
  });

  constructor(
    protected produitOutService: ProduitOutService,
    protected produitService: ProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produitOut }) => {
      this.updateForm(produitOut);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produitOut = this.createFromForm();
    if (produitOut.id !== undefined) {
      this.subscribeToSaveResponse(this.produitOutService.update(produitOut));
    } else {
      this.subscribeToSaveResponse(this.produitOutService.create(produitOut));
    }
  }

  trackProduitById(index: number, item: IProduit): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduitOut>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(produitOut: IProduitOut): void {
    this.editForm.patchValue({
      id: produitOut.id,
      nomAnnexe: produitOut.nomAnnexe,
      produit: produitOut.produit,
    });

    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing(this.produitsSharedCollection, produitOut.produit);
  }

  protected loadRelationshipsOptions(): void {
    this.produitService
      .query()
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) => this.produitService.addProduitToCollectionIfMissing(produits, this.editForm.get('produit')!.value))
      )
      .subscribe((produits: IProduit[]) => (this.produitsSharedCollection = produits));
  }

  protected createFromForm(): IProduitOut {
    return {
      ...new ProduitOut(),
      id: this.editForm.get(['id'])!.value,
      nomAnnexe: this.editForm.get(['nomAnnexe'])!.value,
      produit: this.editForm.get(['produit'])!.value,
    };
  }
}
