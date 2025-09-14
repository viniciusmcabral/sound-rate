import { User } from './user.model';

export interface UserProfile {
  user: User;
  totalReviews: number;
  totalAlbumRatings: number;
  totalTrackRatings: number;
  followersCount: number;
  followingCount: number;
  isFollowedByCurrentUser: boolean;
}