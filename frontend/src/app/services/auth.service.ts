import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthResponse } from '../models/auth.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;

  private apiUrl = '/api/v1/auth';

  constructor(private http: HttpClient, private router: Router) {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<User | null>(storedUser ? JSON.parse(storedUser) : null);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  public isAuthenticated(): boolean {
    return !!this.currentUserValue;
  }

  register(registerData: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, registerData).pipe(
      tap(response => {
        this.handleAuthentication(response);
      })
    );
  }

  login(loginData: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, loginData).pipe(
      tap(response => {
        this.handleAuthentication(response);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('jwt_token');

    this.currentUserSubject.next(null);

    this.router.navigate(['/login']);
  }

  private handleAuthentication(response: AuthResponse): void {
    if (response && response.token && response.user) {
      localStorage.setItem('currentUser', JSON.stringify(response.user));
      localStorage.setItem('jwt_token', response.token);

      this.currentUserSubject.next(response.user);
      this.router.navigate(['/']);
    }
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }
}
