import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttributClient, getAttributClientIdentifier } from '../attribut-client.model';

export type EntityResponseType = HttpResponse<IAttributClient>;
export type EntityArrayResponseType = HttpResponse<IAttributClient[]>;

@Injectable({ providedIn: 'root' })
export class AttributClientService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attribut-clients');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(attributClient: IAttributClient): Observable<EntityResponseType> {
    return this.http.post<IAttributClient>(this.resourceUrl, attributClient, { observe: 'response' });
  }

  update(attributClient: IAttributClient): Observable<EntityResponseType> {
    return this.http.put<IAttributClient>(`${this.resourceUrl}/${getAttributClientIdentifier(attributClient) as string}`, attributClient, {
      observe: 'response',
    });
  }

  partialUpdate(attributClient: IAttributClient): Observable<EntityResponseType> {
    return this.http.patch<IAttributClient>(
      `${this.resourceUrl}/${getAttributClientIdentifier(attributClient) as string}`,
      attributClient,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAttributClient>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttributClient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAttributClientToCollectionIfMissing(
    attributClientCollection: IAttributClient[],
    ...attributClientsToCheck: (IAttributClient | null | undefined)[]
  ): IAttributClient[] {
    const attributClients: IAttributClient[] = attributClientsToCheck.filter(isPresent);
    if (attributClients.length > 0) {
      const attributClientCollectionIdentifiers = attributClientCollection.map(
        attributClientItem => getAttributClientIdentifier(attributClientItem)!
      );
      const attributClientsToAdd = attributClients.filter(attributClientItem => {
        const attributClientIdentifier = getAttributClientIdentifier(attributClientItem);
        if (attributClientIdentifier == null || attributClientCollectionIdentifiers.includes(attributClientIdentifier)) {
          return false;
        }
        attributClientCollectionIdentifiers.push(attributClientIdentifier);
        return true;
      });
      return [...attributClientsToAdd, ...attributClientCollection];
    }
    return attributClientCollection;
  }
}
