# YogaAI Design System: "Bento-Playful Minimalist"

This document outlines the core design language, components, and philosophy used in the YogaAI application following the 2026 UI/UX revamp. 

## 🎨 Color Palette
The application uses a vibrant, glassmorphic color palette focused on modern, energetic hues that contrast sharply in Light and Dark modes. The aesthetic relies heavily on alpha transparency to achieve "frosted glass" effects.

### Light Mode
*   **Primary (Mint Green)**: `#A5D6A7` - Used for the main Zen Mascot, primary buttons, and positive feedback highlights.
*   **Secondary (Sky Blue)**: `#64B5F6` - Used for specific mascot emotional states (Meditative), secondary buttons, and active bottom navigation indicators.
*   **Tertiary (Vibrant Coral)**: `#FF8A65` - Used for warnings, alerts, and the "Encouraging" mascot state.
*   **Background (Off-White)**: `#F8F9FA` - A clean, soft white to allow Bento cards to float.
*   **Surface/Bento Cards**: `#FFFFFF` - Crisp white for elevated card backgrounds.

### Dark Mode
*   **Primary (Mint Green)**: `#81C784` - Deepened slightly for contrast against dark backgrounds.
*   **Secondary (Sky Blue)**: `#4FC3F7` - Brightened for legibility.
*   **Accent (Neon Cyan)**: `#00E5FF` - Exclusively used for the real-time Yoga Pose Detector overlay (skeletal landmarks) to provide an unmistakable, futuristic HUD feel.
*   **Background (Deep Charcoal)**: `#121212` - A rich, true-dark background.
*   **Surface/Bento Cards**: `#1E1E1E` - Slightly elevated dark surfaces.

---

## 📐 Typography & Shapes

### Typography
The application relies on a modern Sans-Serif stack (default Material 3 sans-serif, heavily weighted towards Google Fonts metrics like Inter/Nunito).
*   **Headlines**: Uses `FontWeight.ExtraBold` with slightly negative letter spacing (`-0.5.sp`) for a tight, playful, and impactful headers.
*   **Body**: Uses `FontWeight.Medium` with increased line height for maximum readability during active workouts.

### Shapes & Bento Grid
*   The UI is structured around an asymmetrical **Bento Grid**.
*   **Corner Radius**: All primary surfaces (`MaterialTheme.shapes.large`) utilize an aggressive `32.dp` border radius. This ultra-rounded approach defines the "playful" geometry of the app.
*   **Elevation**: Cards use 0dp elevation but rely on subtle tonal surface colors or glassmorphic backgrounds to establish hierarchy.

---

## 🐨 The "Zen Mascot" System
To inject emotional intelligence into the application, the `ZenMascot` composable acts as a digital companion. The mascot changes its color and facial expression based on application state:

1.  **Happy State (Mint Green)**: Smiling, wide-eyed. Appears when daily wellness goals are met, or when a user receives a "Great" score on a yoga pose.
2.  **Meditative State (Sky Blue)**: Eyes closed, serene expression. Accompanied by a 3000ms breathing animation loop. Attached to the top HUD during active yoga pose detection to encourage calmness.
3.  **Encouraging State (Vibrant Coral)**: Smiling but focused. Appears when risk levels are high/medium, goals aren't met, or the user needs motivation.

---

## 🎬 Animation & Motion
The application avoids linear, rigid animations in favor of fluid, physical motion.

*   **Spring Physics**: The custom Bottom Navigation Bar (`YogaBottomBar`) uses Jetpack Compose `spring` physics (`DampingRatioMediumBouncy`, `StiffnessLow`) when a user taps an icon. The icon smoothly expands into a "Pill" shape containing text.
*   **Breathing Elements**: The Mascot components utilize infinitely repeating `tween` animations (`SineEasing` or `FastOutSlowInEasing`) combined with scale transformations to simulate organic "breathing."
*   **Navigation Transitions**: The application's `NavHost` exclusively uses fluid `slideInVertically` (entering from bottom) and `fadeOut` (alpha decay) transitions to prevent jarring instant screen swaps.
