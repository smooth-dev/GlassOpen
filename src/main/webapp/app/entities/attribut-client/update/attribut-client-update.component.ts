import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAttributClient, AttributClient } from '../attribut-client.model';
import { AttributClientService } from '../service/attribut-client.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IAnnexeOut } from 'app/entities/annexe-out/annexe-out.model';
import { AnnexeOutService } from 'app/entities/annexe-out/service/annexe-out.service';

@Component({
  selector: 'jhi-attribut-client-update',
  templateUrl: './attribut-client-update.component.html',
})
export class AttributClientUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];
  annexeOutsSharedCollection: IAnnexeOut[] = [];

  editForm = this.fb.group({
    id: [],
    nomAttribut: [null, [Validators.required]],
    contenu: [null, []],
    client: [],
    annexeout: [],
  });

  constructor(
    protected attributClientService: AttributClientService,
    protected clientService: ClientService,
    protected annexeOutService: AnnexeOutService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributClient }) => {
      this.updateForm(attributClient);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attributClient = this.createFromForm();
    if (attributClient.id !== undefined) {
      this.subscribeToSaveResponse(this.attributClientService.update(attributClient));
    } else {
      this.subscribeToSaveResponse(this.attributClientService.create(attributClient));
    }
  }

  trackClientById(index: number, item: IClient): string {
    return item.id!;
  }

  trackAnnexeOutById(index: number, item: IAnnexeOut): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttributClient>>): void {
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

  protected updateForm(attributClient: IAttributClient): void {
    this.editForm.patchValue({
      id: attributClient.id,
      nomAttribut: attributClient.nomAttribut,
      contenu: attributClient.contenu,
      client: attributClient.client,
      annexeout: attributClient.annexeout,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, attributClient.client);
    this.annexeOutsSharedCollection = this.annexeOutService.addAnnexeOutToCollectionIfMissing(
      this.annexeOutsSharedCollection,
      attributClient.annexeout
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.annexeOutService
      .query()
      .pipe(map((res: HttpResponse<IAnnexeOut[]>) => res.body ?? []))
      .pipe(
        map((annexeOuts: IAnnexeOut[]) =>
          this.annexeOutService.addAnnexeOutToCollectionIfMissing(annexeOuts, this.editForm.get('annexeout')!.value)
        )
      )
      .subscribe((annexeOuts: IAnnexeOut[]) => (this.annexeOutsSharedCollection = annexeOuts));
  }

  protected createFromForm(): IAttributClient {
    return {
      ...new AttributClient(),
      id: this.editForm.get(['id'])!.value,
      nomAttribut: this.editForm.get(['nomAttribut'])!.value,
      contenu: this.editForm.get(['contenu'])!.value,
      client: this.editForm.get(['client'])!.value,
      annexeout: this.editForm.get(['annexeout'])!.value,
    };
  }
}
