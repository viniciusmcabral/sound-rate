import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { UserProfilePageComponent } from './pages/user-profile-page/user-profile-page.component';
import { AlbumDetailsPageComponent } from './pages/album-details-page/album-details-page.component';
import { SettingsPageComponent } from './pages/settings-page/settings-page.component';
import { authGuard } from './guards/auth.guard';
import { ListenLaterPageComponent } from './pages/listen-later-page/listen-later-page.component';
import { ArtistDetailsPageComponent } from './pages/artist-details-page/artist-details-page.component';
import { ForgetPasswordPageComponent } from './pages/forget-password-page/forget-password-page.component';
import { ResetPasswordPageComponent } from './pages/reset-password-page/reset-password-page.component';

export const routes: Routes = [
    { path: '', component: HomePageComponent },
    { path: 'login', component: LoginPageComponent },
    { path: 'register', component: RegisterPageComponent },
    { path: 'forgot-password', component: ForgetPasswordPageComponent },
    { path: 'reset-password', component: ResetPasswordPageComponent },
    { path: 'user/:username', component: UserProfilePageComponent },
    { path: 'album/:id', component: AlbumDetailsPageComponent },
    { path: 'artist/:id', component: ArtistDetailsPageComponent },
    { path: 'settings', component: SettingsPageComponent, canActivate: [authGuard] },
    { path: 'listen-later', component: ListenLaterPageComponent, canActivate: [authGuard] }
];