import {
  Component,
  computed,
  effect,
  inject,
  input,
  OnDestroy,
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
import { UnsavedChangesService } from '../../../service/unsaved-changes.service';
import { Alert } from '../../../components/alert/alert';

@Component({
  selector: 'app-category-vote',
  imports: [VotingPositionOrderer, DatePipe, TimeRemaining, Button, Alert],
  templateUrl: './category-vote.html',
})
export class CategoryVote implements OnDestroy {
  private readonly votingCategoryApi = inject(VotingCategoryApi);
  private readonly timeService = inject(TimeService);
  private readonly unsavedChangesService = inject(UnsavedChangesService);

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

  protected isMapValid = computed(() => {
    return !Object.values(this.categoryData().positionMap).includes(null);
  });

  protected savingMapInProgress = signal(false);
  protected submittingVoteInProgress = signal(false);

  private autoSaveTimeout: number | null = null;

  constructor() {
    effect(() => {
      this.updatedPositionMap.set(this.categoryData().positionMap);
    });
    effect(() =>
      this.unsavedChangesService.hasUnsavedChanges.set(
        this.updatedPositionMapIncludesChanges()
      )
    );
  }

  ngOnDestroy(): void {
    this.clearAutoSaveTimeout();
  }

  private clearAutoSaveTimeout() {
    if (this.autoSaveTimeout) {
      clearTimeout(this.autoSaveTimeout);
      this.autoSaveTimeout = null;
    }
  }

  private startAutoSaveTimeout() {
    this.clearAutoSaveTimeout();
    this.autoSaveTimeout = window.setTimeout(() => {
      if (this.updatedPositionMapIncludesChanges()) {
        this.saveVotingPositions();
      }
    }, 2500);
  }

  protected handleMapUpdate(updatedMap: VotingPositionMapDto) {
    this.updatedPositionMap.set(updatedMap);
    this.startAutoSaveTimeout();
  }

  protected handleResetMap() {
    if (
      confirm(
        'Möchtest du die Rangliste wirklich zurücksetzen und von vorne beginnen?'
      )
    ) {
      const newMap: VotingPositionMapDto = {};
      for (const key in this.categoryData().positionMap) {
        newMap[key] = null;
      }
      this.updatedPositionMap.set(newMap);
      this.saveVotingPositions();
    }
  }

  protected saveVotingPositions() {
    this.clearAutoSaveTimeout();
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
    this.clearAutoSaveTimeout();
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
