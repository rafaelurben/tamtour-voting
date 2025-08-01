import {
  Component,
  computed,
  effect,
  inject,
  input,
  output,
  signal,
} from '@angular/core';
import { VotingCategoryUserDetailDto } from '../../../dto/voting-category-user-detail.dto';
import { VotingCategoryApi } from '../../../api/voting-category.api';
import { VotingPositionOrderer } from './voting-position-orderer/voting-position-orderer';
import { VotingPositionMapDto } from '../../../dto/voting-position-map.dto';
import { DatePipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { interval, map } from 'rxjs';
import { TimeRemaining } from '../../../components/time-remaining/time-remaining';

@Component({
  selector: 'app-category-vote',
  imports: [VotingPositionOrderer, DatePipe, TimeRemaining],
  templateUrl: './category-vote.html',
  styleUrl: './category-vote.css',
})
export class CategoryVote {
  private readonly votingCategoryApi = inject(VotingCategoryApi);
  protected readonly currentTime = toSignal(
    interval(1000).pipe(map(() => new Date())),
    { initialValue: new Date() }
  );

  public categoryData = input.required<VotingCategoryUserDetailDto>();

  public updateData = output<void>();

  protected updatedPositionMap = signal<VotingPositionMapDto>({});
  protected updatedPositionMapIncludesChanges = computed(() => {
    const currentMap = this.categoryData().positionMap;
    const updatedMap = this.updatedPositionMap();

    return JSON.stringify(currentMap) !== JSON.stringify(updatedMap);
  });

  protected isSubmissionStartPassed = computed(
    () =>
      this.currentTime() >=
      new Date(this.categoryData().category.submissionStart)
  );

  protected isSubmissionEndPassed = computed(
    () =>
      this.currentTime() >= new Date(this.categoryData().category.submissionEnd)
  );

  protected isSubmissionPhase = computed(() => {
    return this.isSubmissionStartPassed() && !this.isSubmissionEndPassed();
  });

  protected isMapValid = computed(() => {
    return !Object.values(this.categoryData().positionMap).includes(null);
  });

  constructor() {
    effect(() => {
      this.updatedPositionMap.set(this.categoryData().positionMap);
    });
  }

  protected saveVotingPositions() {
    this.votingCategoryApi
      .updateCategoryVotingPositions(
        this.categoryData().category.id,
        this.updatedPositionMap()
      )
      .subscribe({
        next: () => {
          this.updateData.emit();
          // TODO: show success message
        },
        error: error => {
          console.error('Error updating voting positions:', error);
        },
      });
  }

  protected submitVote() {
    this.votingCategoryApi
      .submitCategoryVoting(this.categoryData().category.id)
      .subscribe({
        next: () => {
          this.updateData.emit();
          // TODO: show success message
        },
        error: error => {
          console.error('Error submitting vote:', error);
        },
      });
  }
}
