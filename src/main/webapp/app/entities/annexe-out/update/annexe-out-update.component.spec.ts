jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnnexeOutService } from '../service/annexe-out.service';
import { IAnnexeOut, AnnexeOut } from '../annexe-out.model';
import { IAnnexeGroup } from 'app/entities/annexe-group/annexe-group.model';
import { AnnexeGroupService } from 'app/entities/annexe-group/service/annexe-group.service';

import { AnnexeOutUpdateComponent } from './annexe-out-update.component';

describe('Component Tests', () => {
  describe('AnnexeOut Management Update Component', () => {
    let comp: AnnexeOutUpdateComponent;
    let fixture: ComponentFixture<AnnexeOutUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let annexeOutService: AnnexeOutService;
    let annexeGroupService: AnnexeGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnexeOutUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnnexeOutUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnnexeOutUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      annexeOutService = TestBed.inject(AnnexeOutService);
      annexeGroupService = TestBed.inject(AnnexeGroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AnnexeGroup query and add missing value', () => {
        const annexeOut: IAnnexeOut = { id: 'CBA' };
        const annexegroup: IAnnexeGroup = { id: 'Borders collaboration' };
        annexeOut.annexegroup = annexegroup;

        const annexeGroupCollection: IAnnexeGroup[] = [{ id: 'a Saint-Jacques' }];
        spyOn(annexeGroupService, 'query').and.returnValue(of(new HttpResponse({ body: annexeGroupCollection })));
        const additionalAnnexeGroups = [annexegroup];
        const expectedCollection: IAnnexeGroup[] = [...additionalAnnexeGroups, ...annexeGroupCollection];
        spyOn(annexeGroupService, 'addAnnexeGroupToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ annexeOut });
        comp.ngOnInit();

        expect(annexeGroupService.query).toHaveBeenCalled();
        expect(annexeGroupService.addAnnexeGroupToCollectionIfMissing).toHaveBeenCalledWith(
          annexeGroupCollection,
          ...additionalAnnexeGroups
        );
        expect(comp.annexeGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const annexeOut: IAnnexeOut = { id: 'CBA' };
        const annexegroup: IAnnexeGroup = { id: 'AI c Garden' };
        annexeOut.annexegroup = annexegroup;

        activatedRoute.data = of({ annexeOut });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(annexeOut));
        expect(comp.annexeGroupsSharedCollection).toContain(annexegroup);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexeOut = { id: 'ABC' };
        spyOn(annexeOutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexeOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annexeOut }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(annexeOutService.update).toHaveBeenCalledWith(annexeOut);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexeOut = new AnnexeOut();
        spyOn(annexeOutService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexeOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annexeOut }));
        saveSubject.complete();

        // THEN
        expect(annexeOutService.create).toHaveBeenCalledWith(annexeOut);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const annexeOut = { id: 'ABC' };
        spyOn(annexeOutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ annexeOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(annexeOutService.update).toHaveBeenCalledWith(annexeOut);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAnnexeGroupById', () => {
        it('Should return tracked AnnexeGroup primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackAnnexeGroupById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
