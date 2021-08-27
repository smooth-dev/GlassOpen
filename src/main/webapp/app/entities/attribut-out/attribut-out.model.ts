export interface IAttributOut {
  id?: string;
  nomAttribut?: string;
}

export class AttributOut implements IAttributOut {
  constructor(public id?: string, public nomAttribut?: string) {}
}

export function getAttributOutIdentifier(attributOut: IAttributOut): string | undefined {
  return attributOut.id;
}
