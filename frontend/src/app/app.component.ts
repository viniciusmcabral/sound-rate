import { Component } from '@angular/core';
import { LayoutComponent } from './components/layout/layout.component';
import { AudioPlayerComponent } from './components/audio-player/audio-player.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LayoutComponent, AudioPlayerComponent],
  templateUrl: './app.component.html',
})
export class AppComponent {
  title = 'frontend';
}
