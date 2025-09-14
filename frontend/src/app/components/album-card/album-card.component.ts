import { Component, Input } from '@angular/core';
import { DeezerAlbum } from '../../models/deezer.model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ImageDialogComponent } from '../image-dialog/image-dialog.component';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-album-card',
  standalone: true,
  imports: [CommonModule, RouterLink, MatDialogModule, MatIconModule, MatButtonModule],
  templateUrl: './album-card.component.html',
  styleUrl: './album-card.component.scss'
})
export class AlbumCardComponent {
  @Input() album!: DeezerAlbum;

  constructor(private dialog: MatDialog) { }
  
  getAlbumCover(): string {
    if (this.album?.cover_medium) {
      return this.album.cover_medium;
    }
    return 'https://placehold.co/200x200?text=No+Image';
  }

  openImagePreview(event: MouseEvent): void {
    event.stopPropagation();
    const imageUrl = this.album?.cover_xl || this.getAlbumCover();
    this.dialog.open(ImageDialogComponent, {
      data: { imageUrl: imageUrl },
      panelClass: 'image-dialog-panel'
    });
  }
}

