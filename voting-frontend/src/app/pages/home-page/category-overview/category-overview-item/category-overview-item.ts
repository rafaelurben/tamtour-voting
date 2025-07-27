import { Component, input } from '@angular/core';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';
import { VotingCategoryUserStateDto } from '../../../../dto/voting-category-user-state.dto';

@Component({
  selector: 'app-category-overview-item',
  imports: [],
  templateUrl: './category-overview-item.html',
  styleUrl: './category-overview-item.css',
})
export class CategoryOverviewItem {
  public category = input.required<VotingCategoryBaseDto>();
  public userState = input.required<VotingCategoryUserStateDto>();
  public clickable = input<boolean>(true);
}
