import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Observable, BehaviorSubject, switchMap } from 'rxjs';
import { AlbumDetails } from '../../models/album-details.model';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { AlbumReview } from '../../models/review.model';
import { AudioService } from '../../services/audio.service';
import { DeezerTrack } from '../../models/deezer.model';
import { CommonModule, NgClass, JsonPipe, DatePipe, DecimalPipe, SlicePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { StarRatingComponent } from '../../components/star-rating/star-rating.component';
import { ReviewDialogComponent } from '../../components/review-dialog/review-dialog.component';
import { ReviewListComponent } from '../../components/review-list/review-list.component';
import { FormatDurationPipe } from '../../pipes/format-duration.pipe';

@Component({
  selector: 'app-album-details-page',
  standalone: true,
  imports: [
    CommonModule, RouterLink, MatCardModule, MatTabsModule, MatIconModule, MatListModule,
    StarRatingComponent, MatSnackBarModule, MatButtonModule, MatDialogModule,
    ReviewListComponent, MatProgressSpinnerModule, FormatDurationPipe,
    NgClass, JsonPipe, DatePipe, DecimalPipe, SlicePipe
  ],
  templateUrl: './album-details-page.component.html',
  styleUrl: './album-details-page.component.scss'
})
export class AlbumDetailsPageComponent implements OnInit {
  private albumDetailsSubject = new BehaviorSubject<AlbumDetails | null>(null);
  albumDetails$ = this.albumDetailsSubject.asObservable();
  albumId!: string;

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private snackBar: MatSnackBar,
    public authService: AuthService,
    public audioService: AudioService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (!id) throw new Error('Album ID not found');
        this.albumId = id;
        return this.apiService.getAlbumDetails(this.albumId);
      })
    ).subscribe(details => {
      this.albumDetailsSubject.next(details);
    });
  }

  loadAlbumDetails(): void {
    this.apiService.getAlbumDetails(this.albumId).subscribe(details => {
      this.albumDetailsSubject.next(details);
    });
  }

  toggleLike(): void {
    const currentDetails = this.albumDetailsSubject.getValue();
    if (!currentDetails) return;

    const isCurrentlyLiked = currentDetails.isLikedByCurrentUser;

    this.albumDetailsSubject.next({
      ...currentDetails,
      isLikedByCurrentUser: !isCurrentlyLiked,
      likesCount: isCurrentlyLiked ? currentDetails.likesCount - 1 : currentDetails.likesCount + 1
    });

    const apiCall = isCurrentlyLiked
      ? this.apiService.unlikeAlbum(this.albumId)
      : this.apiService.likeAlbum(this.albumId);

    apiCall.subscribe({
      error: () => {
        this.albumDetailsSubject.next(currentDetails);
        this.snackBar.open('Error updating like status.', 'Close', { duration: 3000 });
      }
    });
  }

  toggleListenLater(): void {
    const currentDetails = this.albumDetailsSubject.getValue();
    if (!currentDetails) return;

    const isOnList = currentDetails.isOnListenLaterList;
    const apiCall = isOnList ? this.apiService.removeFromListenLater(this.albumId) : this.apiService.addToListenLater(this.albumId);

    this.albumDetailsSubject.next({
      ...currentDetails,
      isOnListenLaterList: !isOnList,
    });

    apiCall.subscribe({
      error: () => {
        this.albumDetailsSubject.next(currentDetails);
        this.snackBar.open('Error updating your Listen Later list.', 'Close', { duration: 3000 });
      }
    });
  }

  onRatingChanged(newRating: number): void {
    this.apiService.rateAlbumOrTrack({ albumId: this.albumId, rating: newRating }).subscribe({
      next: () => {
        this.snackBar.open('Your rating has been saved!', 'Close', { duration: 3000 });
        this.loadAlbumDetails();
      },
      error: (err) => this.snackBar.open('Error saving your rating.', 'Close', { duration: 3000 })
    });
  }

  openReviewDialog(reviewToEdit?: AlbumReview): void {
    const dialogRef = this.dialog.open(ReviewDialogComponent, {
      width: '600px',
      data: { existingText: reviewToEdit?.text }
    });

    dialogRef.afterClosed().subscribe(resultText => {
      if (resultText !== undefined) {
        const request = { albumId: this.albumId, text: resultText };
        const apiCall = reviewToEdit
          ? this.apiService.updateReview(reviewToEdit.id, request)
          : this.apiService.createReview(request);

        apiCall.subscribe({
          next: () => {
            this.snackBar.open('Review saved successfully!', 'Close', { duration: 3000 });
            this.loadAlbumDetails();
          },
          error: (err) => this.snackBar.open('Error saving the review.', 'Close', { duration: 3000 })
        });
      }
    });
  }

  onDeleteReview(reviewId: number): void {
    if (confirm('Are you sure you want to delete this review?')) {
      this.apiService.deleteReview(reviewId).subscribe({
        next: () => {
          this.snackBar.open('Review deleted successfully!', 'Close', { duration: 3000 });
          this.loadAlbumDetails();
        },
        error: (err) => this.snackBar.open('Error deleting the review.', 'Close', { duration: 3000 })
      });
    }
  }

  getAlbumCover(albumDetails: AlbumDetails | null): string {
    return albumDetails?.deezerDetails.cover_xl
      || albumDetails?.deezerDetails?.cover_medium
      || 'https://placehold.co/300x300?text=No+Image';
  }

  getScoreColorClass(score: number | null | undefined): string {
    if (score === null || score === undefined) return '';
    if (score >= 8.0) return 'score-high';
    if (score >= 5.0) return 'score-medium';
    return 'score-low';
  }

  getTrackArtists(track: DeezerTrack): string {
    if (track?.contributors?.length > 0) {
      const mainArtists = track.contributors
        .filter(c => c.role === 'Main' || c.role === 'Featured')
        .map(a => a.name)
        .join(', ');
      return mainArtists;
    }
    return track?.artist?.name || '';
  }
}

