import { Component } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, startWith } from 'rxjs/operators';
import { DeezerAlbum } from '../../models/deezer.model';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AlbumCardComponent } from '../../components/album-card/album-card.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatIconModule, AlbumCardComponent, MatDialogModule, MatProgressSpinnerModule, MatButtonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomePageComponent {
  searchControl = new FormControl('');
  albums$: Observable<DeezerAlbum[]>;

  constructor(private apiService: ApiService) {
    this.albums$ = this.searchControl.valueChanges.pipe(
      startWith(''),
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => {
        if (query && query.length > 2) {
          return this.apiService.searchAlbums(query);
        }
        return of([]);
      })
    );
  }
}