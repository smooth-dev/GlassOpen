jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ProduitOutService } from '../service/produit-out.service';

import { ProduitOutDeleteDialogComponent } from './produit-out-delete-dialog.component';

describe('Component Tests', () => {
  describe('ProduitOut Management Delete Component', () => {
    let comp: ProduitOutDeleteDialogComponent;
    let fixture: ComponentFixture<ProduitOutDeleteDialogComponent>;
    let service: ProduitOutService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProduitOutDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ProduitOutDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProduitOutDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProduitOutService);
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
