# Personalized Job Hunter

A Flutter application designed to help users find personalized job opportunities.

## Environment Requirements

To run this project, ensure you have the following components installed with the specified versions:

### Core Requirements

- **Flutter**: 3.22.1 or later
  - Dart SDK: 3.4.1 or later
  - Flutter SDK: 3.22.1 or later
- **Java**: JDK 17 or later
  - Current version: 17.0.12 (LTS)
- **Gradle**: 8.13 or compatible
- **Android SDK**:
  - Platform: android-35
  - Build tools: 30.0.3
  - Min SDK Version: 16
  - Target SDK Version: 28

### IDE Recommendations

- **VS Code** with Flutter extension (3.110.0+)
- **IntelliJ IDEA** with Flutter and Dart plugins
- **Android Studio** (optional but recommended)

## Project Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd jobhunter-backend
```

### 2. Install Dependencies

```bash
flutter pub get
```

### 3. Firebase Configuration

This project uses Firebase for authentication, analytics, and messaging. Ensure you have:

- Set up a Firebase project
- Added your Android app to the Firebase project
- Downloaded and placed the `google-services.json` file in the `android/app/` directory

### 4. Android Setup

The application is configured with signing keys for release builds. For development:

```bash
# Create key.properties file in android/ directory if not present
echo "storeFile=key.jks
storePassword=password
keyAlias=key
keyPassword=password" > android/key.properties
```

### 5. Run the Application

```bash
flutter run
```

## Features

- User authentication (Google, Facebook)
- Personalized job recommendations
- Job search and filtering
- Web view for job applications
- Push notifications for new job matches
- User profile management
- Subscription options

## Project Structure

- `lib/`: Contains all Dart code
  - `features/`: Feature-based architecture with separate modules
  - `util/`: Utility functions and helpers
- `assets/`: Contains all images and static resources
- `android/`: Android-specific configuration
- `web/`: Web platform support files

## Dependencies

This project uses the following key packages:

- Provider (^6.1.2) for state management
- Firebase packages for backend integration
- Google and Facebook sign-in for authentication
- Flutter InAppWebView (^6.0.0) for web content rendering
- Curved Navigation Bar (^1.0.6) for UI navigation
- HTTP (^1.2.2) for API communication
- Flutter Local Notifications (^18.0.1) for push notifications

## Building for Release

### Android

```bash
flutter build apk --release
# OR
flutter build appbundle --release
```

## Troubleshooting

If you encounter any issues:

1. Make sure your Flutter SDK is up to date: `flutter upgrade`
2. Check Android SDK setup: `flutter doctor -v`
3. Clear build cache if needed: `flutter clean`

## License

[Include license information here]
