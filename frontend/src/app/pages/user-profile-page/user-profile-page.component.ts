import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Observable, BehaviorSubject, switchMap, tap } from 'rxjs';
import { UserProfile } from '../../models/user-profile.model';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { UserRating } from '../../models/user-rating.model';
import { DeezerAlbum } from '../../models/deezer.model';
import { Page } from '../../models/page.model';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ReviewDisplayDialogComponent } from '../../components/review-display-dialog/review-display-dialog.component';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { AlbumCardComponent } from '../../components/album-card/album-card.component';
import { StarRatingComponent } from '../../components/star-rating/star-rating.component';

@Component({
  selector: 'app-user-profile-page',
  standalone: true,
  imports: [
    CommonModule, RouterLink, MatCardModule, MatTabsModule, MatIconModule,
    AlbumCardComponent, StarRatingComponent, MatProgressSpinnerModule, MatButtonModule,
    MatPaginatorModule, MatDialogModule
  ],
  templateUrl: './user-profile-page.component.html',
  styleUrl: './user-profile-page.component.scss'
})
export class UserProfilePageComponent implements OnInit {
  private profileSubject = new BehaviorSubject<UserProfile | null>(null);
  userProfile$ = this.profileSubject.asObservable();
  private username!: string;
  isOwnProfile: boolean = false;

  ratingsSubject = new BehaviorSubject<UserRating[]>([]);
  ratings$ = this.ratingsSubject.asObservable();
  totalRatings = 0;
  ratingsPageSize = 12;
  currentRatingsPage = 0;
  isLoadingRatings = true;

  likedAlbumsSubject = new BehaviorSubject<DeezerAlbum[]>([]);
  likedAlbums$ = this.likedAlbumsSubject.asObservable();
  totalLikedAlbums = 0;
  likedAlbumsPageSize = 18;
  currentLikedAlbumsPage = 0;
  isLoadingLikedAlbums = true;

  listenLaterSubject = new BehaviorSubject<DeezerAlbum[]>([]);
  listenLater$ = this.listenLaterSubject.asObservable();
  totalListenLater = 0;
  listenLaterPageSize = 18;
  currentListenLaterPage = 0;
  isLoadingListenLater = true;

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    public authService: AuthService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        this.username = params.get('username')!;
        this.resetFeeds();
        return this.apiService.getUserProfile(this.username);
      }),
      tap(profile => {
        this.profileSubject.next(profile);
        this.isOwnProfile = this.authService.currentUserValue?.username === profile.user.username;
        this.loadRatedAlbums();
        this.loadLikedAlbums();
        if (this.isOwnProfile) {
          this.loadListenLater();
        }
      })
    ).subscribe();
  }

  toggleFollow(): void {
    const currentProfile = this.profileSubject.getValue();
    if (!currentProfile || this.isOwnProfile) return;

    const isCurrentlyFollowed = currentProfile.isFollowedByCurrentUser;
    const apiCall = isCurrentlyFollowed
      ? this.apiService.unfollowUser(this.username)
      : this.apiService.followUser(this.username);

    this.profileSubject.next({
      ...currentProfile,
      isFollowedByCurrentUser: !isCurrentlyFollowed,
      followersCount: isCurrentlyFollowed ? currentProfile.followersCount - 1 : currentProfile.followersCount + 1
    });

    apiCall.subscribe({
      error: () => {
        this.profileSubject.next(currentProfile);
      }
    });
  }

  loadRatedAlbums(): void {
    this.isLoadingRatings = true;
    this.apiService.getRatedAlbums(this.username, this.currentRatingsPage, this.ratingsPageSize)
      .subscribe(page => {
        this.ratingsSubject.next(page.content);
        this.totalRatings = page.totalElements;
        this.isLoadingRatings = false;
      });
  }

  loadLikedAlbums(): void {
    this.isLoadingLikedAlbums = true;
    this.apiService.getLikedAlbums(this.username, this.currentLikedAlbumsPage, this.likedAlbumsPageSize)
      .subscribe(page => {
        this.likedAlbumsSubject.next(page.content);
        this.totalLikedAlbums = page.totalElements;
        this.isLoadingLikedAlbums = false;
      });
  }

  loadListenLater(): void {
    this.isLoadingListenLater = true;
    this.apiService.getListenLaterList(this.currentListenLaterPage, this.listenLaterPageSize)
      .subscribe(page => {
        this.listenLaterSubject.next(page.content);
        this.totalListenLater = page.totalElements;
        this.isLoadingListenLater = false;
      });
  }

  showReview(rating: UserRating): void {
    if (!rating.reviewText) return;

    this.dialog.open(ReviewDisplayDialogComponent, {
      width: '600px',
      data: {
        albumTitle: rating.album.title,
        reviewText: rating.reviewText
      }
    });
  }

  onRatingsPageChange(event: PageEvent): void {
    this.currentRatingsPage = event.pageIndex;
    this.ratingsPageSize = event.pageSize;
    this.loadRatedAlbums();
  }

  onLikedAlbumsPageChange(event: PageEvent): void {
    this.currentLikedAlbumsPage = event.pageIndex;
    this.likedAlbumsPageSize = event.pageSize;
    this.loadLikedAlbums();
  }

  onListenLaterPageChange(event: PageEvent): void {
    this.currentListenLaterPage = event.pageIndex;
    this.listenLaterPageSize = event.pageSize;
    this.loadListenLater();
  }

  private resetFeeds(): void {
    this.ratingsSubject.next([]);
    this.likedAlbumsSubject.next([]);
    this.currentRatingsPage = 0;
    this.currentLikedAlbumsPage = 0;
    this.isLoadingRatings = true;
    this.isLoadingLikedAlbums = true;
  }
}