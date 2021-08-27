jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AttributClientService } from '../service/attribut-client.service';
import { IAttributClient, AttributClient } from '../attribut-client.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IAnnexeOut } from 'app/entities/annexe-out/annexe-out.model';
import { AnnexeOutService } from 'app/entities/annexe-out/service/annexe-out.service';

import { AttributClientUpdateComponent } from './attribut-client-update.component';

describe('Component Tests', () => {
  describe('AttributClient Management Update Component', () => {
    let comp: AttributClientUpdateComponent;
    let fixture: ComponentFixture<AttributClientUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let attributClientService: AttributClientService;
    let clientService: ClientService;
    let annexeOutService: AnnexeOutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributClientUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AttributClientUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributClientUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      attributClientService = TestBed.inject(AttributClientService);
      clientService = TestBed.inject(ClientService);
      annexeOutService = TestBed.inject(AnnexeOutService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const attributClient: IAttributClient = { id: 'CBA' };
        const client: IClient = { id: 'a interfaces' };
        attributClient.client = client;

        const clientCollection: IClient[] = [{ id: 'Handcrafted iterate PNG' }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ attributClient });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AnnexeOut query and add missing value', () => {
        const attributClient: IAttributClient = { id: 'CBA' };
        const annexeout: IAnnexeOut = { id: 'payment 6th JBOD' };
        attributClient.annexeout = annexeout;

        const annexeOutCollection: IAnnexeOut[] = [{ id: 'SAS web monetize' }];
        spyOn(annexeOutService, 'query').and.returnValue(of(new HttpResponse({ body: annexeOutCollection })));
        const additionalAnnexeOuts = [annexeout];
        const expectedCollection: IAnnexeOut[] = [...additionalAnnexeOuts, ...annexeOutCollection];
        spyOn(annexeOutService, 'addAnnexeOutToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ attributClient });
        comp.ngOnInit();

        expect(annexeOutService.query).toHaveBeenCalled();
        expect(annexeOutService.addAnnexeOutToCollectionIfMissing).toHaveBeenCalledWith(annexeOutCollection, ...additionalAnnexeOuts);
        expect(comp.annexeOutsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const attributClient: IAttributClient = { id: 'CBA' };
        const client: IClient = { id: 'Account' };
        attributClient.client = client;
        const annexeout: IAnnexeOut = { id: 'b' };
        attributClient.annexeout = annexeout;

        activatedRoute.data = of({ attributClient });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(attributClient));
        expect(comp.clientsSharedCollection).toContain(client);
        expect(comp.annexeOutsSharedCollection).toContain(annexeout);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributClient = { id: 'ABC' };
        spyOn(attributClientService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributClient });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attributClient }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(attributClientService.update).toHaveBeenCalledWith(attributClient);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributClient = new AttributClient();
        spyOn(attributClientService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributClient });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attributClient }));
        saveSubject.complete();

        // THEN
        expect(attributClientService.create).toHaveBeenCalledWith(attributClient);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributClient = { id: 'ABC' };
        spyOn(attributClientService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributClient });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(attributClientService.update).toHaveBeenCalledWith(attributClient);
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

      describe('trackAnnexeOutById', () => {
        it('Should return tracked AnnexeOut primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackAnnexeOutById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
