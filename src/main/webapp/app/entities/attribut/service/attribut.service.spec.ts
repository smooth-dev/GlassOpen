import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAttribut, Attribut } from '../attribut.model';

import { AttributService } from './attribut.service';

describe('Service Tests', () => {
  describe('Attribut Service', () => {
    let service: AttributService;
    let httpMock: HttpTestingController;
    let elemDefault: IAttribut;
    let expectedResult: IAttribut | IAttribut[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AttributService);
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

      it('should create a Attribut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Attribut()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Attribut', () => {
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

      it('should partial update a Attribut', () => {
        const patchObject = Object.assign(
          {
            nomAttribut: 'BBBBBB',
          },
          new Attribut()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Attribut', () => {
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

      it('should delete a Attribut', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAttributToCollectionIfMissing', () => {
        it('should add a Attribut to an empty array', () => {
          const attribut: IAttribut = { id: 'ABC' };
          expectedResult = service.addAttributToCollectionIfMissing([], attribut);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attribut);
        });

        it('should not add a Attribut to an array that contains it', () => {
          const attribut: IAttribut = { id: 'ABC' };
          const attributCollection: IAttribut[] = [
            {
              ...attribut,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAttributToCollectionIfMissing(attributCollection, attribut);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Attribut to an array that doesn't contain it", () => {
          const attribut: IAttribut = { id: 'ABC' };
          const attributCollection: IAttribut[] = [{ id: 'CBA' }];
          expectedResult = service.addAttributToCollectionIfMissing(attributCollection, attribut);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attribut);
        });

        it('should add only unique Attribut to an array', () => {
          const attributArray: IAttribut[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Stagiaire Ghana' }];
          const attributCollection: IAttribut[] = [{ id: 'ABC' }];
          expectedResult = service.addAttributToCollectionIfMissing(attributCollection, ...attributArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const attribut: IAttribut = { id: 'ABC' };
          const attribut2: IAttribut = { id: 'CBA' };
          expectedResult = service.addAttributToCollectionIfMissing([], attribut, attribut2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attribut);
          expect(expectedResult).toContain(attribut2);
        });

        it('should accept null and undefined values', () => {
          const attribut: IAttribut = { id: 'ABC' };
          expectedResult = service.addAttributToCollectionIfMissing([], null, attribut, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attribut);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
