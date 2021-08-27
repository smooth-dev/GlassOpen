import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnexeGroup } from '../annexe-group.model';

@Component({
  selector: 'jhi-annexe-group-detail',
  templateUrl: './annexe-group-detail.component.html',
})
export class AnnexeGroupDetailComponent implements OnInit {
  annexeGroup: IAnnexeGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annexeGroup }) => {
      this.annexeGroup = annexeGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
