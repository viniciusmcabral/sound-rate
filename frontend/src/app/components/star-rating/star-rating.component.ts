import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-star-rating',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.scss',
})
export class StarRatingComponent {
  @Input() rating: number = 0;
  @Input() readonly: boolean = false;

  @Output() ratingChange = new EventEmitter<number>();

  maxRating: number = 5;
  hoverRating: number = 0;

  get stars(): number[] {
    return Array.from({ length: this.maxRating }, (_, i) => i + 1);
  }

  rate(rating: number): void {
    if (this.readonly) return;
    this.rating = rating;
    this.ratingChange.emit(rating);
  }

  setHoverRating(rating: number): void {
    if (this.readonly) return;
    this.hoverRating = rating;
  }

  clearHoverRating(): void {
    this.hoverRating = 0;
  }

  trackByIndex(index: number): number {
    return index;
  }
}
