import { Component, inject, OnInit, signal } from '@angular/core';
import { VotingCategoryApi } from '../../api/voting-category.api';
import { VotingCategoryUserDetailDto } from '../../dto/voting-category-user-detail.dto';
import { ActivatedRoute } from '@angular/router';
import { CategoryVote } from './category-vote/category-vote';
import { Spinner } from '../../components/spinner/spinner';

@Component({
  selector: 'app-voting-page',
  imports: [CategoryVote, Spinner],
  templateUrl: './voting-page.html',
  styleUrl: './voting-page.css',
})
export class VotingPage implements OnInit {
  private readonly votingCategoryApi = inject(VotingCategoryApi);
  private readonly activatedRoute = inject(ActivatedRoute);

  protected categoryData = signal<VotingCategoryUserDetailDto | null>(null);

  ngOnInit(): void {
    this.fetchData();
  }

  protected fetchData() {
    this.activatedRoute.params.subscribe(params => {
      this.votingCategoryApi
        .getCategory(params['categoryId'])
        .subscribe(data => this.categoryData.set(data));
    });
  }
}
