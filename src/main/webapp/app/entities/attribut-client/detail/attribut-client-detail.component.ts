import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttributClient } from '../attribut-client.model';

@Component({
  selector: 'jhi-attribut-client-detail',
  templateUrl: './attribut-client-detail.component.html',
})
export class AttributClientDetailComponent implements OnInit {
  attributClient: IAttributClient | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributClient }) => {
      this.attributClient = attributClient;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
