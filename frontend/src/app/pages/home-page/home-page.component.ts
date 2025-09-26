import { Component, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, combineLatest, of } from 'rxjs';
import { map, startWith, debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';
import { SearchResult } from '../../models/search-result.model';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AlbumCardComponent } from '../../components/album-card/album-card.component';
import { ArtistCardComponent } from '../../components/artist-card/artist-card.component';
import { UserCardComponent } from '../../components/user-card/user-card.component';
import { AlbumDashboard } from '../../models/album-details.model';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatIconModule, AlbumCardComponent, ArtistCardComponent, UserCardComponent,
    MatProgressSpinnerModule, MatButtonToggleModule,
  ],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent implements OnInit {
  searchControl = new FormControl('');
  filterControl = new FormControl<'all' | 'album' | 'artist' | 'user'>('all');

  resultsData$!: Observable<{
    isLoading: boolean;
    results: SearchResult[];
    filteredResults: SearchResult[];
    query: string;
  }>;

  highestRatedAlbums$!: Observable<AlbumDashboard[]>;
  isLoading = false;

  constructor(private apiService: ApiService) { }

  ngOnInit(): void {
    this.highestRatedAlbums$ = this.apiService.getHighestRatedAlbums();
    const rawResults$ = this.searchControl.valueChanges.pipe(
      startWith(''),
      debounceTime(400),
      distinctUntilChanged(),
      tap(query => {
        this.isLoading = !!query && query.length >= 3;
        if (this.isLoading) {
          this.filterControl.setValue('all', { emitEvent: false });
        }
      }),
      switchMap(query => {
        if (!query || query.length < 3) {
          return of([]);
        }
        return this.apiService.search(query);
      }),
      tap(() => {
        this.isLoading = false;
      })
    );

    this.resultsData$ = combineLatest({
      results: rawResults$,
      filter: this.filterControl.valueChanges.pipe(startWith('all' as const)),
      query: this.searchControl.valueChanges.pipe(startWith(''))
    }).pipe(
      map(({ results, filter, query }) => {
        const filteredResults = filter === 'all'
          ? results
          : results.filter(result => result.type === filter);
        return { isLoading: this.isLoading, results, filteredResults, query: query || '' };
      })
    );
  }
}