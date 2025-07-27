import { Component, inject, OnInit, signal } from '@angular/core';
import { VotingCategoryUserOverviewDto } from '../../dto/voting-category-user-overview.dto';
import { VotingCategoryApi } from '../../api/voting-category.api';
import { CategoryOverview } from './category-overview/category-overview.component';

@Component({
  selector: 'app-home-page',
  imports: [CategoryOverview],
  templateUrl: './home-page.html',
  styleUrl: './home-page.css',
})
export class HomePage implements OnInit {
  private votingCategoryApi = inject(VotingCategoryApi);

  protected votingCategories = signal<VotingCategoryUserOverviewDto[] | null>(
    null
  );

  ngOnInit(): void {
    this.votingCategoryApi
      .getCategories()
      .subscribe(categories => this.votingCategories.set(categories));
  }
}
