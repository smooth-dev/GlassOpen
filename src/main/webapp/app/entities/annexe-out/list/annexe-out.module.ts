import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { ANNEXEOUT_ROUTE } from './annexe-out.route';
import { AnnexeOutComponent } from './annexe-out.component';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatTableModule } from '@angular/material/table';

import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  imports: [
    SharedModule,
    MatButtonModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    MatIconModule,
    RouterModule.forChild([ANNEXEOUT_ROUTE]),
    MatSlideToggleModule,
    MatGridListModule,
    MatExpansionModule,
    MatDatepickerModule,
    MatTableModule,
    MatButtonToggleModule,
    MatDividerModule,
  ],
  declarations: [AnnexeOutComponent],
  exports: [AnnexeOutComponent, MatDividerModule],
})
export class AnnexeOutModule {}
