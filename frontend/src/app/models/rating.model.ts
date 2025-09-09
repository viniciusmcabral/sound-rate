import { User } from './user.model';

export interface AlbumRating {
  id: number;
  rating: number;
  author: User;
}

export interface TrackRating {
  id: number;
  rating: number;
  trackId: string;
  author: User;
}
