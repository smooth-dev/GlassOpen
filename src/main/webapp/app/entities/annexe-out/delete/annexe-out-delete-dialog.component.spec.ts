jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AnnexeOutService } from '../service/annexe-out.service';

import { AnnexeOutDeleteDialogComponent } from './annexe-out-delete-dialog.component';

describe('Component Tests', () => {
  describe('AnnexeOut Management Delete Component', () => {
    let comp: AnnexeOutDeleteDialogComponent;
    let fixture: ComponentFixture<AnnexeOutDeleteDialogComponent>;
    let service: AnnexeOutService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnexeOutDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AnnexeOutDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnnexeOutDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AnnexeOutService);
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
