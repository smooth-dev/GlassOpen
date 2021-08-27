import { Route } from '@angular/router';

import { AnnexeOutComponent } from './annexe-out.component';

export const ANNEXEOUT_ROUTE: Route = {
  path: '',
  component: AnnexeOutComponent,
  data: {
    pageTitle: 'Crédit du Maroc, Simulateur des crédits',
  },
};
