import { AlbumReview, CriticReview } from './review.model';
import { SpotifyAlbum } from './spotify.model';

export interface AlbumDetails {
  spotifyDetails: SpotifyAlbum;
  criticReview: CriticReview | null;
  communityScore: number | null;
  currentUserRating: number | null;
  userReviews: AlbumReview[];
}
