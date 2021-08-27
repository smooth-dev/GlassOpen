import { IProduit } from 'app/entities/produit/produit.model';

export interface IProduitOut {
  id?: string;
  nomAnnexe?: string;
  produit?: IProduit | null;
}

export class ProduitOut implements IProduitOut {
  constructor(public id?: string, public nomAnnexe?: string, public produit?: IProduit | null) {}
}

export function getProduitOutIdentifier(produitOut: IProduitOut): string | undefined {
  return produitOut.id;
}
