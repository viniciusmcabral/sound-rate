import { DeezerAlbum, DeezerArtist } from './deezer.model';
import { User } from './user.model';

export interface SearchResult {
    type: 'album' | 'artist' | 'user';
    album?: DeezerAlbum;
    artist?: DeezerArtist;
    user?: User;
}