import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAnnexeGroup, AnnexeGroup } from '../annexe-group.model';

import { AnnexeGroupService } from './annexe-group.service';

describe('Service Tests', () => {
  describe('AnnexeGroup Service', () => {
    let service: AnnexeGroupService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnnexeGroup;
    let expectedResult: IAnnexeGroup | IAnnexeGroup[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnnexeGroupService);
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

      it('should create a AnnexeGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AnnexeGroup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AnnexeGroup', () => {
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

      it('should partial update a AnnexeGroup', () => {
        const patchObject = Object.assign(
          {
            nomAnnexe: 'BBBBBB',
          },
          new AnnexeGroup()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AnnexeGroup', () => {
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

      it('should delete a AnnexeGroup', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnnexeGroupToCollectionIfMissing', () => {
        it('should add a AnnexeGroup to an empty array', () => {
          const annexeGroup: IAnnexeGroup = { id: 'ABC' };
          expectedResult = service.addAnnexeGroupToCollectionIfMissing([], annexeGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(annexeGroup);
        });

        it('should not add a AnnexeGroup to an array that contains it', () => {
          const annexeGroup: IAnnexeGroup = { id: 'ABC' };
          const annexeGroupCollection: IAnnexeGroup[] = [
            {
              ...annexeGroup,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAnnexeGroupToCollectionIfMissing(annexeGroupCollection, annexeGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AnnexeGroup to an array that doesn't contain it", () => {
          const annexeGroup: IAnnexeGroup = { id: 'ABC' };
          const annexeGroupCollection: IAnnexeGroup[] = [{ id: 'CBA' }];
          expectedResult = service.addAnnexeGroupToCollectionIfMissing(annexeGroupCollection, annexeGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(annexeGroup);
        });

        it('should add only unique AnnexeGroup to an array', () => {
          const annexeGroupArray: IAnnexeGroup[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'strategic' }];
          const annexeGroupCollection: IAnnexeGroup[] = [{ id: 'ABC' }];
          expectedResult = service.addAnnexeGroupToCollectionIfMissing(annexeGroupCollection, ...annexeGroupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const annexeGroup: IAnnexeGroup = { id: 'ABC' };
          const annexeGroup2: IAnnexeGroup = { id: 'CBA' };
          expectedResult = service.addAnnexeGroupToCollectionIfMissing([], annexeGroup, annexeGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(annexeGroup);
          expect(expectedResult).toContain(annexeGroup2);
        });

        it('should accept null and undefined values', () => {
          const annexeGroup: IAnnexeGroup = { id: 'ABC' };
          expectedResult = service.addAnnexeGroupToCollectionIfMissing([], null, annexeGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(annexeGroup);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
