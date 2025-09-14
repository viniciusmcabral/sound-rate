import { User } from './user.model';

export interface ReviewRequest {
  albumId: string;
  text: string;
}

export interface AlbumReview {
  id: number;
  text: string;
  createdAt: string;
  author: User;
  likesCount: number;
  isLikedByCurrentUser: boolean;
}


