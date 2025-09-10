import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SpotifyAlbum } from '../models/spotify.model';
import { AlbumDetails } from '../models/album-details.model';
import { RatingRequest } from '../models/rating.model';

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  private apiUrl = '/api/v1'; 

  constructor(private http: HttpClient) { }

  searchAlbums(query: string): Observable<SpotifyAlbum[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<SpotifyAlbum[]>(`${this.apiUrl}/albums/search`, { params });
  }

  getAlbumDetails(albumId: string): Observable<AlbumDetails> {
    return this.http.get<AlbumDetails>(`${this.apiUrl}/albums/${albumId}`);
  }

  rateAlbumOrTrack(ratingData: RatingRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/ratings`, ratingData);
  }
}
