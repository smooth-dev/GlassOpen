import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDossier, Dossier } from '../dossier.model';
import { DossierService } from '../service/dossier.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

@Component({
  selector: 'jhi-dossier-update',
  templateUrl: './dossier-update.component.html',
})
export class DossierUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];
  produitsSharedCollection: IProduit[] = [];

  editForm = this.fb.group({
    id: [],
    numeroDossier: [null, [Validators.required]],
    client: [],
    produit: [],
  });

  constructor(
    protected dossierService: DossierService,
    protected clientService: ClientService,
    protected produitService: ProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossier }) => {
      this.updateForm(dossier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dossier = this.createFromForm();
    if (dossier.id !== undefined) {
      this.subscribeToSaveResponse(this.dossierService.update(dossier));
    } else {
      this.subscribeToSaveResponse(this.dossierService.create(dossier));
    }
  }

  trackClientById(index: number, item: IClient): string {
    return item.id!;
  }

  trackProduitById(index: number, item: IProduit): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossier>>): void {
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

  protected updateForm(dossier: IDossier): void {
    this.editForm.patchValue({
      id: dossier.id,
      numeroDossier: dossier.numeroDossier,
      client: dossier.client,
      produit: dossier.produit,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, dossier.client);
    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing(this.produitsSharedCollection, dossier.produit);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.produitService
      .query()
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) => this.produitService.addProduitToCollectionIfMissing(produits, this.editForm.get('produit')!.value))
      )
      .subscribe((produits: IProduit[]) => (this.produitsSharedCollection = produits));
  }

  protected createFromForm(): IDossier {
    return {
      ...new Dossier(),
      id: this.editForm.get(['id'])!.value,
      numeroDossier: this.editForm.get(['numeroDossier'])!.value,
      client: this.editForm.get(['client'])!.value,
      produit: this.editForm.get(['produit'])!.value,
    };
  }
}
