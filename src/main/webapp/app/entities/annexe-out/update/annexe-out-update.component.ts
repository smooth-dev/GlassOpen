import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAnnexeOut, AnnexeOut } from '../annexe-out.model';
import { AnnexeOutService } from '../service/annexe-out.service';
import { IAnnexeGroup } from 'app/entities/annexe-group/annexe-group.model';
import { AnnexeGroupService } from 'app/entities/annexe-group/service/annexe-group.service';

@Component({
  selector: 'jhi-annexe-out-update',
  templateUrl: './annexe-out-update.component.html',
})
export class AnnexeOutUpdateComponent implements OnInit {
  isSaving = false;

  annexeGroupsSharedCollection: IAnnexeGroup[] = [];

  editForm = this.fb.group({
    id: [],
    nomAnnexe: [null, [Validators.required]],
    idClient: [null, [Validators.required]],
    annexegroup: [],
  });

  constructor(
    protected annexeOutService: AnnexeOutService,
    protected annexeGroupService: AnnexeGroupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annexeOut }) => {
      this.updateForm(annexeOut);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annexeOut = this.createFromForm();
    if (annexeOut.id !== undefined) {
      this.subscribeToSaveResponse(this.annexeOutService.update(annexeOut));
    } else {
      this.subscribeToSaveResponse(this.annexeOutService.create(annexeOut));
    }
  }

  trackAnnexeGroupById(index: number, item: IAnnexeGroup): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnexeOut>>): void {
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

  protected updateForm(annexeOut: IAnnexeOut): void {
    this.editForm.patchValue({
      id: annexeOut.id,
      nomAnnexe: annexeOut.nomAnnexe,
      idClient: annexeOut.idClient,
      annexegroup: annexeOut.annexegroup,
    });

    this.annexeGroupsSharedCollection = this.annexeGroupService.addAnnexeGroupToCollectionIfMissing(
      this.annexeGroupsSharedCollection,
      annexeOut.annexegroup
    );
  }

  protected loadRelationshipsOptions(): void {
    this.annexeGroupService
      .query()
      .pipe(map((res: HttpResponse<IAnnexeGroup[]>) => res.body ?? []))
      .pipe(
        map((annexeGroups: IAnnexeGroup[]) =>
          this.annexeGroupService.addAnnexeGroupToCollectionIfMissing(annexeGroups, this.editForm.get('annexegroup')!.value)
        )
      )
      .subscribe((annexeGroups: IAnnexeGroup[]) => (this.annexeGroupsSharedCollection = annexeGroups));
  }

  protected createFromForm(): IAnnexeOut {
    return {
      ...new AnnexeOut(),
      id: this.editForm.get(['id'])!.value,
      nomAnnexe: this.editForm.get(['nomAnnexe'])!.value,
      idClient: this.editForm.get(['idClient'])!.value,
      annexegroup: this.editForm.get(['annexegroup'])!.value,
    };
  }
}
