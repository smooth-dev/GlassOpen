import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'Clients' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'dossier',
        data: { pageTitle: 'Dossiers' },
        loadChildren: () => import('./dossier/dossier.module').then(m => m.DossierModule),
      },
      {
        path: 'produit',
        data: { pageTitle: 'Produits' },
        loadChildren: () => import('./produit/produit.module').then(m => m.ProduitModule),
      },
      {
        path: 'annexe',
        data: { pageTitle: 'Annexes' },
        loadChildren: () => import('./annexe/annexe.module').then(m => m.AnnexeModule),
      },
      {
        path: 'attribut',
        data: { pageTitle: 'Attributs' },
        loadChildren: () => import('./attribut/attribut.module').then(m => m.AttributModule),
      },
      {
        path: 'attribut-client',
        data: { pageTitle: 'AttributClients' },
        loadChildren: () => import('./attribut-client/attribut-client.module').then(m => m.AttributClientModule),
      },
      {
        path: 'annexe-out',
        data: { pageTitle: 'AnnexeOuts' },
        loadChildren: () => import('./annexe-out/annexe-out.module').then(m => m.AnnexeOutModule),
      },
      {
        path: 'attribut-out',
        data: { pageTitle: 'AttributOuts' },
        loadChildren: () => import('./attribut-out/attribut-out.module').then(m => m.AttributOutModule),
      },
      {
        path: 'produit-out',
        data: { pageTitle: 'ProduitOuts' },
        loadChildren: () => import('./produit-out/produit-out.module').then(m => m.ProduitOutModule),
      },
      {
        path: 'annexe-group',
        data: { pageTitle: 'AnnexeGroups' },
        loadChildren: () => import('./annexe-group/annexe-group.module').then(m => m.AnnexeGroupModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
