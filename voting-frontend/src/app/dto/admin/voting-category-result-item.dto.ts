import { VotingCandidateDto } from '../voting-candidate.dto';

export interface VotingCategoryResultItemDto {
  candidate: VotingCandidateDto;
  points: number;
  rank: number;
}
