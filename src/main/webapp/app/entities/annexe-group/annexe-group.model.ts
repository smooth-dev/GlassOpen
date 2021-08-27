import { IAnnexeOut } from 'app/entities/annexe-out/annexe-out.model';
import { IProduit } from 'app/entities/produit/produit.model';

export interface IAnnexeGroup {
  id?: string;
  nomAnnexe?: string;
  annexeouts?: IAnnexeOut[] | null;
  produit?: IProduit | null;
}

export class AnnexeGroup implements IAnnexeGroup {
  constructor(public id?: string, public nomAnnexe?: string, public annexeouts?: IAnnexeOut[] | null, public produit?: IProduit | null) {}
}

export function getAnnexeGroupIdentifier(annexeGroup: IAnnexeGroup): string | undefined {
  return annexeGroup.id;
}
