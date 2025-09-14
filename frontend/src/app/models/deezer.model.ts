export interface DeezerArtist {
  id: number;
  name: string;
  picture: string;
  picture_medium: string;
  tracklist: string;
}

export interface DeezerContributor {
  id: number;
  name: string;
  role: string;
}

export interface DeezerTrack {
  id: number;
  title: string;
  duration: number;
  preview: string | null;
  artist: DeezerArtist;
  contributors: DeezerContributor[];
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
  contributors: DeezerContributor[];
}

