import { IAnnexe } from 'app/entities/annexe/annexe.model';

export interface IAttribut {
  id?: string;
  nomAttribut?: string;
  annexe?: IAnnexe | null;
}

export class Attribut implements IAttribut {
  constructor(public id?: string, public nomAttribut?: string, public annexe?: IAnnexe | null) {}
}

export function getAttributIdentifier(attribut: IAttribut): string | undefined {
  return attribut.id;
}
