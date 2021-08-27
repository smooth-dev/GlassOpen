import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAnnexe, Annexe } from '../annexe.model';

import { AnnexeService } from './annexe.service';

describe('Service Tests', () => {
  describe('Annexe Service', () => {
    let service: AnnexeService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnnexe;
    let expectedResult: IAnnexe | IAnnexe[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnnexeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        nomAnnexe: 'AAAAAAA',
        enteteAnnexe: 'AAAAAAA',
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

      it('should create a Annexe', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Annexe()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Annexe', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAnnexe: 'BBBBBB',
            enteteAnnexe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Annexe', () => {
        const patchObject = Object.assign(
          {
            enteteAnnexe: 'BBBBBB',
          },
          new Annexe()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Annexe', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAnnexe: 'BBBBBB',
            enteteAnnexe: 'BBBBBB',
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

      it('should delete a Annexe', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnnexeToCollectionIfMissing', () => {
        it('should add a Annexe to an empty array', () => {
          const annexe: IAnnexe = { id: 'ABC' };
          expectedResult = service.addAnnexeToCollectionIfMissing([], annexe);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(annexe);
        });

        it('should not add a Annexe to an array that contains it', () => {
          const annexe: IAnnexe = { id: 'ABC' };
          const annexeCollection: IAnnexe[] = [
            {
              ...annexe,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAnnexeToCollectionIfMissing(annexeCollection, annexe);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Annexe to an array that doesn't contain it", () => {
          const annexe: IAnnexe = { id: 'ABC' };
          const annexeCollection: IAnnexe[] = [{ id: 'CBA' }];
          expectedResult = service.addAnnexeToCollectionIfMissing(annexeCollection, annexe);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(annexe);
        });

        it('should add only unique Annexe to an array', () => {
          const annexeArray: IAnnexe[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Specialiste Sports' }];
          const annexeCollection: IAnnexe[] = [{ id: 'ABC' }];
          expectedResult = service.addAnnexeToCollectionIfMissing(annexeCollection, ...annexeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const annexe: IAnnexe = { id: 'ABC' };
          const annexe2: IAnnexe = { id: 'CBA' };
          expectedResult = service.addAnnexeToCollectionIfMissing([], annexe, annexe2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(annexe);
          expect(expectedResult).toContain(annexe2);
        });

        it('should accept null and undefined values', () => {
          const annexe: IAnnexe = { id: 'ABC' };
          expectedResult = service.addAnnexeToCollectionIfMissing([], null, annexe, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(annexe);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
