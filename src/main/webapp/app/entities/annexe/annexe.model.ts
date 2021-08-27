import { IAttribut } from 'app/entities/attribut/attribut.model';
import { IProduit } from 'app/entities/produit/produit.model';

export interface IAnnexe {
  id?: string;
  nomAnnexe?: string;
  enteteAnnexe?: string;
  attributs?: IAttribut[] | null;
  produit?: IProduit | null;
}

export class Annexe implements IAnnexe {
  constructor(
    public id?: string,
    public nomAnnexe?: string,
    public enteteAnnexe?: string,
    public attributs?: IAttribut[] | null,
    public produit?: IProduit | null
  ) {}
}

export function getAnnexeIdentifier(annexe: IAnnexe): string | undefined {
  return annexe.id;
}
