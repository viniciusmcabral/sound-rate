import { Component, Input } from '@angular/core';
import { SpotifyAlbum } from '../../models/spotify.model';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-album-card',
  standalone: true,
  imports: [CommonModule, MatCardModule, RouterLink],
  templateUrl: './album-card.component.html',
  styleUrl: './album-card.component.scss'
})

export class AlbumCardComponent {
  @Input() album!: SpotifyAlbum;

  get albumCover(): string {
    if (this.album?.images?.length > 0) {
      return this.album.images[0].url;
    }
    return 'https://placehold.co/300x300?text=No+Image';
  }
}
