import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { AlbumDetails } from '../../models/album-details.model';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';

import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { StarRatingComponent } from '../../components/star-rating/star-rating.component';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-album-details-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatTabsModule,
    MatIconModule,
    MatListModule,
    StarRatingComponent,
    MatSnackBarModule,
    MatButtonModule
  ],
  templateUrl: './album-details-page.component.html',
  styleUrls: ['./album-details-page.component.scss']
})
export class AlbumDetailsPageComponent implements OnInit {
  albumDetails$!: Observable<AlbumDetails>;
  albumId!: string;

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private snackBar: MatSnackBar,
    public authService: AuthService,
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      console.warn('ID do álbum não fornecido!');
      return;
    }
    this.albumId = id;
    this.loadAlbumDetails();
  }

  loadAlbumDetails(): void {
    this.albumDetails$ = this.apiService.getAlbumDetails(this.albumId);
  }

  onRatingChanged(newRating: number): void {
    this.apiService.rateAlbumOrTrack({ albumId: this.albumId, rating: newRating }).subscribe({
      next: () => {
        this.showMessage('A sua nota foi salva com sucesso!');
        this.loadAlbumDetails();
      },
      error: (err) => {
        this.showMessage('Erro ao salvar a nota. Por favor, tente novamente.');
        console.error(err);
      }
    });
  }

  private showMessage(msg: string): void {
    this.snackBar.open(msg, 'Fechar', { duration: 3000 });
  }

  getAlbumCover(albumDetails: AlbumDetails): string {
    return albumDetails.spotifyDetails?.images?.[0]?.url
      ?? 'https://placehold.co/300x300?text=No+Image';
  }
}
