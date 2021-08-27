import { IDossier } from 'app/entities/dossier/dossier.model';
import { IAttributClient } from 'app/entities/attribut-client/attribut-client.model';

export interface IClient {
  id?: string;
  nomClient?: string;
  prenomClient?: string;
  cin?: string;
  adresse?: string;
  dossiers?: IDossier[] | null;
  attributclients?: IAttributClient[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: string,
    public nomClient?: string,
    public prenomClient?: string,
    public cin?: string,
    public adresse?: string,
    public dossiers?: IDossier[] | null,
    public attributclients?: IAttributClient[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): string | undefined {
  return client.id;
}
