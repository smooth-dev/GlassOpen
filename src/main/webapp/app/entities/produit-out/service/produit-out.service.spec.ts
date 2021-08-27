import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProduitOut, ProduitOut } from '../produit-out.model';

import { ProduitOutService } from './produit-out.service';

describe('Service Tests', () => {
  describe('ProduitOut Service', () => {
    let service: ProduitOutService;
    let httpMock: HttpTestingController;
    let elemDefault: IProduitOut;
    let expectedResult: IProduitOut | IProduitOut[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProduitOutService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        nomAnnexe: 'AAAAAAA',
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

      it('should create a ProduitOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ProduitOut()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ProduitOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAnnexe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ProduitOut', () => {
        const patchObject = Object.assign(
          {
            nomAnnexe: 'BBBBBB',
          },
          new ProduitOut()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ProduitOut', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            nomAnnexe: 'BBBBBB',
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

      it('should delete a ProduitOut', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProduitOutToCollectionIfMissing', () => {
        it('should add a ProduitOut to an empty array', () => {
          const produitOut: IProduitOut = { id: 'ABC' };
          expectedResult = service.addProduitOutToCollectionIfMissing([], produitOut);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(produitOut);
        });

        it('should not add a ProduitOut to an array that contains it', () => {
          const produitOut: IProduitOut = { id: 'ABC' };
          const produitOutCollection: IProduitOut[] = [
            {
              ...produitOut,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addProduitOutToCollectionIfMissing(produitOutCollection, produitOut);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ProduitOut to an array that doesn't contain it", () => {
          const produitOut: IProduitOut = { id: 'ABC' };
          const produitOutCollection: IProduitOut[] = [{ id: 'CBA' }];
          expectedResult = service.addProduitOutToCollectionIfMissing(produitOutCollection, produitOut);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(produitOut);
        });

        it('should add only unique ProduitOut to an array', () => {
          const produitOutArray: IProduitOut[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Reactive b Savings' }];
          const produitOutCollection: IProduitOut[] = [{ id: 'ABC' }];
          expectedResult = service.addProduitOutToCollectionIfMissing(produitOutCollection, ...produitOutArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const produitOut: IProduitOut = { id: 'ABC' };
          const produitOut2: IProduitOut = { id: 'CBA' };
          expectedResult = service.addProduitOutToCollectionIfMissing([], produitOut, produitOut2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(produitOut);
          expect(expectedResult).toContain(produitOut2);
        });

        it('should accept null and undefined values', () => {
          const produitOut: IProduitOut = { id: 'ABC' };
          expectedResult = service.addProduitOutToCollectionIfMissing([], null, produitOut, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(produitOut);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
