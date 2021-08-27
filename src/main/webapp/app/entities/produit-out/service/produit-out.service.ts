import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProduitOut, getProduitOutIdentifier } from '../produit-out.model';

export type EntityResponseType = HttpResponse<IProduitOut>;
export type EntityArrayResponseType = HttpResponse<IProduitOut[]>;

@Injectable({ providedIn: 'root' })
export class ProduitOutService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/produit-outs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(produitOut: IProduitOut): Observable<EntityResponseType> {
    return this.http.post<IProduitOut>(this.resourceUrl, produitOut, { observe: 'response' });
  }

  update(produitOut: IProduitOut): Observable<EntityResponseType> {
    return this.http.put<IProduitOut>(`${this.resourceUrl}/${getProduitOutIdentifier(produitOut) as string}`, produitOut, {
      observe: 'response',
    });
  }

  partialUpdate(produitOut: IProduitOut): Observable<EntityResponseType> {
    return this.http.patch<IProduitOut>(`${this.resourceUrl}/${getProduitOutIdentifier(produitOut) as string}`, produitOut, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IProduitOut>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProduitOut[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProduitOutToCollectionIfMissing(
    produitOutCollection: IProduitOut[],
    ...produitOutsToCheck: (IProduitOut | null | undefined)[]
  ): IProduitOut[] {
    const produitOuts: IProduitOut[] = produitOutsToCheck.filter(isPresent);
    if (produitOuts.length > 0) {
      const produitOutCollectionIdentifiers = produitOutCollection.map(produitOutItem => getProduitOutIdentifier(produitOutItem)!);
      const produitOutsToAdd = produitOuts.filter(produitOutItem => {
        const produitOutIdentifier = getProduitOutIdentifier(produitOutItem);
        if (produitOutIdentifier == null || produitOutCollectionIdentifiers.includes(produitOutIdentifier)) {
          return false;
        }
        produitOutCollectionIdentifiers.push(produitOutIdentifier);
        return true;
      });
      return [...produitOutsToAdd, ...produitOutCollection];
    }
    return produitOutCollection;
  }
}
