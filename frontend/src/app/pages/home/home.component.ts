import { Component } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { SpotifyAlbum } from '../../models/spotify.model';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AlbumCardComponent } from '../../components/album-card/album-card.component';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    AlbumCardComponent,
    MatIconModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})

export class HomePageComponent {
  searchControl = new FormControl('');
  albums$: Observable<SpotifyAlbum[]>;

  constructor(private apiService: ApiService) {
    this.albums$ = this.searchControl.valueChanges.pipe(
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
