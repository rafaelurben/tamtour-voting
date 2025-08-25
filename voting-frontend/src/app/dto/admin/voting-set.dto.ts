import { VotingUserDto } from '../voting-user.dto';
import { VotingPositionMapDto } from '../voting-position-map.dto';

export interface VotingSetDto {
  id: number;
  votingUser: VotingUserDto;
  createdAt: string; // ISO 8601 date-time string
  modifiedAt: string; // ISO 8601 date-time string
  submittedAt: string | null; // ISO 8601 date-time string or null
  submitted: boolean;
  disqualified: boolean;
  positionMap: VotingPositionMapDto;
}
