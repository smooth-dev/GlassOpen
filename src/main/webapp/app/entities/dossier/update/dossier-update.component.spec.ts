jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DossierService } from '../service/dossier.service';
import { IDossier, Dossier } from '../dossier.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

import { DossierUpdateComponent } from './dossier-update.component';

describe('Component Tests', () => {
  describe('Dossier Management Update Component', () => {
    let comp: DossierUpdateComponent;
    let fixture: ComponentFixture<DossierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dossierService: DossierService;
    let clientService: ClientService;
    let produitService: ProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DossierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DossierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DossierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dossierService = TestBed.inject(DossierService);
      clientService = TestBed.inject(ClientService);
      produitService = TestBed.inject(ProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const dossier: IDossier = { id: 'CBA' };
        const client: IClient = { id: 'architecture multi-byte Administrateur' };
        dossier.client = client;

        const clientCollection: IClient[] = [{ id: 'paradigm' }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ dossier });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Produit query and add missing value', () => {
        const dossier: IDossier = { id: 'CBA' };
        const produit: IProduit = { id: 'Marks' };
        dossier.produit = produit;

        const produitCollection: IProduit[] = [{ id: 'circuit Shoes' }];
        spyOn(produitService, 'query').and.returnValue(of(new HttpResponse({ body: produitCollection })));
        const additionalProduits = [produit];
        const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
        spyOn(produitService, 'addProduitToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ dossier });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitCollection, ...additionalProduits);
        expect(comp.produitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dossier: IDossier = { id: 'CBA' };
        const client: IClient = { id: 'foreground XSS engage' };
        dossier.client = client;
        const produit: IProduit = { id: 'frictionless Tools Gambie' };
        dossier.produit = produit;

        activatedRoute.data = of({ dossier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dossier));
        expect(comp.clientsSharedCollection).toContain(client);
        expect(comp.produitsSharedCollection).toContain(produit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dossier = { id: 'ABC' };
        spyOn(dossierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dossier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dossier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dossierService.update).toHaveBeenCalledWith(dossier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dossier = new Dossier();
        spyOn(dossierService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dossier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dossier }));
        saveSubject.complete();

        // THEN
        expect(dossierService.create).toHaveBeenCalledWith(dossier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dossier = { id: 'ABC' };
        spyOn(dossierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dossier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dossierService.update).toHaveBeenCalledWith(dossier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
