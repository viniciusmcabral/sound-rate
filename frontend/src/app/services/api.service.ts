import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AlbumDetails } from '../models/album-details.model';
import { RatingRequest } from '../models/rating.model';
import { AlbumReview, ReviewRequest } from '../models/review.model';
import { UserProfile } from '../models/user-profile.model';
import { UserRating } from '../models/user-rating.model';
import { Page } from '../models/page.model';
import { DeezerAlbum } from '../models/deezer.model';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private apiUrl = '/api/v1';

  constructor(private http: HttpClient) { }

  searchAlbums(query: string): Observable<DeezerAlbum[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<DeezerAlbum[]>(`${this.apiUrl}/albums/search`, { params });
  }

  getAlbumDetails(albumId: string): Observable<AlbumDetails> {
    return this.http.get<AlbumDetails>(`${this.apiUrl}/albums/${albumId}`);
  }

  rateAlbumOrTrack(ratingData: RatingRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/ratings`, ratingData);
  }

  likeAlbum(albumId: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/albums/${albumId}/like`, {});
  }

  unlikeAlbum(albumId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/albums/${albumId}/like`);
  }

  likeReview(reviewId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/reviews/${reviewId}/like`, {});
  }

  unlikeReview(reviewId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/reviews/${reviewId}/like`);
  }

  createReview(reviewData: ReviewRequest): Observable<AlbumReview> {
    return this.http.post<AlbumReview>(`${this.apiUrl}/reviews`, reviewData);
  }

  updateReview(reviewId: number, reviewData: ReviewRequest): Observable<AlbumReview> {
    return this.http.put<AlbumReview>(`${this.apiUrl}/reviews/${reviewId}`, reviewData);
  }

  deleteReview(reviewId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/reviews/${reviewId}`);
  }

  getUserProfile(username: string): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/users/${username}`);
  }

  getRatedAlbums(username: string, page: number, size: number): Observable<Page<UserRating>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<Page<UserRating>>(`${this.apiUrl}/users/${username}/ratings`, { params });
  }

  getLikedAlbums(username: string, page: number, size: number): Observable<Page<DeezerAlbum>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<Page<DeezerAlbum>>(`${this.apiUrl}/users/${username}/likes`, { params });
  }

  getListenLaterList(page: number, size: number): Observable<Page<DeezerAlbum>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<Page<DeezerAlbum>>(`${this.apiUrl}/listen-later`, { params });
  }

  addToListenLater(albumId: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/listen-later/${albumId}`, {});
  }

  removeFromListenLater(albumId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/listen-later/${albumId}`);
  }

  followUser(username: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/users/${username}/follow`, {});
  }

  unfollowUser(username: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${username}/follow`);
  }

  deleteCurrentUser(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/me`);
  }

  updateProfile(data: { email: string }): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/me/profile`, data);
  }

  updatePassword(data: any): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/users/me/password`, data);
  }

  updateAvatar(file: File): Observable<User> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.http.put<User>(`${this.apiUrl}/users/me/avatar`, formData);
  }

  resetAvatar(): Observable<User> {
    return this.http.delete<User>(`${this.apiUrl}/users/me/avatar`);
  }
}