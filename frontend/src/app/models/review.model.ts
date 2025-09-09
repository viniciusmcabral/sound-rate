import { User } from './user.model';

export interface AlbumReview {
  id: number;
  text: string;
  createdAt: string; 
  author: User;
}

export interface CriticReview {
  source: string;
  score: number;
  reviewUrl: string;
}
