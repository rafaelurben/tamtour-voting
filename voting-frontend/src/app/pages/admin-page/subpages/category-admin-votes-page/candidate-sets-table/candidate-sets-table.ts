import { Component, computed, inject, input, output } from '@angular/core';
import { VotingCandidateDto } from '../../../../../dto/voting-candidate.dto';
import { VotingSetDto } from '../../../../../dto/admin/voting-set.dto';
import { AdminVotingCategoriesApi } from '../../../../../api/admin/admin-voting-categories.api';
import { VotingSetUpdateDto } from '../../../../../dto/admin/voting-set-update.dto';
import { Button } from '../../../../../components/button/button';

@Component({
  selector: 'app-candidate-sets-table',
  imports: [Button],
  templateUrl: './candidate-sets-table.html',
  styleUrl: './candidate-sets-table.css',
})
export class CandidateSetsTable {
  private readonly adminCategoriesApi = inject(AdminVotingCategoriesApi);

  public categoryId = input.required<number>();
  public candidates = input.required<VotingCandidateDto[]>();
  public sets = input.required<VotingSetDto[]>();

  public replaceSet = output<VotingSetDto>();

  protected sortedCandidates = computed(() =>
    this.candidates().toSorted((a, b) =>
      a.startNumber.localeCompare(b.startNumber)
    )
  );

  protected sortedSets = computed(() =>
    this.sets().toSorted((a, b) => {
      if (a.disqualified === b.disqualified) {
        if (a.submitted === b.submitted) {
          return a.id - b.id;
        }
        return a.submitted ? -1 : 1;
      }
      return a.disqualified ? 1 : -1;
    })
  );

  private updateSet(setId: number, update: VotingSetUpdateDto) {
    this.adminCategoriesApi
      .updateSet(this.categoryId(), setId, update)
      .subscribe({
        next: newSet => {
          this.replaceSet.emit(newSet);
        },
      });
  }

  protected disqualifySet(setId: number) {
    this.updateSet(setId, { disqualified: true });
  }

  protected undisqualifySet(setId: number) {
    this.updateSet(setId, { disqualified: false });
  }

  protected unsubmitSet(setId: number) {
    this.updateSet(setId, { submitted: false });
  }

  protected calcColor(rank: number | null): string {
    const maxRank = this.candidates().length;
    if (rank === null) {
      return '#ffffff';
    }
    const r = Math.round(rank * (255 / maxRank));
    const g = 255 - Math.round(rank * (255 / maxRank));
    const b = 0;
    return `rgb(${r}, ${g}, ${b})`;
  }
}
