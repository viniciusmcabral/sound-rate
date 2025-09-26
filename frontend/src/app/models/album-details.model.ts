import { AlbumReview } from './review.model';
import { DeezerAlbum } from './deezer.model';
import { UserTrackRating } from './user-rating.model';

export interface AlbumDetails {
  deezerDetails: DeezerAlbum;
  communityScore: number | null;
  currentUserRating: number | null;
  currentUserReview: AlbumReview | null;
  userReviews: AlbumReview[];
  likesCount: number;
  isLikedByCurrentUser: boolean;
  isOnListenLaterList: boolean;
  currentUserTrackRatings: UserTrackRating[];
  ratingsCount: number;
}

export interface AlbumDashboard {
  id: string;
  title: string;
  coverUrl: string;
  artistName: string;
  averageRating: number;
}
