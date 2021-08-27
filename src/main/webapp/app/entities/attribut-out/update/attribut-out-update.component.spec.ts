jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AttributOutService } from '../service/attribut-out.service';
import { IAttributOut, AttributOut } from '../attribut-out.model';

import { AttributOutUpdateComponent } from './attribut-out-update.component';

describe('Component Tests', () => {
  describe('AttributOut Management Update Component', () => {
    let comp: AttributOutUpdateComponent;
    let fixture: ComponentFixture<AttributOutUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let attributOutService: AttributOutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributOutUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AttributOutUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributOutUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      attributOutService = TestBed.inject(AttributOutService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const attributOut: IAttributOut = { id: 'CBA' };

        activatedRoute.data = of({ attributOut });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(attributOut));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributOut = { id: 'ABC' };
        spyOn(attributOutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attributOut }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(attributOutService.update).toHaveBeenCalledWith(attributOut);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributOut = new AttributOut();
        spyOn(attributOutService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attributOut }));
        saveSubject.complete();

        // THEN
        expect(attributOutService.create).toHaveBeenCalledWith(attributOut);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributOut = { id: 'ABC' };
        spyOn(attributOutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributOut });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(attributOutService.update).toHaveBeenCalledWith(attributOut);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
