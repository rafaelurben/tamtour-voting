import { VotingUserDto } from './voting-user.dto';

export interface WhoamiDto {
  user: VotingUserDto;
  roles: string[];
  isAdmin: boolean;
}
