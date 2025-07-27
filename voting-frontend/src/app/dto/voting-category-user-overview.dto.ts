import { VotingCategoryBaseDto } from './voting-category-base.dto';
import { VotingCategoryUserStateDto } from './voting-category-user-state.dto';

export interface VotingCategoryUserOverviewDto {
  category: VotingCategoryBaseDto;
  userState: VotingCategoryUserStateDto;
}
