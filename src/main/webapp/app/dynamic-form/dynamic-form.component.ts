import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormField } from '../form-field';
import { FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-app-dynamic-form',
  templateUrl: './dynamic-form.component.html',
  styleUrls: ['./dynamic-form.component.css'],
})
export class DynamicFormComponent implements OnInit {
  formFields!: FormField[];
  form = new FormGroup({});

  constructor(private httpClient: HttpClient) {}

  ngOnInit(): void {
    // this.httpClient.get<FormField[]>("/assets/form.json").subscribe((formFields) => {
    //   for (const formField of formFields) {
    //     this.form.addControl(formField.fieldName, new FormControl('',this.getValidator(formField)));
    //   }
    this.formFields = [
      {
        fieldName: 'Num carte grise',
        fieldType: 'text',
        required: true,
        validator: 'email',
      },
      {
        fieldName: 'Adresse secondaire',
        fieldType: 'email',
        required: true,
        validator: 'email',
      },
      {
        fieldName: 'Salaire net',
        fieldType: 'text',
        required: true,
        validator: 'email',
      },
    ];
    // });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const value = this.form.value;
      console.log(this.form.value);
    }
  }

  private getValidator(formField: FormField): ValidatorFn {
    switch (formField.validator) {
      case 'email':
        return Validators.email;
      case 'required':
        return Validators.required;
      default:
        return Validators.required;
    }
  }
}
