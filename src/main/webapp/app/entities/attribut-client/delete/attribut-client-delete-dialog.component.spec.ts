jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AttributClientService } from '../service/attribut-client.service';

import { AttributClientDeleteDialogComponent } from './attribut-client-delete-dialog.component';

describe('Component Tests', () => {
  describe('AttributClient Management Delete Component', () => {
    let comp: AttributClientDeleteDialogComponent;
    let fixture: ComponentFixture<AttributClientDeleteDialogComponent>;
    let service: AttributClientService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributClientDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AttributClientDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttributClientDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AttributClientService);
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
