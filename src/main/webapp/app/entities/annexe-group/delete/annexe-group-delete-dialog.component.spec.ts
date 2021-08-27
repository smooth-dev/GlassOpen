jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AnnexeGroupService } from '../service/annexe-group.service';

import { AnnexeGroupDeleteDialogComponent } from './annexe-group-delete-dialog.component';

describe('Component Tests', () => {
  describe('AnnexeGroup Management Delete Component', () => {
    let comp: AnnexeGroupDeleteDialogComponent;
    let fixture: ComponentFixture<AnnexeGroupDeleteDialogComponent>;
    let service: AnnexeGroupService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnexeGroupDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AnnexeGroupDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnnexeGroupDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AnnexeGroupService);
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
