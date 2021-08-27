jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AttributOutService } from '../service/attribut-out.service';

import { AttributOutDeleteDialogComponent } from './attribut-out-delete-dialog.component';

describe('Component Tests', () => {
  describe('AttributOut Management Delete Component', () => {
    let comp: AttributOutDeleteDialogComponent;
    let fixture: ComponentFixture<AttributOutDeleteDialogComponent>;
    let service: AttributOutService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributOutDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AttributOutDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttributOutDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AttributOutService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('ABC');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('ABC');
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
