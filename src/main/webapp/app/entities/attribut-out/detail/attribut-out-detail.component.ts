import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttributOut } from '../attribut-out.model';

@Component({
  selector: 'jhi-attribut-out-detail',
  templateUrl: './attribut-out-detail.component.html',
})
export class AttributOutDetailComponent implements OnInit {
  attributOut: IAttributOut | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributOut }) => {
      this.attributOut = attributOut;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
