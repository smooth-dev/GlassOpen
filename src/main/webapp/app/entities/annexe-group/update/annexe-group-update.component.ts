import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAnnexeGroup, AnnexeGroup } from '../annexe-group.model';
import { AnnexeGroupService } from '../service/annexe-group.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

@Component({
  selector: 'jhi-annexe-group-update',
  templateUrl: './annexe-group-update.component.html',
})
export class AnnexeGroupUpdateComponent implements OnInit {
  isSaving = false;

  produitsSharedCollection: IProduit[] = [];

  editForm = this.fb.group({
    id: [],
    nomAnnexe: [null, [Validators.required]],
    produit: [],
  });

  constructor(
    protected annexeGroupService: AnnexeGroupService,
    protected produitService: ProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annexeGroup }) => {
      this.updateForm(annexeGroup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annexeGroup = this.createFromForm();
    if (annexeGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.annexeGroupService.update(annexeGroup));
    } else {
      this.subscribeToSaveResponse(this.annexeGroupService.create(annexeGroup));
    }
  }

  trackProduitById(index: number, item: IProduit): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnexeGroup>>): void {
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

  protected updateForm(annexeGroup: IAnnexeGroup): void {
    this.editForm.patchValue({
      id: annexeGroup.id,
      nomAnnexe: annexeGroup.nomAnnexe,
      produit: annexeGroup.produit,
    });

    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing(this.produitsSharedCollection, annexeGroup.produit);
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

  protected createFromForm(): IAnnexeGroup {
    return {
      ...new AnnexeGroup(),
      id: this.editForm.get(['id'])!.value,
      nomAnnexe: this.editForm.get(['nomAnnexe'])!.value,
      produit: this.editForm.get(['produit'])!.value,
    };
  }
}
