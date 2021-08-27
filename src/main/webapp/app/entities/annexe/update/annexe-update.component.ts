import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAnnexe, Annexe } from '../annexe.model';
import { AnnexeService } from '../service/annexe.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

@Component({
  selector: 'jhi-annexe-update',
  templateUrl: './annexe-update.component.html',
})
export class AnnexeUpdateComponent implements OnInit {
  isSaving = false;

  produitsSharedCollection: IProduit[] = [];

  editForm = this.fb.group({
    id: [],
    nomAnnexe: [null, [Validators.required]],
    enteteAnnexe: [null, [Validators.required]],
    produit: [],
  });

  constructor(
    protected annexeService: AnnexeService,
    protected produitService: ProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annexe }) => {
      this.updateForm(annexe);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annexe = this.createFromForm();
    if (annexe.id !== undefined) {
      this.subscribeToSaveResponse(this.annexeService.update(annexe));
    } else {
      this.subscribeToSaveResponse(this.annexeService.create(annexe));
    }
  }

  trackProduitById(index: number, item: IProduit): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnexe>>): void {
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

  protected updateForm(annexe: IAnnexe): void {
    this.editForm.patchValue({
      id: annexe.id,
      nomAnnexe: annexe.nomAnnexe,
      enteteAnnexe: annexe.enteteAnnexe,
      produit: annexe.produit,
    });

    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing(this.produitsSharedCollection, annexe.produit);
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

  protected createFromForm(): IAnnexe {
    return {
      ...new Annexe(),
      id: this.editForm.get(['id'])!.value,
      nomAnnexe: this.editForm.get(['nomAnnexe'])!.value,
      enteteAnnexe: this.editForm.get(['enteteAnnexe'])!.value,
      produit: this.editForm.get(['produit'])!.value,
    };
  }
}
