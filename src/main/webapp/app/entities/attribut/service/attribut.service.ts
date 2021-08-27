import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttribut, getAttributIdentifier } from '../attribut.model';

export type EntityResponseType = HttpResponse<IAttribut>;
export type EntityArrayResponseType = HttpResponse<IAttribut[]>;

@Injectable({ providedIn: 'root' })
export class AttributService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attributs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(attribut: IAttribut): Observable<EntityResponseType> {
    return this.http.post<IAttribut>(this.resourceUrl, attribut, { observe: 'response' });
  }

  update(attribut: IAttribut): Observable<EntityResponseType> {
    return this.http.put<IAttribut>(`${this.resourceUrl}/${getAttributIdentifier(attribut) as string}`, attribut, { observe: 'response' });
  }

  partialUpdate(attribut: IAttribut): Observable<EntityResponseType> {
    return this.http.patch<IAttribut>(`${this.resourceUrl}/${getAttributIdentifier(attribut) as string}`, attribut, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAttribut>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttribut[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAttributToCollectionIfMissing(attributCollection: IAttribut[], ...attributsToCheck: (IAttribut | null | undefined)[]): IAttribut[] {
    const attributs: IAttribut[] = attributsToCheck.filter(isPresent);
    if (attributs.length > 0) {
      const attributCollectionIdentifiers = attributCollection.map(attributItem => getAttributIdentifier(attributItem)!);
      const attributsToAdd = attributs.filter(attributItem => {
        const attributIdentifier = getAttributIdentifier(attributItem);
        if (attributIdentifier == null || attributCollectionIdentifiers.includes(attributIdentifier)) {
          return false;
        }
        attributCollectionIdentifiers.push(attributIdentifier);
        return true;
      });
      return [...attributsToAdd, ...attributCollection];
    }
    return attributCollection;
  }
}
