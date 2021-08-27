import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttributOut, getAttributOutIdentifier } from '../attribut-out.model';

export type EntityResponseType = HttpResponse<IAttributOut>;
export type EntityArrayResponseType = HttpResponse<IAttributOut[]>;

@Injectable({ providedIn: 'root' })
export class AttributOutService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attribut-outs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(attributOut: IAttributOut): Observable<EntityResponseType> {
    return this.http.post<IAttributOut>(this.resourceUrl, attributOut, { observe: 'response' });
  }

  update(attributOut: IAttributOut): Observable<EntityResponseType> {
    return this.http.put<IAttributOut>(`${this.resourceUrl}/${getAttributOutIdentifier(attributOut) as string}`, attributOut, {
      observe: 'response',
    });
  }

  partialUpdate(attributOut: IAttributOut): Observable<EntityResponseType> {
    return this.http.patch<IAttributOut>(`${this.resourceUrl}/${getAttributOutIdentifier(attributOut) as string}`, attributOut, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAttributOut>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttributOut[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAttributOutToCollectionIfMissing(
    attributOutCollection: IAttributOut[],
    ...attributOutsToCheck: (IAttributOut | null | undefined)[]
  ): IAttributOut[] {
    const attributOuts: IAttributOut[] = attributOutsToCheck.filter(isPresent);
    if (attributOuts.length > 0) {
      const attributOutCollectionIdentifiers = attributOutCollection.map(attributOutItem => getAttributOutIdentifier(attributOutItem)!);
      const attributOutsToAdd = attributOuts.filter(attributOutItem => {
        const attributOutIdentifier = getAttributOutIdentifier(attributOutItem);
        if (attributOutIdentifier == null || attributOutCollectionIdentifiers.includes(attributOutIdentifier)) {
          return false;
        }
        attributOutCollectionIdentifiers.push(attributOutIdentifier);
        return true;
      });
      return [...attributOutsToAdd, ...attributOutCollection];
    }
    return attributOutCollection;
  }
}
