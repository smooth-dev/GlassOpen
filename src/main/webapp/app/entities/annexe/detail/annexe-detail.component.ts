import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnexe } from '../annexe.model';

@Component({
  selector: 'jhi-annexe-detail',
  templateUrl: './annexe-detail.component.html',
})
export class AnnexeDetailComponent implements OnInit {
  annexe: IAnnexe | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annexe }) => {
      this.annexe = annexe;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
