import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttribut } from '../attribut.model';

@Component({
  selector: 'jhi-attribut-detail',
  templateUrl: './attribut-detail.component.html',
})
export class AttributDetailComponent implements OnInit {
  attribut: IAttribut | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attribut }) => {
      this.attribut = attribut;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
