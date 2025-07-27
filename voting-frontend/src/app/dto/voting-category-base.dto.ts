export interface VotingCategoryBaseDto {
  id: number;
  name: string;
  votingStart: string; // ISO date
  submissionStart: string; // ISO date
  submissionEnd: string; // ISO date
}
