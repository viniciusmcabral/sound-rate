import { AlbumRating } from './rating.model';
import { AlbumReview } from './review.model';
import { User } from './user.model';

export interface UserProfile {
  user: User;
  totalReviews: number;
  totalAlbumRatings: number;
  totalTrackRatings: number;
  recentReviews: AlbumReview[];
  recentAlbumRatings: AlbumRating[];
}
