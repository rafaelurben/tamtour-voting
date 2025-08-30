export interface VotingCategoryBaseDto {
  id: number;
  name: string;
  votingStart: string; // ISO datetime string including timezone
  submissionStart: string; // ISO datetime string including timezone
  submissionEnd: string; // ISO datetime string including timezone
}
