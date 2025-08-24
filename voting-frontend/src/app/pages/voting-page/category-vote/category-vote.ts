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
import { TimeRemaining } from '../../../components/time-remaining/time-remaining';
import { Button } from '../../../components/button/button';
import { TimeService } from '../../../service/time.service';

@Component({
  selector: 'app-category-vote',
  imports: [VotingPositionOrderer, DatePipe, TimeRemaining, Button],
  templateUrl: './category-vote.html',
  styleUrl: './category-vote.css',
})
export class CategoryVote {
  private readonly votingCategoryApi = inject(VotingCategoryApi);
  private readonly timeService = inject(TimeService);

  protected readonly currentTime = this.timeService.currentTime1s;

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

  protected savingMapInProgress = signal(false);
  protected submittingVoteInProgress = signal(false);

  constructor() {
    effect(() => {
      this.updatedPositionMap.set(this.categoryData().positionMap);
    });
  }

  protected saveVotingPositions() {
    this.savingMapInProgress.set(true);
    this.votingCategoryApi
      .updateCategoryVotingPositions(
        this.categoryData().category.id,
        this.updatedPositionMap()
      )
      .subscribe({
        next: () => {
          this.savingMapInProgress.set(false);
          this.updateData.emit();
        },
        error: error => {
          this.savingMapInProgress.set(false);
          throw error;
        },
      });
  }

  protected submitVote() {
    this.submittingVoteInProgress.set(true);
    this.votingCategoryApi
      .submitCategoryVoting(this.categoryData().category.id)
      .subscribe({
        next: () => {
          this.submittingVoteInProgress.set(false);
          this.updateData.emit();
        },
        error: error => {
          this.submittingVoteInProgress.set(false);
          throw error;
        },
      });
  }
}
