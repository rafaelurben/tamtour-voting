export interface VotingUserDto {
  id: number;
  firstName: string;
  lastName: string;
  accountEmail: string;
  accountName: string;
  pictureLink: string;
  initialRegistrationComplete: boolean;
  createdAt: string; // ISO datetime string including timezone
  lastLoginAt: string | null; // ISO datetime string including timezone
  blocked: boolean;
}
