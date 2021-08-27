jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AttributService } from '../service/attribut.service';
import { IAttribut, Attribut } from '../attribut.model';
import { IAnnexe } from 'app/entities/annexe/annexe.model';
import { AnnexeService } from 'app/entities/annexe/service/annexe.service';

import { AttributUpdateComponent } from './attribut-update.component';

describe('Component Tests', () => {
  describe('Attribut Management Update Component', () => {
    let comp: AttributUpdateComponent;
    let fixture: ComponentFixture<AttributUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let attributService: AttributService;
    let annexeService: AnnexeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AttributUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      attributService = TestBed.inject(AttributService);
      annexeService = TestBed.inject(AnnexeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Annexe query and add missing value', () => {
        const attribut: IAttribut = { id: 'CBA' };
        const annexe: IAnnexe = { id: 'Soft Practical' };
        attribut.annexe = annexe;

        const annexeCollection: IAnnexe[] = [{ id: 'interfaces Agent' }];
        spyOn(annexeService, 'query').and.returnValue(of(new HttpResponse({ body: annexeCollection })));
        const additionalAnnexes = [annexe];
        const expectedCollection: IAnnexe[] = [...additionalAnnexes, ...annexeCollection];
        spyOn(annexeService, 'addAnnexeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ attribut });
        comp.ngOnInit();

        expect(annexeService.query).toHaveBeenCalled();
        expect(annexeService.addAnnexeToCollectionIfMissing).toHaveBeenCalledWith(annexeCollection, ...additionalAnnexes);
        expect(comp.annexesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const attribut: IAttribut = { id: 'CBA' };
        const annexe: IAnnexe = { id: 'Metal la' };
        attribut.annexe = annexe;

        activatedRoute.data = of({ attribut });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(attribut));
        expect(comp.annexesSharedCollection).toContain(annexe);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attribut = { id: 'ABC' };
        spyOn(attributService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attribut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attribut }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(attributService.update).toHaveBeenCalledWith(attribut);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attribut = new Attribut();
        spyOn(attributService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attribut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attribut }));
        saveSubject.complete();

        // THEN
        expect(attributService.create).toHaveBeenCalledWith(attribut);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attribut = { id: 'ABC' };
        spyOn(attributService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attribut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(attributService.update).toHaveBeenCalledWith(attribut);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAnnexeById', () => {
        it('Should return tracked Annexe primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackAnnexeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
