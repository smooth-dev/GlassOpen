import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnexeOut } from '../annexe-out.model';

@Component({
  selector: 'jhi-annexe-out-detail',
  templateUrl: './annexe-out-detail.component.html',
})
export class AnnexeOutDetailComponent implements OnInit {
  annexeOut: IAnnexeOut | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annexeOut }) => {
      this.annexeOut = annexeOut;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
