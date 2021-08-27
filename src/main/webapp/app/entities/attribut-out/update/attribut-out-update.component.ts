import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAttributOut, AttributOut } from '../attribut-out.model';
import { AttributOutService } from '../service/attribut-out.service';

@Component({
  selector: 'jhi-attribut-out-update',
  templateUrl: './attribut-out-update.component.html',
})
export class AttributOutUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomAttribut: [null, [Validators.required]],
  });

  constructor(protected attributOutService: AttributOutService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributOut }) => {
      this.updateForm(attributOut);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attributOut = this.createFromForm();
    if (attributOut.id !== undefined) {
      this.subscribeToSaveResponse(this.attributOutService.update(attributOut));
    } else {
      this.subscribeToSaveResponse(this.attributOutService.create(attributOut));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttributOut>>): void {
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

  protected updateForm(attributOut: IAttributOut): void {
    this.editForm.patchValue({
      id: attributOut.id,
      nomAttribut: attributOut.nomAttribut,
    });
  }

  protected createFromForm(): IAttributOut {
    return {
      ...new AttributOut(),
      id: this.editForm.get(['id'])!.value,
      nomAttribut: this.editForm.get(['nomAttribut'])!.value,
    };
  }
}
