import { VotingUserDto } from '../voting-user.dto';
import { VotingPositionMapDto } from '../voting-position-map.dto';

export interface VotingSetDto {
  id: number;
  votingUser: VotingUserDto;
  createdAt: string; // ISO datetime string including timezone
  modifiedAt: string; // ISO datetime string including timezone
  submittedAt: string | null; // ISO datetime string including timezone
  submitted: boolean;
  disqualified: boolean;
  positionMap: VotingPositionMapDto;
}
