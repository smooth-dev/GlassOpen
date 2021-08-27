import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { MatAccordion } from '@angular/material/expansion';

import { IAnnexeOut } from '../annexe-out.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { IDossier } from 'app/entities/dossier/dossier.model';

import { AnnexeOutService } from '../service/annexe-out.service';
import { ProduitService } from '../../produit/service/produit.service';
import { DossierService } from '../../dossier/service/dossier.service';

import { AnnexeOutDeleteDialogComponent } from '../delete/annexe-out-delete-dialog.component';

@Component({
  selector: 'jhi-annexe-out',
  templateUrl: './annexe-out.component.html',
  styleUrls: ['./annexe-out.component.scss'],
})
export class AnnexeOutComponent implements OnInit {
  annexeOuts?: IAnnexeOut[];
  produits?: IProduit[];
  produitsFiltered?: IProduit;

  dossiers?: IDossier[];

  selectedOption?: string;
  selectedDossier?: string;
  printedOption?: string;
  printedDossier?: string;

  @ViewChild(MatAccordion)
  accordion: MatAccordion = new MatAccordion();

  options = [
    { name: 'option1', value: 1 },
    { name: 'option2', value: 2 },
  ];

  isLoading = false;
  editForm = this.fb.group({
    produit: [],
  });

  constructor(
    protected annexeOutService: AnnexeOutService,
    protected produitService: ProduitService,
    protected dossierService: DossierService,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  mapthis(index: number): string {
    switch (index) {
      case 0: {
        return 'zero';
        break;
      }
      case 1: {
        return 'one';
        break;
      }
      case 2: {
        return 'two';
        break;
      }
      case 3: {
        return 'three';
        break;
      }
      default: {
        return 'rr';
        break;
      }
    }
  }

  loadAll(): void {
    this.isLoading = true;

    this.annexeOutService.query().subscribe(
      (res: HttpResponse<IAnnexeOut[]>) => {
        this.isLoading = false;
        this.annexeOuts = res.body ?? [];
        console.log('j', this.annexeOuts[0].attributclients);
      },
      () => {
        this.isLoading = false;
      }
    );

    this.produitService.query().subscribe(
      (res: HttpResponse<IAnnexeOut[]>) => {
        this.isLoading = false;
        this.produits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.dossierService.query().subscribe(
      (res: HttpResponse<IAnnexeOut[]>) => {
        this.isLoading = false;
        this.dossiers = res.body ?? [];
        console.log('ju');
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  test(): void {
    console.log(this.selectedOption);
    console.log(this.produits);
    if (this.selectedOption) {
      this.produitService.findByNomProduit(this.selectedOption).subscribe(
        (res: HttpResponse<IProduit[]>) => {
          this.isLoading = false;
          if (res.body) {
            this.annexeOuts = res.body;
          }
          console.log('after click filter', this.produits);
        },
        () => {
          this.isLoading = false;
        }
      );
    }
  }

  test2(): void {
    console.log(this.selectedDossier);
    console.log(this.dossiers);
    if (this.selectedDossier) {
      this.dossierService.findByNumeroDossier(this.selectedDossier).subscribe(
        (res: HttpResponse<IProduit>) => {
          this.isLoading = false;
          if (res.body) {
            this.produitsFiltered = res.body;
            this.produits = [];
            this.produits.push(this.produitsFiltered);

            console.log('fil', this.produits);
          }
        },
        () => {
          this.isLoading = false;
        }
      );
    }
  }

  print(): void {
    this.printedOption = this.selectedOption;
  }

  SelectedRow(): void {
    console.log('tt');
  }

  trackId(index: number, item: IAnnexeOut): string {
    return item.id!;
  }

  delete(annexeOut: IAnnexeOut): void {
    const modalRef = this.modalService.open(AnnexeOutDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.annexeOut = annexeOut;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
