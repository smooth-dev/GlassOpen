import { IClient } from 'app/entities/client/client.model';
import { IProduit } from 'app/entities/produit/produit.model';

export interface IDossier {
  id?: string;
  numeroDossier?: string;
  client?: IClient | null;
  produit?: IProduit | null;
}

export class Dossier implements IDossier {
  constructor(public id?: string, public numeroDossier?: string, public client?: IClient | null, public produit?: IProduit | null) {}
}

export function getDossierIdentifier(dossier: IDossier): string | undefined {
  return dossier.id;
}
