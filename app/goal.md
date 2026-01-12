1. Product Overview
The product is a mobile‑first Android app that connects to a single wearable / health data source (e.g., Google Fit / Health Connect), estimates one wellness risk (choose either sleep disruption or fatigue), and presents interpretable, privacy‑aware insights with basic goals and notifications.
​

Goals

Transform raw wearable metrics into simple, actionable wellness insights.
​

Provide transparent AI explanations instead of black‑box scores.
​

Maintain strong privacy with pseudonymous identifiers and minimal data retention.
​

Non‑Goals (out of scope)

Clinical diagnosis or medical advice.
​

Multi‑wearable federation, formal HIPAA/GDPR compliance, or large‑scale enterprise deployment.
​

2. User Personas & Use Cases
Primary persona: Busy knowledge worker / grad student

Uses smartwatch or phone sensors.

Wants quick view of current wellness risk and simple recommendations, not raw charts.
​

Core use cases

Check today’s wellness risk and why it is high/low.
​

Understand which behaviors (sleep, steps, HR) contributed to current risk.
​

Set simple goals (sleep hours, steps) and configure reminders.
​

Control data sharing and delete their data if desired.
​

3. High‑Level Feature List
Onboarding & consent.

Wearable / health data connection.

Background data sync and preprocessing.

Risk prediction & explanation (single domain).

Home “Today” dashboard.

Insights history & explanations.

Goals & notifications.

Settings & privacy controls.

(Optional) In‑app pilot study feedback screen.
​

4. Screen‑by‑Screen UI and Functional Details
4.1 Onboarding & Consent Flow
Screens

Splash

Intro carousel (2–3 pages)

Consent screen

Connect data source

Basic profile (optional, non‑identifying)

UI Requirements

Splash:

App name and simple tagline (“Predictive Wellness Insights, Not Just Numbers”).
​

Intro carousel pages:

Page 1: “What this app does” – short text + illustration (e.g., converts wearable data into early warnings for fatigue/sleep issues).
​

Page 2: “How your data is used” – bullets on data minimization, pseudonymous IDs, and no sharing without consent.
​

Page 3: “Not medical advice” disclaimer.
​

“Skip” and “Next” buttons.

Consent screen:

Scrollable text area with consent summary.

Checkbox “I agree to participate and share my data as described”.
​

Primary button “Continue” disabled until checkbox checked.

Connect data source:

Explanation: “We need access to steps, sleep, and heart rate to compute your wellness risk.”
​

Buttons: “Connect Google Fit / Health Connect / Wearable”.

Status chip: Connected / Not Connected.

Profile (optional):

Non‑identifying fields like age range, general sleep schedule; skip allowed.

Functional Requirements

FR‑O1: App MUST store a boolean consent flag per user locally and on backend before any data upload.
​

FR‑O2: If consent is not granted, app MUST restrict to a view‑only demo mode with mock data.
​

FR‑O3: Data source connection MUST use secure OAuth or system APIs (Health Connect / Google Fit).
​

FR‑O4: On first launch after consent, app triggers initial historical data sync (e.g., last 7–14 days).
​

4.2 Home – Today Dashboard
Screen name: Today

UI Layout

AppBar:

Title: “Today”.

Right icon: Settings.

Header:

Greeting: “Good morning, [First Name or generic]”.

Date.

Current wellness card:

Large risk label: “Low / Medium / High [Sleep Disruption or Fatigue] Risk”.
​

Color band (green/yellow/red).

Risk score (optional numeric / 0–100).

One‑line explanation snippet, e.g., “Short sleep for 2 nights increased your risk.”
​

Metrics row (horizontal cards):

Card 1: Steps today vs goal (progress bar).

Card 2: Sleep duration last night vs target.

Card 3: Resting HR today vs baseline (arrow up/down).
​

Trend preview:

Mini chart (7‑day risk trend) or list of last 3 days with risk levels.
​

Button: “View detailed insights” → Insights screen.

Functional Requirements

FR‑H1: Home MUST display the latest available prediction and its risk level mapped to discrete buckets.
​

FR‑H2: App MUST fetch latest metrics from local cache; if stale or missing, trigger background sync.
​

FR‑H3: When new data (e.g., new day of steps/sleep) is available, app or backend MUST run inference within 5 seconds for the latest risk estimate.
​

FR‑H4: If no real data yet (e.g., just installed), show placeholder state and prompt user to keep wearable on for at least one full day.
​

4.3 Insights & Explanations
Screen name: Insights

UI Layout

AppBar:

Title: “Insights”.

Filters:

Segmented control: “7 days / 14 days / 30 days”.

Summary section:

Average risk over selected period.

Number of high‑risk days.

List of daily cards (reverse chronological):

Date + risk level chip.

Short explanation (up to 2 lines).

Top contributing signals list (3 items max):

“Sleep duration lower than your usual.”

“Resting HR higher than baseline.”

“Low activity during daytime.”
​

Tap card → Daily detail screen.

Daily detail screen

Header: date + risk label + color.

Section: “Why this risk level?”

Text explanation: 2–4 sentences produced from feature importances.
​

Section: “Key signals”

Small metrics table or bullet list: feature name, value, comparison (↑/↓ vs baseline).

Section: “Simple recommendations”

2–3 bullet tips, rule‑based on the signals (e.g., if sleep < target, suggest earlier bedtime).
​

Link: “How this model works” → Model info screen.

Model info screen

Short paragraphs describing:

Model type (e.g., “lightweight deep learning model on wearable timeseries”).
​

Data sources used (steps, HR, sleep).
​

High‑level training and evaluation summary (non‑technical).
​

Limitations and disclaimer.
​

Functional Requirements

FR‑I1: App MUST fetch historical daily predictions and feature importances from backend or local store.
​

FR‑I2: For each prediction, system MUST expose a short natural‑language explanation in UI.
​

FR‑I3: App MUST support at least 7–30 days of history for pilot users.
​

FR‑I4: Explanations MUST be consistent (same phrasing for same feature pattern) to improve interpretability.
​

4.4 Goals & Notifications
Screen name: Goals

UI Layout

AppBar: “Goals & Reminders”.

Section: “Your goals”

Card: Daily steps goal (e.g., slider / number picker).

Card: Sleep duration target (e.g., 7–9 hours selector).
​

Section: “Reminders”

Toggle: “Daily check‑in reminder”.

Time picker for reminder (e.g., 9:00 PM).

Optional toggle: “Only remind me on high‑risk days”.

Section: “Progress snapshot”

Text: “You met your sleep goal on X of last 7 days.”
​

Functional Requirements

FR‑G1: App MUST allow users to set/change step and sleep targets with reasonable ranges.
​

FR‑G2: Goals MUST be stored locally and synced to backend for inclusion in explanations if needed.
​

FR‑G3: App MUST schedule local notifications using WorkManager / AlarmManager according to configured reminder time.
​

FR‑G4: When “Only on high‑risk days” is enabled, notification logic MUST check risk for that day before firing.
​

4.5 Settings & Privacy
Screen name: Settings

UI Layout

Sections:

Account & data

Data source

Privacy & research

About

Account & data:

Display pseudonymous user ID (non‑editable).
​

Button: “Export my data” (optional for dev build).

Button: “Delete my data from this device”.

Data source:

Connection status chip (Connected/Not connected).

Button: “Reconnect wearable”.

Button: “Disconnect”.

Privacy & research:

Toggle: “Allow anonymized usage data for research study”.
​

Subtitle: Short explanation of what is logged.

Link: “View data usage policy” → open in‑app page.

About:

App version.

Credits (university, advisor).

Legal disclaimers.
​

Functional Requirements

FR‑S1: App MUST support disconnecting the wearable; after disconnecting, no new data is fetched.
​

FR‑S2: “Delete my data from this device” MUST clear local DB and cached predictions; next launch should appear as fresh install (but may re‑pull server data if not also deleted remotely).
​

FR‑S3: “Export my data” if implemented, MUST anonymize export and exclude direct identifiers.
​

FR‑S4: Research opt‑in toggle MUST be logged server‑side to respect user choice for interaction logging.
​

4.6 Optional: Pilot Study Feedback
Screen name: Feedback (or “Study Feedback”; accessible via Settings or debug menu)

UI Layout

Short intro text: purpose of feedback.
​

5–10 Likert items:

“The app was easy to use.”

“I understood why the app gave a particular risk level.”

“The recommendations were helpful.”
​

1–2 free‑text fields:

“What did you like?”

“What confused you or needs improvement?”
​

Submit button.

Functional Requirements

FR‑F1: Responses MUST be stored with pseudonymous user ID and timestamp.
​

FR‑F2: Feedback data MUST be clearly separated from operational logs for later analysis.
​

5. Data & Model Behavior Requirements
Data Sources

Single primary wearable / health ecosystem (e.g., Health Connect / Google Fit).
​

Metrics: steps, heart rate, sleep duration / sleep stages (depending on availability).
​

Backend / Processing Behavior

FR‑D1: System MUST ingest historical and periodic updates from the chosen platform via secure, authenticated APIs.
​

FR‑D2: Data MUST be stored using pseudonymous IDs; direct identifiers avoided where not necessary.
​

FR‑D3: Prediction pipeline MUST aggregate data into daily windows (e.g., previous 24 hours) and produce one risk score per day.
​

FR‑D4: Model MUST be a lightweight deep model or similar (e.g., CNN/LSTM or small transformer) with offline evaluation using standard metrics (F1, ROC‑AUC).
​

FR‑D5: System MUST compute feature importances or contribution signals for each prediction, to drive explanations.
​

6. Non‑Functional Requirements (App‑Facing)
Performance:

Inference completion ≤ 5 seconds from new data arrival.
​

UI screens should load in < 1 second when data cached.

Security & privacy:

All network calls over HTTPS/TLS.
​

No hard‑coded secrets in the client; use secure storage for tokens.
​

Scalability (pilot):

Stable for 50–200 users, 20 concurrent sessions.
​

Usability:

Aim for SUS ≥ 70 from 5–15 pilot users.
​

Maintainability:

Clear module separation (data, domain, UI), basic test coverage on core logic.
​

7. Navigation Map
Bottom nav:

Today

Insights

Goals

Settings:

From top‑right icon on Today.

Onboarding flow shown only on first run or when user resets local data.