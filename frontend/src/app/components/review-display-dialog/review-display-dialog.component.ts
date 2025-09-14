import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

export interface ReviewDisplayData {
  albumTitle: string;
  reviewText: string;
}

@Component({
  selector: 'app-review-display-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './review-display-dialog.component.html',
})
export class ReviewDisplayDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: ReviewDisplayData) { }
}
