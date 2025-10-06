# AthleTrack

AthleTrack is a mobile-first training, attendance, and events companion designed for the Mobile Applications Development course at ESTGA. The project pairs an Android app built with Jetpack Compose and a Kotlin Spring Boot backend to help coaches manage squads, sessions, QR based check-ins, and shared calendars for their athletes.

## Core Capabilities
- **Athlete experience**: daily timetable, QR attendance scanning via CameraX + ML Kit, upcoming events, and persistent login via DataStore.
- **Coach experience**: dashboard for today and tomorrow classes, QR codes per training session, bulk attendance management, roster management with password guarded actions, and calendar driven event planning.
- **Calendar and events**: Compose based month view with modal details, local calendar export, role aware creation and deletion guarded by password confirmation, and modality filtering handled on the backend.
- **Integration layer**: Retrofit services for users, trainings, events, modalities, and presences hitting the Spring Boot API running either locally or on Render.

## Architecture Highlights
- `app/` Android module using Jetpack Compose Material 3, Navigation Compose, CameraX, ML Kit Barcode Scanning, ZXing QR generation, Retrofit, Cronet, and DataStore Preferences.
- `backend/` Kotlin Spring Boot 3 service exposing REST endpoints backed by Spring Data JPA, PostgreSQL, BCrypt password hashing, and scheduled cleanup jobs.
- Retrofit base URL defaults to the hosted API (`https://athletrack-backend.onrender.com`); a commented local URL (`http://10.0.2.2:8080/api/`) is available for emulator testing.
- Shared DTOs and ViewModels encapsulate domain workflows such as training creation, presence collection, and event orchestration; unit tests cover the Android view models and Spring controllers.

## Project Layout
- `app/src/main` - Compose UI, navigation graph, feature screens for athletes and coaches, API layer, and theming utilities.
- `app/src/test` - Unit tests for view models (login, home, calendar, attendance, event creation).
- `backend/src/main` - Spring Boot application, REST controllers (`user`, `treinos`, `presencas`, `eventos`, `modalidade`), data repositories, entities, and security configuration.
- `backend/src/test` - Controller-level tests using Spring Boot Test and Mockito.
- `AthleTrack.apk` - Prebuilt debug APK that can be sideloaded on Android devices (requires enabling installs from unknown sources).
- `AthleTrack_Manual_de_Utilizacao.pdf` and `Relatorio AthleTrack.pdf` - Portuguese documentation and course report for deeper background.

## Prerequisites
- Android Studio Iguana (AGP 8.1+) with the Kotlin Compose plugin and Android SDK 26 through 35.
- Java 21 and Gradle Wrapper (bundled) for the backend service.
- PostgreSQL 14+ (hosted instance or local database) with credentials supplied via environment variables.
- Optional: An Android device with Android 8.0+ or an emulator image created from Android Studio.

## Running the Mobile App
### Option 1: Install the packaged APK
1. Transfer `AthleTrack.apk` to your Android device (USB, ADB, or cloud drive).
2. On the device, allow installs from unknown sources if prompted and open the APK to install AthleTrack.
3. Launch the app, sign in with a valid membership ID and password from your backend, and start scanning QR codes or browsing sessions.

### Option 2: Build the APK from source
1. Open **only** the `app/` directory in Android Studio (to avoid Gradle conflicts with the `backend` module). Alternatively mark `backend/` as a plain folder in the IDE.
2. Let Gradle sync, resolve dependencies, and build Compose previews.
3. Connect a device or start an emulator.
4. Build and deploy:
   - For quick installs run `Run > Run 'app'`.
   - To produce a distributable APK execute `./gradlew assembleDebug` (or `.\gradlew.bat assembleDebug` on Windows) inside `app/`.
   - For a signed release APK use `Build > Generate Signed Bundle / APK` and provide your keystore.

## Running the Backend
1. Copy `backend/src/main/resources/.env` to the project root (or export the variables in your shell) and update `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` with your PostgreSQL credentials. **Rotate or replace the committed sample values before deploying.**
2. From the `backend/` directory run the service:
   ```bash
   ./gradlew bootRun
   ```
   (Use `gradlew.bat bootRun` on Windows.) The API listens on `http://localhost:8080` by default.
3. To verify the API and database, hit endpoints such as `POST /api/user/login`, `POST /api/treinos/hoje`, or `POST /api/eventos/listar` with JSON payloads matching the DTOs in `backend/dto`.
4. Automated tests can be executed with:
   ```bash
   ./gradlew test
   ```
5. The service schedules a weekly cleanup (`PresencaCleanup`) that purges attendance on Sundays at 23:59; disable or adjust the cron if that does not suit your environment.

## Connecting the App to Your Backend
- When using the hosted Render instance no changes are required.
- For a local backend, edit `app/src/main/java/estga/dadm/athletrack/api/RetrofitClient.kt` and uncomment the `http://10.0.2.2:8080/api/` base URL. This special address lets the Android emulator reach your machine.
- On physical devices connected to the same network, replace the base URL with your computer IP (for example `http://192.168.1.50:8080/`) and ensure the port is exposed.

## Testing
- **Android unit tests:** run `./gradlew testDebugUnitTest` (or the IDE gutter actions) to execute the suite in `app/src/test`, covering login, calendar logic, attendance, and event workflows.
- **Backend tests:** run `./gradlew test` inside `backend/` to execute the controller tests for trainings, events, modalities, and presences.

## Troubleshooting Tips
- If Android Studio imports the backend module and Gradle sync fails, close the project and reopen `app/` directly or mark `backend/` as excluded from Gradle settings.
- QR scanning relies on the camera permission. If you deny it, use the bottom sheet menu on the athlete home screen to retry and re-request permissions.
- Presence registration requires the athlete to be enrolled in the session modality; otherwise the backend returns `403` and the app shows an error toast.
- When running locally, make sure your database accepts SSL or adjust the JDBC URL accordingly.

## Documentation and Next Steps
- Consult `AthleTrack_Manual_de_Utilizacao.pdf` for a Portuguese end-user guide, including QR attendance flows.
- `Relatorio AthleTrack.pdf` captures the academic context, architectural decisions, and evaluation criteria for the project.
- Consider externalising secrets (Retrofit key, database credentials) into environment variables or encrypted stores before publishing the repository.
- Future enhancements the team discussed include push notifications for upcoming events, offline attendance caching, and tighter CI around the two Gradle builds.
