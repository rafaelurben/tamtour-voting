import { Component, inject, input, OnInit, output } from '@angular/core';
import { VotingCategoryBaseDto } from '../../../dto/voting-category-base.dto';
import { VotingCategoryRequestDto } from '../../../dto/admin/voting-category-request.dto';
import {
  FormBuilder,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Button } from '../../button/button';

@Component({
  selector: 'app-category-form',
  imports: [Button, FormsModule, ReactiveFormsModule],
  templateUrl: './category-form.html',
})
export class CategoryForm implements OnInit {
  public defaultValues = input<VotingCategoryBaseDto>();
  public loading = input(false);

  public formSubmit = output<VotingCategoryRequestDto>();
  public formCancel = output<void>();

  private readonly formBuilder = inject(FormBuilder);

  protected categoryForm = this.formBuilder.nonNullable.group({
    name: ['', Validators.required],
    votingStart: ['', Validators.required],
    submissionStart: ['', Validators.required],
    submissionEnd: ['', Validators.required],
  });

  ngOnInit() {
    const values = this.defaultValues();
    if (values) {
      this.categoryForm.setValue({
        name: values.name,
        votingStart: values.votingStart,
        submissionStart: values.submissionStart,
        submissionEnd: values.submissionEnd,
      });
    }
  }

  protected onSubmit() {
    const category: VotingCategoryRequestDto = this.categoryForm.getRawValue();

    const votingStart = new Date(category.votingStart);
    const submissionStart = new Date(category.submissionStart);
    const submissionEnd = new Date(category.submissionEnd);

    if (submissionEnd <= submissionStart) {
      this.categoryForm
        .get('submissionEnd')
        ?.setErrors({ endBeforeStart: true });
      return;
    }
    if (submissionStart <= votingStart) {
      this.categoryForm
        .get('submissionStart')
        ?.setErrors({ beforeVotingStart: true });
      return;
    }

    if (this.categoryForm.invalid) return;
    this.formSubmit.emit(category);
  }
}
