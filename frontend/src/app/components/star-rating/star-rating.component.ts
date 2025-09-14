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

  getStarIcon(star: number): string {
    const ratingToShow = this.hoverRating || this.rating;

    if (ratingToShow >= star) {
      return 'star';
    }
    if (ratingToShow >= star - 0.5) {
      return 'star_half';
    }
    return 'star_border';
  }

  rate(star: number, event: MouseEvent): void {
    if (this.readonly) return;

    const target = event.target as HTMLElement;
    const rect = target.getBoundingClientRect();
    const isHalf = (event.clientX - rect.left) < (rect.width / 2);
    const newRating = isHalf ? star - 0.5 : star;
    this.ratingChange.emit(newRating);
  }

  setHoverRating(star: number, event: MouseEvent): void {
    if (this.readonly) return;

    const target = event.target as HTMLElement;
    const rect = target.getBoundingClientRect();
    const isHalf = (event.clientX - rect.left) < (rect.width / 2);
    this.hoverRating = isHalf ? star - 0.5 : star;
  }

  clearHoverRating(): void {
    if (this.readonly) return;
    this.hoverRating = 0;
  }

  trackByIndex(index: number): number {
    return index;
  }
}
