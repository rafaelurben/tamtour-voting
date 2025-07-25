export interface VotingUserDto {
  id: number;
  firstName: string;
  lastName: string;
  accountEmail: string;
  accountName: string;
  pictureLink: string;
  initialRegistrationComplete: boolean;
  createdAt: string;
  lastLoginAt: string | null;
  blocked: boolean;
}
