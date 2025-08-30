import { VotingCategoryResultItemDto } from './voting-category-result-item.dto';

export interface VotingCategoryResultDto {
  ended: boolean;
  title: string;
  items: VotingCategoryResultItemDto[];
}
