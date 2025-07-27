import { VotingCategoryBaseDto } from './voting-category-base.dto';
import { VotingCategoryUserStateDto } from './voting-category-user-state.dto';
import { VotingCandidateDto } from './voting-candidate.dto';
import { VotingPositionMapDto } from './voting-position-map.dto';

export interface VotingCategoryUserDetailDto {
  category: VotingCategoryBaseDto;
  userState: VotingCategoryUserStateDto;
  candidates: VotingCandidateDto[];
  positionMap: VotingPositionMapDto;
}
