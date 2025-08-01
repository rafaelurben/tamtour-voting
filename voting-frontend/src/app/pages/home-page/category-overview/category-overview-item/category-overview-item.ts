import { Component, input } from '@angular/core';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';
import { VotingCategoryUserStateDto } from '../../../../dto/voting-category-user-state.dto';
import { RouterLink } from '@angular/router';
import { TimeRemaining } from '../../../../components/time-remaining/time-remaining';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-category-overview-item',
  imports: [RouterLink, TimeRemaining, NgClass],
  templateUrl: './category-overview-item.html',
  styleUrl: './category-overview-item.css',
})
export class CategoryOverviewItem {
  public category = input.required<VotingCategoryBaseDto>();
  public userState = input.required<VotingCategoryUserStateDto>();
  public clickable = input<boolean>(true);
  public displayEndCountdown = input<boolean>(false);
  public currentTime = input<Date>();
}
