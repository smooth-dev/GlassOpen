import { IAttributClient } from 'app/entities/attribut-client/attribut-client.model';
import { IAnnexeGroup } from 'app/entities/annexe-group/annexe-group.model';

export interface IAnnexeOut {
  id?: string;
  nomAnnexe?: string;
  idClient?: string;
  attributclients?: IAttributClient[] | null;
  annexegroup?: IAnnexeGroup | null;
}

export class AnnexeOut implements IAnnexeOut {
  constructor(
    public id?: string,
    public nomAnnexe?: string,
    public idClient?: string,
    public attributclients?: IAttributClient[] | null,
    public annexegroup?: IAnnexeGroup | null
  ) {}
}

export function getAnnexeOutIdentifier(annexeOut: IAnnexeOut): string | undefined {
  return annexeOut.id;
}
