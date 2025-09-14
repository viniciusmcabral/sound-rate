import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { DeezerAlbum } from '../../models/deezer.model';
import { Page } from '../../models/page.model';
import { BehaviorSubject } from 'rxjs';
import { PageEvent } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AlbumCardComponent } from '../../components/album-card/album-card.component';

@Component({
  selector: 'app-listen-later-page',
  standalone: true,
  imports: [CommonModule, MatPaginatorModule, MatProgressSpinnerModule, AlbumCardComponent],
  templateUrl: './listen-later-page.component.html',
  styleUrl: './listen-later-page.component.scss'
})
export class ListenLaterPageComponent implements OnInit {
  private albumsSubject = new BehaviorSubject<DeezerAlbum[]>([]);
  albums$ = this.albumsSubject.asObservable();

  totalAlbums = 0;
  pageSize = 20;
  currentPage = 0;
  isLoading = true;

  constructor(private apiService: ApiService) { }

  ngOnInit(): void {
    this.loadListenLaterPage();
  }

  loadListenLaterPage(): void {
    this.isLoading = true;
    this.apiService.getListenLaterList(this.currentPage, this.pageSize).subscribe(page => {
      this.albumsSubject.next(page.content);
      this.totalAlbums = page.totalElements;
      this.isLoading = false;
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadListenLaterPage();
  }
}