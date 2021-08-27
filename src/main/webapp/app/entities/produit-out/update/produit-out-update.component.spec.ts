jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProduitOutService } from '../service/produit-out.service';
import { IProduitOut, ProduitOut } from '../produit-out.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

import { ProduitOutUpdateComponent } from './produit-out-update.component';

describe('Component Tests', () => {
  describe('ProduitOut Management Update Component', () => {
    let comp: ProduitOutUpdateComponent;
    let fixture: ComponentFixture<ProduitOutUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let produitOutService: ProduitOutService;
    let produitService: ProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProduitOutUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProduitOutUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProduitOutUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      produitOutService = TestBed.inject(ProduitOutService);
      produitService = TestBed.inject(ProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Produit query and add missing value', () => {
        const produitOut: IProduitOut = { id: 'CBA' };
        const produit: IProduit = { id: 'Movies users' };
        produitOut.produit = produit;

        const produitCollection: IProduit[] = [{ id: 'auxiliary Loan c' }];
        spyOn(produitService, 'query').and.returnValue(of(new HttpResponse({ body: produitCollection })));
        const additionalProduits = [produit];
        const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
        spyOn(produitService, 'addProduitToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ produitOut });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitCollection, ...additionalProduits);
        expect(comp.produitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const produitOut: IProduitOut = { id: 'CBA' };
        const produit: IProduit = { id: 'Bourgogne' };
        produitOut.produit = produit;

        activatedRoute.data = of({ produitOut });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(produitOut));
        expect(comp.produitsSharedCollection).toContain(produit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const produitOut = { id: 'ABC' };
        spyOn(produitOutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ produitOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: produitOut }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(produitOutService.update).toHaveBeenCalledWith(produitOut);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const produitOut = new ProduitOut();
        spyOn(produitOutService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ produitOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: produitOut }));
        saveSubject.complete();

        // THEN
        expect(produitOutService.create).toHaveBeenCalledWith(produitOut);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const produitOut = { id: 'ABC' };
        spyOn(produitOutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ produitOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(produitOutService.update).toHaveBeenCalledWith(produitOut);
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
