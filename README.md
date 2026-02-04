# YogaAI

YogaAI is a modern Android application built with **Jetpack Compose** that helps users practice Yoga, track their progress, and view instructional videos. It leverages **AI** (via local or remote models) to personalize the yoga experience.

## ✨ Features

- **Home**: Main dashboard for accessing daily practices, health metrics, and recommendations.
- **Classes**: Browse and join various yoga classes categorized by difficulty and style.
- **Health Tracking**: Integrated with **Health Connect** to track daily streaks, calories burned, and steps.
- **Yoga Detector**: Real-time pose detection using the camera to analyze yoga poses.
- **Profile**: Manage your user profile, settings, and health permissions.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: Clean Architecture with Feature Modules
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Network**: [Ktor](https://ktor.io/)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- **Health Data**: [Health Connect SDK](https://developer.android.com/health-and-fitness/guides/health-connect)
- **Storage**: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)
- **Logging**: [Timber](https://github.com/JakeWharton/timber)
- **AI/ML**: [MediaPipe](https://developers.google.com/mediapipe) (Vision & GenAI)
- **Camera**: [CameraX](https://developer.android.com/media/camera/camerax)
- **Analytics/Crash Reporting**: [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics)

## 📁 Project Structure

The project follows a standard multi-module structure for better scalability:

- **:app**: The main entry point that wires all features and core modules together.
- **:features**: Contains all UI components and business logic for features (Home, Profile, etc.).
- **:core**: Shared infrastructure, including networking, database, models, and repositories.

## 🚀 Setup & Build

1. Clone the repository.
2. Open in Android Studio (Ladybug or newer recommended).
3. Sync Gradle project.
4. Run on an emulator or device (Min SDK 24).

## 🎨 Design

YogaAI uses **Material 3 Expressive Design**, focusing on:
- **Vibrant & Calm Colors**: A palette inspired by nature and energy.
- **Expressive Shapes**: Rounded, organic shapes that feel friendly.
- **Responsive Layouts**: Optimized for various screen sizes including tablets.
