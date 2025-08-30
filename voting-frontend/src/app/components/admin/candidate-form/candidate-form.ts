import { Component, inject, input, OnInit, output } from '@angular/core';
import {
  FormBuilder,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Button } from '../../button/button';
import { VotingCandidateDto } from '../../../dto/voting-candidate.dto';
import { VotingCandidateRequestDto } from '../../../dto/admin/voting-candidate-request.dto';

@Component({
  selector: 'app-candidate-form',
  imports: [Button, FormsModule, ReactiveFormsModule],
  templateUrl: './candidate-form.html',
})
export class CandidateForm implements OnInit {
  public defaultValues = input<VotingCandidateDto>();
  public loading = input(false);

  public formSubmit = output<VotingCandidateRequestDto>();
  public formCancel = output<void>();

  private readonly formBuilder = inject(FormBuilder);

  protected candidateForm = this.formBuilder.nonNullable.group({
    name: ['', Validators.required],
    startNumber: ['', Validators.required],
  });

  ngOnInit() {
    const values = this.defaultValues();
    if (values) {
      this.candidateForm.setValue({
        name: values.name,
        startNumber: values.startNumber,
      });
    }
  }

  protected onSubmit() {
    if (this.candidateForm.invalid) return;

    const candidate: VotingCandidateRequestDto =
      this.candidateForm.getRawValue();
    this.formSubmit.emit(candidate);
  }
}
