# SoundRate Frontend

This is the Angular frontend for the SoundRate application, a web site for discovering, rating, and reviewing music.

## ✨ Features

- **User Authentication:** Login, Register, and Forgot/Reset Password pages.
- **Dynamic Content:** Homepage with dashboards for highest-rated albums and most active users.
- **Global Search:** A reactive search bar to find albums, artists, and users.
- **Detailed Pages:** Dedicated pages for Album Details, Artist Profiles, and User Profiles.
- **Interactive Components:** Reusable components for star ratings, data grids, and dialogs.
- **Account Management:** A settings page for users to update their profile, password, and avatar.
- **Full Application State Management:** A global `AuthService` using RxJS to manage user authentication state in real-time.

## 🛠️ Tech Stack

- **Framework:** Angular
- **Language:** TypeScript
- **Styling:** SCSS with Angular Material
- **Reactivity:** RxJS
- **Containerization:** Docker & Nginx

## 🚀 Getting Started

### Prerequisites
- Node.js (LTS version recommended)
- npm (Node Package Manager)
- Angular CLI (`npm install -g @angular/cli`)

### Installation & Running Locally

1.  **Navigate to the frontend directory:**
    ```bash
    cd frontend
    ```

2.  **Install dependencies:**
    ```bash
    npm install
    ```

3.  **Run the backend:**
    For the frontend to work correctly, the backend API must be running. Ensure the backend is running (e.g., via Docker Compose) so that the proxy can connect to it.

4.  **Start the development server:**
    ```bash
    ng serve
    ```
    The application will be available at `http://localhost:4200`. The development server uses the `proxy.conf.json` file to automatically redirect API requests from `/api/v1` to the backend at `http://localhost:8080`.

## Build & Test Commands

### Build for Production
To create a production-ready build in the `dist/` folder, run:
```bash
ng build --configuration production