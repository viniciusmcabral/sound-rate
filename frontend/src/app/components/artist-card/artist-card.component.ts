import { Component, Input } from '@angular/core';
import { DeezerArtist } from '../../models/deezer.model';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-artist-card',
  standalone: true,
  imports: [CommonModule, RouterLink, MatCardModule],
  templateUrl: './artist-card.component.html',
  styleUrl: './artist-card.component.scss'
})
export class ArtistCardComponent {
  @Input() artist!: DeezerArtist;
}