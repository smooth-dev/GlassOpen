jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnnexeGroupService } from '../service/annexe-group.service';
import { IAnnexeGroup, AnnexeGroup } from '../annexe-group.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

import { AnnexeGroupUpdateComponent } from './annexe-group-update.component';

describe('Component Tests', () => {
  describe('AnnexeGroup Management Update Component', () => {
    let comp: AnnexeGroupUpdateComponent;
    let fixture: ComponentFixture<AnnexeGroupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let annexeGroupService: AnnexeGroupService;
    let produitService: ProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnexeGroupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnnexeGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnnexeGroupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      annexeGroupService = TestBed.inject(AnnexeGroupService);
      produitService = TestBed.inject(ProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Produit query and add missing value', () => {
        const annexeGroup: IAnnexeGroup = { id: 'CBA' };
        const produit: IProduit = { id: 'morph' };
        annexeGroup.produit = produit;

        const produitCollection: IProduit[] = [{ id: 'calculate Chili' }];
        spyOn(produitService, 'query').and.returnValue(of(new HttpResponse({ body: produitCollection })));
        const additionalProduits = [produit];
        const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
        spyOn(produitService, 'addProduitToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ annexeGroup });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitCollection, ...additionalProduits);
        expect(comp.produitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const annexeGroup: IAnnexeGroup = { id: 'CBA' };
        const produit: IProduit = { id: 'Sausages Table' };
        annexeGroup.produit = produit;

        activatedRoute.data = of({ annexeGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(annexeGroup));
        expect(comp.produitsSharedCollection).toContain(produit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexeGroup = { id: 'ABC' };
        spyOn(annexeGroupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexeGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annexeGroup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(annexeGroupService.update).toHaveBeenCalledWith(annexeGroup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexeGroup = new AnnexeGroup();
        spyOn(annexeGroupService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexeGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annexeGroup }));
        saveSubject.complete();

        // THEN
        expect(annexeGroupService.create).toHaveBeenCalledWith(annexeGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexeGroup = { id: 'ABC' };
        spyOn(annexeGroupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexeGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(annexeGroupService.update).toHaveBeenCalledWith(annexeGroup);
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
