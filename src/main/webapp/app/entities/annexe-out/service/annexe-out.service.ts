import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnexeOut, getAnnexeOutIdentifier } from '../annexe-out.model';

export type EntityResponseType = HttpResponse<IAnnexeOut>;
export type EntityArrayResponseType = HttpResponse<IAnnexeOut[]>;

@Injectable({ providedIn: 'root' })
export class AnnexeOutService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/annexe-outs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(annexeOut: IAnnexeOut): Observable<EntityResponseType> {
    return this.http.post<IAnnexeOut>(this.resourceUrl, annexeOut, { observe: 'response' });
  }

  update(annexeOut: IAnnexeOut): Observable<EntityResponseType> {
    return this.http.put<IAnnexeOut>(`${this.resourceUrl}/${getAnnexeOutIdentifier(annexeOut) as string}`, annexeOut, {
      observe: 'response',
    });
  }

  partialUpdate(annexeOut: IAnnexeOut): Observable<EntityResponseType> {
    return this.http.patch<IAnnexeOut>(`${this.resourceUrl}/${getAnnexeOutIdentifier(annexeOut) as string}`, annexeOut, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAnnexeOut>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnnexeOut[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAnnexeOutToCollectionIfMissing(
    annexeOutCollection: IAnnexeOut[],
    ...annexeOutsToCheck: (IAnnexeOut | null | undefined)[]
  ): IAnnexeOut[] {
    const annexeOuts: IAnnexeOut[] = annexeOutsToCheck.filter(isPresent);
    if (annexeOuts.length > 0) {
      const annexeOutCollectionIdentifiers = annexeOutCollection.map(annexeOutItem => getAnnexeOutIdentifier(annexeOutItem)!);
      const annexeOutsToAdd = annexeOuts.filter(annexeOutItem => {
        const annexeOutIdentifier = getAnnexeOutIdentifier(annexeOutItem);
        if (annexeOutIdentifier == null || annexeOutCollectionIdentifiers.includes(annexeOutIdentifier)) {
          return false;
        }
        annexeOutCollectionIdentifiers.push(annexeOutIdentifier);
        return true;
      });
      return [...annexeOutsToAdd, ...annexeOutCollection];
    }
    return annexeOutCollection;
  }
}
