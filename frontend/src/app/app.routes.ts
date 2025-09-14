import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home/home.component';
import { LoginPageComponent } from './pages/login/login.component';
import { RegisterPageComponent } from './pages/register/register.component';
import { UserProfilePageComponent } from './pages/user-profile-page/user-profile-page.component';
import { AlbumDetailsPageComponent } from './pages/album-details-page/album-details-page.component';
import { SettingsPageComponent } from './pages/settings-page/settings-page.component';
import { authGuard } from './guards/auth.guard';
import { ListenLaterPageComponent } from './pages/listen-later-page/listen-later-page.component';

export const routes: Routes = [
    { path: '', component: HomePageComponent },
    { path: 'login', component: LoginPageComponent },
    { path: 'register', component: RegisterPageComponent },
    { path: 'user/:username', component: UserProfilePageComponent },
    { path: 'album/:id', component: AlbumDetailsPageComponent },
    { path: 'settings', component: SettingsPageComponent, canActivate: [authGuard] },
    { path: 'listen-later', component: ListenLaterPageComponent, canActivate: [authGuard] }
];