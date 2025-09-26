export interface DeezerArtist {
  id: number;
  name: string;
  picture: string;
  picture_medium: string;
  tracklist: string;
}

export interface DeezerArtistDetails {
  id: number;
  name: string;
  link: string;
  picture_xl: string; 
  nb_album: number; 
  nb_fan: number;
}

export interface DeezerSimpleAlbum {
  id: number;
  title: string;
  cover: string;
  cover_small: string;
  cover_medium: string;
  cover_big: string;
  cover_xl: string;
}

export interface DeezerTrack {
  id: number;
  title: string;
  duration: number;
  preview: string | null;
  artist: DeezerArtist;
  album: DeezerSimpleAlbum;
}

export interface DeezerAlbum {
  id: number;
  title: string;
  link: string;
  cover_medium: string;
  cover_xl: string;
  artist: DeezerArtist;
  release_date?: string;
  duration: number;
  fans: number;
  explicit_lyrics: boolean;
  tracks: {
    data: DeezerTrack[];
  };
}

