import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnexe, getAnnexeIdentifier } from '../annexe.model';

export type EntityResponseType = HttpResponse<IAnnexe>;
export type EntityArrayResponseType = HttpResponse<IAnnexe[]>;

@Injectable({ providedIn: 'root' })
export class AnnexeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/annexes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(annexe: IAnnexe): Observable<EntityResponseType> {
    return this.http.post<IAnnexe>(this.resourceUrl, annexe, { observe: 'response' });
  }

  update(annexe: IAnnexe): Observable<EntityResponseType> {
    return this.http.put<IAnnexe>(`${this.resourceUrl}/${getAnnexeIdentifier(annexe) as string}`, annexe, { observe: 'response' });
  }

  partialUpdate(annexe: IAnnexe): Observable<EntityResponseType> {
    return this.http.patch<IAnnexe>(`${this.resourceUrl}/${getAnnexeIdentifier(annexe) as string}`, annexe, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAnnexe>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnnexe[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAnnexeToCollectionIfMissing(annexeCollection: IAnnexe[], ...annexesToCheck: (IAnnexe | null | undefined)[]): IAnnexe[] {
    const annexes: IAnnexe[] = annexesToCheck.filter(isPresent);
    if (annexes.length > 0) {
      const annexeCollectionIdentifiers = annexeCollection.map(annexeItem => getAnnexeIdentifier(annexeItem)!);
      const annexesToAdd = annexes.filter(annexeItem => {
        const annexeIdentifier = getAnnexeIdentifier(annexeItem);
        if (annexeIdentifier == null || annexeCollectionIdentifiers.includes(annexeIdentifier)) {
          return false;
        }
        annexeCollectionIdentifiers.push(annexeIdentifier);
        return true;
      });
      return [...annexesToAdd, ...annexeCollection];
    }
    return annexeCollection;
  }
}
