import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAttributClient, AttributClient } from '../attribut-client.model';

import { AttributClientService } from './attribut-client.service';

describe('Service Tests', () => {
  describe('AttributClient Service', () => {
    let service: AttributClientService;
    let httpMock: HttpTestingController;
    let elemDefault: IAttributClient;
    let expectedResult: IAttributClient | IAttributClient[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AttributClientService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        nomAttribut: 'AAAAAAA',
        contenu: 'AAAAAAA',
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

      it('should create a AttributClient', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AttributClient()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AttributClient', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAttribut: 'BBBBBB',
            contenu: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AttributClient', () => {
        const patchObject = Object.assign(
          {
            contenu: 'BBBBBB',
          },
          new AttributClient()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AttributClient', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAttribut: 'BBBBBB',
            contenu: 'BBBBBB',
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

      it('should delete a AttributClient', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAttributClientToCollectionIfMissing', () => {
        it('should add a AttributClient to an empty array', () => {
          const attributClient: IAttributClient = { id: 'ABC' };
          expectedResult = service.addAttributClientToCollectionIfMissing([], attributClient);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attributClient);
        });

        it('should not add a AttributClient to an array that contains it', () => {
          const attributClient: IAttributClient = { id: 'ABC' };
          const attributClientCollection: IAttributClient[] = [
            {
              ...attributClient,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAttributClientToCollectionIfMissing(attributClientCollection, attributClient);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AttributClient to an array that doesn't contain it", () => {
          const attributClient: IAttributClient = { id: 'ABC' };
          const attributClientCollection: IAttributClient[] = [{ id: 'CBA' }];
          expectedResult = service.addAttributClientToCollectionIfMissing(attributClientCollection, attributClient);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attributClient);
        });

        it('should add only unique AttributClient to an array', () => {
          const attributClientArray: IAttributClient[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'AGP overriding' }];
          const attributClientCollection: IAttributClient[] = [{ id: 'ABC' }];
          expectedResult = service.addAttributClientToCollectionIfMissing(attributClientCollection, ...attributClientArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const attributClient: IAttributClient = { id: 'ABC' };
          const attributClient2: IAttributClient = { id: 'CBA' };
          expectedResult = service.addAttributClientToCollectionIfMissing([], attributClient, attributClient2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attributClient);
          expect(expectedResult).toContain(attributClient2);
        });

        it('should accept null and undefined values', () => {
          const attributClient: IAttributClient = { id: 'ABC' };
          expectedResult = service.addAttributClientToCollectionIfMissing([], null, attributClient, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attributClient);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
