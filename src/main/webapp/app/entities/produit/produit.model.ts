import { IDossier } from 'app/entities/dossier/dossier.model';
import { IAnnexe } from 'app/entities/annexe/annexe.model';
import { IAnnexeGroup } from 'app/entities/annexe-group/annexe-group.model';
import { IProduitOut } from 'app/entities/produit-out/produit-out.model';

export interface IProduit {
  id?: string;
  nomProduit?: string;
  dossiers?: IDossier[] | null;
  annexes?: IAnnexe[] | null;
  annexegroups?: IAnnexeGroup[] | null;
  produitouts?: IProduitOut[] | null;
}

export class Produit implements IProduit {
  constructor(
    public id?: string,
    public nomProduit?: string,
    public dossiers?: IDossier[] | null,
    public annexes?: IAnnexe[] | null,
    public annexegroups?: IAnnexeGroup[] | null,
    public produitouts?: IProduitOut[] | null
  ) {}
}

export function getProduitIdentifier(produit: IProduit): string | undefined {
  return produit.id;
}
