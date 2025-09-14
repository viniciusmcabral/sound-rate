import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AudioService {
    private audio = new Audio();
    private isPlayingSubject = new BehaviorSubject<boolean>(false);
    private currentTrackUrlSubject = new BehaviorSubject<string | null>(null);
    public isPlaying$ = this.isPlayingSubject.asObservable();
    public currentTrackUrl$ = this.currentTrackUrlSubject.asObservable();

    constructor() {
        this.audio.addEventListener('playing', () => this.isPlayingSubject.next(true));
        this.audio.addEventListener('pause', () => this.isPlayingSubject.next(false));
        this.audio.addEventListener('ended', () => {
            this.isPlayingSubject.next(false);
            this.currentTrackUrlSubject.next(null);
        });
    }

    togglePlay(trackUrl: string | null): void {
        if (!trackUrl) {
            console.warn('Attempt to play a null previous URL');
            return;
        }

        if (this.currentTrackUrlSubject.value === trackUrl && !this.audio.paused) {
            this.audio.pause();
        } else {
            console.log('Playing preview:', trackUrl);
            this.audio.src = trackUrl;
            this.currentTrackUrlSubject.next(trackUrl);

            const playPromise = this.audio.play();
            if (playPromise !== undefined) {
                playPromise.catch(error => {
                    console.warn('Audio playback error caught:', error);
                    this.isPlayingSubject.next(false);
                    this.currentTrackUrlSubject.next(null);
                });
            }
        }
    }
}