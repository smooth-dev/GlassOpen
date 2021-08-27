import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAttributOut, AttributOut } from '../attribut-out.model';

import { AttributOutService } from './attribut-out.service';

describe('Service Tests', () => {
  describe('AttributOut Service', () => {
    let service: AttributOutService;
    let httpMock: HttpTestingController;
    let elemDefault: IAttributOut;
    let expectedResult: IAttributOut | IAttributOut[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AttributOutService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        nomAttribut: 'AAAAAAA',
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

      it('should create a AttributOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AttributOut()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AttributOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAttribut: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AttributOut', () => {
        const patchObject = Object.assign({}, new AttributOut());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AttributOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAttribut: 'BBBBBB',
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

      it('should delete a AttributOut', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAttributOutToCollectionIfMissing', () => {
        it('should add a AttributOut to an empty array', () => {
          const attributOut: IAttributOut = { id: 'ABC' };
          expectedResult = service.addAttributOutToCollectionIfMissing([], attributOut);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attributOut);
        });

        it('should not add a AttributOut to an array that contains it', () => {
          const attributOut: IAttributOut = { id: 'ABC' };
          const attributOutCollection: IAttributOut[] = [
            {
              ...attributOut,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAttributOutToCollectionIfMissing(attributOutCollection, attributOut);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AttributOut to an array that doesn't contain it", () => {
          const attributOut: IAttributOut = { id: 'ABC' };
          const attributOutCollection: IAttributOut[] = [{ id: 'CBA' }];
          expectedResult = service.addAttributOutToCollectionIfMissing(attributOutCollection, attributOut);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attributOut);
        });

        it('should add only unique AttributOut to an array', () => {
          const attributOutArray: IAttributOut[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Grocery withdrawal' }];
          const attributOutCollection: IAttributOut[] = [{ id: 'ABC' }];
          expectedResult = service.addAttributOutToCollectionIfMissing(attributOutCollection, ...attributOutArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const attributOut: IAttributOut = { id: 'ABC' };
          const attributOut2: IAttributOut = { id: 'CBA' };
          expectedResult = service.addAttributOutToCollectionIfMissing([], attributOut, attributOut2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attributOut);
          expect(expectedResult).toContain(attributOut2);
        });

        it('should accept null and undefined values', () => {
          const attributOut: IAttributOut = { id: 'ABC' };
          expectedResult = service.addAttributOutToCollectionIfMissing([], null, attributOut, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attributOut);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
