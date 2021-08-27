import { IClient } from 'app/entities/client/client.model';
import { IAnnexeOut } from 'app/entities/annexe-out/annexe-out.model';

export interface IAttributClient {
  id?: string;
  nomAttribut?: string;
  contenu?: string;
  client?: IClient | null;
  annexeout?: IAnnexeOut | null;
}

export class AttributClient implements IAttributClient {
  constructor(
    public id?: string,
    public nomAttribut?: string,
    public contenu?: string,
    public client?: IClient | null,
    public annexeout?: IAnnexeOut | null
  ) {}
}

export function getAttributClientIdentifier(attributClient: IAttributClient): string | undefined {
  return attributClient.id;
}
