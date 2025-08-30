import {
  Component,
  computed,
  inject,
  input,
  output,
  signal,
} from '@angular/core';
import { VotingCandidateDto } from '../../../../../dto/voting-candidate.dto';
import { VotingSetDto } from '../../../../../dto/admin/voting-set.dto';
import { AdminVotingCategoriesApi } from '../../../../../api/admin/admin-voting-categories.api';
import { VotingSetUpdateDto } from '../../../../../dto/admin/voting-set-update.dto';
import { Button } from '../../../../../components/button/button';
import { VotingCategoryResultDto } from '../../../../../dto/admin/voting-category-result.dto';

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
  public result = input.required<VotingCategoryResultDto>();

  public replaceSet = output<VotingSetDto>();

  protected readonly updatingSetIds = signal<number[]>([]);

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

  protected candidateResultMap = computed(() => {
    const map = new Map<number, { points: number; rank: number }>();
    for (const item of this.result().items) {
      map.set(item.candidate.id, { points: item.points, rank: item.rank });
    }
    return map;
  });

  private updateSet(setId: number, update: VotingSetUpdateDto) {
    this.updatingSetIds.update(ids => [...ids, setId]);
    this.adminCategoriesApi
      .updateSet(this.categoryId(), setId, update)
      .subscribe({
        next: newSet => {
          this.replaceSet.emit(newSet);
          this.updatingSetIds.update(ids => ids.filter(id => id !== setId));
        },
      });
  }

  protected disqualifySet(setId: number) {
    this.updateSet(setId, { disqualified: true });
  }

  protected undisqualifySet(setId: number) {
    this.updateSet(setId, { disqualified: false });
  }

  protected submitSet(setId: number) {
    this.updateSet(setId, { submitted: true });
  }

  protected unsubmitSet(setId: number) {
    this.updateSet(setId, { submitted: false });
  }

  protected calcColor(rank: number | null | undefined): string {
    const maxRank = this.candidates().length;
    if (rank === null || rank === undefined) {
      return '#ffffff';
    }
    const value = (rank - 1) / (maxRank - 1);
    const hue = ((1 - value) * 120).toString(10);
    return `hsl(${hue},100%,50%)`;
  }
}
