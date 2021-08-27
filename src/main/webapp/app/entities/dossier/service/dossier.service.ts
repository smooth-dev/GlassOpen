import { IProduit } from './../../produit/produit.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDossier, getDossierIdentifier } from '../dossier.model';
import { IAnnexeOut } from 'app/entities/annexe-out/annexe-out.model';

export type EntityResponseType = HttpResponse<IDossier>;
export type EntityArrayResponseType = HttpResponse<IDossier[]>;

@Injectable({ providedIn: 'root' })
export class DossierService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/dossiers');
  public resource = 'filter';
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.post<IDossier>(this.resourceUrl, dossier, { observe: 'response' });
  }

  update(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.put<IDossier>(`${this.resourceUrl}/${getDossierIdentifier(dossier) as string}`, dossier, { observe: 'response' });
  }

  partialUpdate(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.patch<IDossier>(`${this.resourceUrl}/${getDossierIdentifier(dossier) as string}`, dossier, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDossier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByNumeroDossier(numDossier: string): Observable<EntityResponseType> {
    return this.http.get<IProduit>(`${this.resourceUrl}/${this.resource}/${numDossier}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDossier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDossierToCollectionIfMissing(dossierCollection: IDossier[], ...dossiersToCheck: (IDossier | null | undefined)[]): IDossier[] {
    const dossiers: IDossier[] = dossiersToCheck.filter(isPresent);
    if (dossiers.length > 0) {
      const dossierCollectionIdentifiers = dossierCollection.map(dossierItem => getDossierIdentifier(dossierItem)!);
      const dossiersToAdd = dossiers.filter(dossierItem => {
        const dossierIdentifier = getDossierIdentifier(dossierItem);
        if (dossierIdentifier == null || dossierCollectionIdentifiers.includes(dossierIdentifier)) {
          return false;
        }
        dossierCollectionIdentifiers.push(dossierIdentifier);
        return true;
      });
      return [...dossiersToAdd, ...dossierCollection];
    }
    return dossierCollection;
  }
}
