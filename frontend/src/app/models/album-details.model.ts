import { AlbumReview } from './review.model';
import { DeezerAlbum } from './deezer.model';

export interface AlbumDetails {
  deezerDetails: DeezerAlbum;
  communityScore: number | null;
  currentUserRating: number | null;
  currentUserReview: AlbumReview | null;
  userReviews: AlbumReview[];
  likesCount: number;
  isLikedByCurrentUser: boolean;
  isOnListenLaterList: boolean;
}