import { Component, inject, OnInit, signal } from '@angular/core';
import { VotingCategoryApi } from '../../api/voting-category.api';
import { VotingCategoryUserDetailDto } from '../../dto/voting-category-user-detail.dto';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-voting-page',
  imports: [],
  templateUrl: './voting-page.html',
  styleUrl: './voting-page.css',
})
export class VotingPage implements OnInit {
  private votingCategoryApi = inject(VotingCategoryApi);
  private activatedRoute = inject(ActivatedRoute);

  protected categoryData = signal<VotingCategoryUserDetailDto | null>(null);

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.votingCategoryApi
        .getCategory(params['categoryId'])
        .subscribe(data => this.categoryData.set(data));
    });
  }
}
