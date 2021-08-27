jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnnexeService } from '../service/annexe.service';
import { IAnnexe, Annexe } from '../annexe.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

import { AnnexeUpdateComponent } from './annexe-update.component';

describe('Component Tests', () => {
  describe('Annexe Management Update Component', () => {
    let comp: AnnexeUpdateComponent;
    let fixture: ComponentFixture<AnnexeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let annexeService: AnnexeService;
    let produitService: ProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnexeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnnexeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnnexeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      annexeService = TestBed.inject(AnnexeService);
      produitService = TestBed.inject(ProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Produit query and add missing value', () => {
        const annexe: IAnnexe = { id: 'CBA' };
        const produit: IProduit = { id: 'next de' };
        annexe.produit = produit;

        const produitCollection: IProduit[] = [{ id: 'deliverables' }];
        spyOn(produitService, 'query').and.returnValue(of(new HttpResponse({ body: produitCollection })));
        const additionalProduits = [produit];
        const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
        spyOn(produitService, 'addProduitToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ annexe });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitCollection, ...additionalProduits);
        expect(comp.produitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const annexe: IAnnexe = { id: 'CBA' };
        const produit: IProduit = { id: 'Cambridgeshire SCSI Centre' };
        annexe.produit = produit;

        activatedRoute.data = of({ annexe });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(annexe));
        expect(comp.produitsSharedCollection).toContain(produit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexe = { id: 'ABC' };
        spyOn(annexeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annexe }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(annexeService.update).toHaveBeenCalledWith(annexe);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexe = new Annexe();
        spyOn(annexeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annexe }));
        saveSubject.complete();

        // THEN
        expect(annexeService.create).toHaveBeenCalledWith(annexe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexe = { id: 'ABC' };
        spyOn(annexeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(annexeService.update).toHaveBeenCalledWith(annexe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProduitById', () => {
        it('Should return tracked Produit primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackProduitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
