# YogaAI

YogaAI is a modern Android application built with **Jetpack Compose** that helps users practice Yoga, track their progress, and view instructional videos. It leverages **AI** (via local or remote models, future scope) to personalize the yoga experience.

## Features

The application is structured around key feature components:

-   **Home**: Main dashboard for accessing daily practices, recommendations, and quick actions.
-   **Classes**: Browse and join various yoga classes categorized by difficulty and style.
-   **Video Player**: Integrated player for watching high-quality yoga instruction videos.
-   **Progress**: Track your daily streaks, calories burned, and overall yoga journey statistics.
-   **Profile**: Manage your user profile, settings, and preferences.

## Tech Stack

-   **Language**: [Kotlin](https://kotlinlang.org/)
-   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
-   **Architecture**: Clean Architecture with Feature Modules (Single Module Pattern)
-   **Dependency Injection**: [Koin](https://insert-koin.io/)
-   **Network**: [Ktor](https://ktor.io/)
-   **Database**: [Room](https://developer.android.com/training/data-storage/room)
-   **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

## Project Structure

The project follows a standard multi-module structure, optimized for feature cohesion:

-   **:app**: The main application module that wires everything together.
-   **:features**: Contains the UI and domain logic for all features, organized by package (`home`, `classes`, etc.).
-   **:coreNetwork**: Shared network layer implementation using Ktor.
-   **:coreDB**: Shared local database and storage layer using Room.

## Setup & Build

1.  Clone the repository.
2.  Open in Android Studio (Ladybug or newer recommended).
3.  Sync Gradle project.
4.  Run on an emulator or device (Min SDK 24).

## Design

YogaAI uses **Material 3 Expressive Design**, focusing on:
-   **Vibrant & Calm Colors**: A palette inspired by nature and energy.
-   **Expressive Shapes**: Rounded, organic shapes that feel friendly and approachable.
-   **Motion**: Smooth transitions and animations (in progress).
