import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDossier, Dossier } from '../dossier.model';

import { DossierService } from './dossier.service';

describe('Service Tests', () => {
  describe('Dossier Service', () => {
    let service: DossierService;
    let httpMock: HttpTestingController;
    let elemDefault: IDossier;
    let expectedResult: IDossier | IDossier[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DossierService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        numeroDossier: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Dossier', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Dossier()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dossier', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            numeroDossier: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Dossier', () => {
        const patchObject = Object.assign(
          {
            numeroDossier: 'BBBBBB',
          },
          new Dossier()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dossier', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            numeroDossier: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Dossier', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDossierToCollectionIfMissing', () => {
        it('should add a Dossier to an empty array', () => {
          const dossier: IDossier = { id: 'ABC' };
          expectedResult = service.addDossierToCollectionIfMissing([], dossier);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dossier);
        });

        it('should not add a Dossier to an array that contains it', () => {
          const dossier: IDossier = { id: 'ABC' };
          const dossierCollection: IDossier[] = [
            {
              ...dossier,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addDossierToCollectionIfMissing(dossierCollection, dossier);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Dossier to an array that doesn't contain it", () => {
          const dossier: IDossier = { id: 'ABC' };
          const dossierCollection: IDossier[] = [{ id: 'CBA' }];
          expectedResult = service.addDossierToCollectionIfMissing(dossierCollection, dossier);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dossier);
        });

        it('should add only unique Dossier to an array', () => {
          const dossierArray: IDossier[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Unbranded Dollar' }];
          const dossierCollection: IDossier[] = [{ id: 'ABC' }];
          expectedResult = service.addDossierToCollectionIfMissing(dossierCollection, ...dossierArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dossier: IDossier = { id: 'ABC' };
          const dossier2: IDossier = { id: 'CBA' };
          expectedResult = service.addDossierToCollectionIfMissing([], dossier, dossier2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dossier);
          expect(expectedResult).toContain(dossier2);
        });

        it('should accept null and undefined values', () => {
          const dossier: IDossier = { id: 'ABC' };
          expectedResult = service.addDossierToCollectionIfMissing([], null, dossier, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dossier);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
