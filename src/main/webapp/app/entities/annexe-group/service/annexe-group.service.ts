import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnexeGroup, getAnnexeGroupIdentifier } from '../annexe-group.model';

export type EntityResponseType = HttpResponse<IAnnexeGroup>;
export type EntityArrayResponseType = HttpResponse<IAnnexeGroup[]>;

@Injectable({ providedIn: 'root' })
export class AnnexeGroupService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/annexe-groups');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(annexeGroup: IAnnexeGroup): Observable<EntityResponseType> {
    return this.http.post<IAnnexeGroup>(this.resourceUrl, annexeGroup, { observe: 'response' });
  }

  update(annexeGroup: IAnnexeGroup): Observable<EntityResponseType> {
    return this.http.put<IAnnexeGroup>(`${this.resourceUrl}/${getAnnexeGroupIdentifier(annexeGroup) as string}`, annexeGroup, {
      observe: 'response',
    });
  }

  partialUpdate(annexeGroup: IAnnexeGroup): Observable<EntityResponseType> {
    return this.http.patch<IAnnexeGroup>(`${this.resourceUrl}/${getAnnexeGroupIdentifier(annexeGroup) as string}`, annexeGroup, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAnnexeGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnnexeGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAnnexeGroupToCollectionIfMissing(
    annexeGroupCollection: IAnnexeGroup[],
    ...annexeGroupsToCheck: (IAnnexeGroup | null | undefined)[]
  ): IAnnexeGroup[] {
    const annexeGroups: IAnnexeGroup[] = annexeGroupsToCheck.filter(isPresent);
    if (annexeGroups.length > 0) {
      const annexeGroupCollectionIdentifiers = annexeGroupCollection.map(annexeGroupItem => getAnnexeGroupIdentifier(annexeGroupItem)!);
      const annexeGroupsToAdd = annexeGroups.filter(annexeGroupItem => {
        const annexeGroupIdentifier = getAnnexeGroupIdentifier(annexeGroupItem);
        if (annexeGroupIdentifier == null || annexeGroupCollectionIdentifiers.includes(annexeGroupIdentifier)) {
          return false;
        }
        annexeGroupCollectionIdentifiers.push(annexeGroupIdentifier);
        return true;
      });
      return [...annexeGroupsToAdd, ...annexeGroupCollection];
    }
    return annexeGroupCollection;
  }
}
