import { DeezerAlbum } from "./deezer.model";

export interface UserRating {
  album: DeezerAlbum;
  rating: number; 
  ratingDate: string; 
  reviewText: string | null; 
}
