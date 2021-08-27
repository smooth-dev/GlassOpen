import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAnnexeOut, AnnexeOut } from '../annexe-out.model';

import { AnnexeOutService } from './annexe-out.service';

describe('Service Tests', () => {
  describe('AnnexeOut Service', () => {
    let service: AnnexeOutService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnnexeOut;
    let expectedResult: IAnnexeOut | IAnnexeOut[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnnexeOutService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        nomAnnexe: 'AAAAAAA',
        idClient: 'AAAAAAA',
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

      it('should create a AnnexeOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AnnexeOut()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AnnexeOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAnnexe: 'BBBBBB',
            idClient: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AnnexeOut', () => {
        const patchObject = Object.assign(
          {
            idClient: 'BBBBBB',
          },
          new AnnexeOut()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AnnexeOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAnnexe: 'BBBBBB',
            idClient: 'BBBBBB',
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

      it('should delete a AnnexeOut', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnnexeOutToCollectionIfMissing', () => {
        it('should add a AnnexeOut to an empty array', () => {
          const annexeOut: IAnnexeOut = { id: 'ABC' };
          expectedResult = service.addAnnexeOutToCollectionIfMissing([], annexeOut);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(annexeOut);
        });

        it('should not add a AnnexeOut to an array that contains it', () => {
          const annexeOut: IAnnexeOut = { id: 'ABC' };
          const annexeOutCollection: IAnnexeOut[] = [
            {
              ...annexeOut,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAnnexeOutToCollectionIfMissing(annexeOutCollection, annexeOut);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AnnexeOut to an array that doesn't contain it", () => {
          const annexeOut: IAnnexeOut = { id: 'ABC' };
          const annexeOutCollection: IAnnexeOut[] = [{ id: 'CBA' }];
          expectedResult = service.addAnnexeOutToCollectionIfMissing(annexeOutCollection, annexeOut);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(annexeOut);
        });

        it('should add only unique AnnexeOut to an array', () => {
          const annexeOutArray: IAnnexeOut[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'infomediaries' }];
          const annexeOutCollection: IAnnexeOut[] = [{ id: 'ABC' }];
          expectedResult = service.addAnnexeOutToCollectionIfMissing(annexeOutCollection, ...annexeOutArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const annexeOut: IAnnexeOut = { id: 'ABC' };
          const annexeOut2: IAnnexeOut = { id: 'CBA' };
          expectedResult = service.addAnnexeOutToCollectionIfMissing([], annexeOut, annexeOut2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(annexeOut);
          expect(expectedResult).toContain(annexeOut2);
        });

        it('should accept null and undefined values', () => {
          const annexeOut: IAnnexeOut = { id: 'ABC' };
          expectedResult = service.addAnnexeOutToCollectionIfMissing([], null, annexeOut, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(annexeOut);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
