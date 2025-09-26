import { DeezerAlbum, DeezerArtistDetails } from './deezer.model';
import { Page } from './page.model';

export interface ArtistPage {
  artistDetails: DeezerArtistDetails;
  albums: Page<DeezerAlbum>;
}