import { Component, computed, input, signal } from '@angular/core';
import { VotingCategoryUserOverviewDto } from '../../../dto/voting-category-user-overview.dto';
import { CategoryOverviewItem } from './category-overview-item/category-overview-item';

@Component({
  selector: 'app-category-overview',
  imports: [CategoryOverviewItem],
  templateUrl: './category-overview.component.html',
  styleUrl: './category-overview.component.css',
})
export class CategoryOverview {
  public votingCategories = input.required<VotingCategoryUserOverviewDto[]>();

  private currentTime = signal(new Date());

  protected startedCategories = computed(() =>
    this.votingCategories().filter(
      c =>
        c.userState.hasCreated &&
        !c.userState.hasSubmitted &&
        new Date(c.category.submissionEnd) > this.currentTime()
    )
  );

  protected startableCategories = computed(() =>
    this.votingCategories().filter(
      c =>
        !c.userState.hasCreated &&
        new Date(c.category.votingStart) < this.currentTime() &&
        new Date(c.category.submissionEnd) > this.currentTime()
    )
  );

  protected endedCategories = computed(() =>
    this.votingCategories().filter(
      c =>
        c.userState.hasSubmitted ||
        new Date(c.category.submissionEnd) < this.currentTime()
    )
  );

  protected futureCategories = computed(() =>
    this.votingCategories().filter(
      c =>
        !c.userState.hasCreated &&
        new Date(c.category.votingStart) > this.currentTime()
    )
  );
}
