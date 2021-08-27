import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAttribut, Attribut } from '../attribut.model';
import { AttributService } from '../service/attribut.service';
import { IAnnexe } from 'app/entities/annexe/annexe.model';
import { AnnexeService } from 'app/entities/annexe/service/annexe.service';

@Component({
  selector: 'jhi-attribut-update',
  templateUrl: './attribut-update.component.html',
})
export class AttributUpdateComponent implements OnInit {
  isSaving = false;

  annexesSharedCollection: IAnnexe[] = [];

  editForm = this.fb.group({
    id: [],
    nomAttribut: [null, [Validators.required]],
    annexe: [],
  });

  constructor(
    protected attributService: AttributService,
    protected annexeService: AnnexeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attribut }) => {
      this.updateForm(attribut);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attribut = this.createFromForm();
    if (attribut.id !== undefined) {
      this.subscribeToSaveResponse(this.attributService.update(attribut));
    } else {
      this.subscribeToSaveResponse(this.attributService.create(attribut));
    }
  }

  trackAnnexeById(index: number, item: IAnnexe): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttribut>>): void {
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

  protected updateForm(attribut: IAttribut): void {
    this.editForm.patchValue({
      id: attribut.id,
      nomAttribut: attribut.nomAttribut,
      annexe: attribut.annexe,
    });

    this.annexesSharedCollection = this.annexeService.addAnnexeToCollectionIfMissing(this.annexesSharedCollection, attribut.annexe);
  }

  protected loadRelationshipsOptions(): void {
    this.annexeService
      .query()
      .pipe(map((res: HttpResponse<IAnnexe[]>) => res.body ?? []))
      .pipe(map((annexes: IAnnexe[]) => this.annexeService.addAnnexeToCollectionIfMissing(annexes, this.editForm.get('annexe')!.value)))
      .subscribe((annexes: IAnnexe[]) => (this.annexesSharedCollection = annexes));
  }

  protected createFromForm(): IAttribut {
    return {
      ...new Attribut(),
      id: this.editForm.get(['id'])!.value,
      nomAttribut: this.editForm.get(['nomAttribut'])!.value,
      annexe: this.editForm.get(['annexe'])!.value,
    };
  }
}
