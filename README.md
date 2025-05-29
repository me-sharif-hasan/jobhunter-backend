# Personalized Job Hunter

A Flutter application designed to help users find personalized job opportunities.

## Environment Requirements

To run this project, ensure you have the following components installed with the specified versions:

### Core Requirements

- **Flutter**: 3.29.3 (Current stable version)
  - Dart SDK: 3.7.2 (included with Flutter)
  - Flutter SDK: 3.29.3 on stable channel
- **Java**: Java SE 24.0.1+9-30 (Current system version)
  - Minimum required: JDK 17 or later
  - Required for Android compilation and desugaring features
- **Gradle**: 8.14 (Updated from 8.13)
- **Android SDK**: 35.0.0
  - Platform: android-35-2
  - Build tools: 35.0.0
  - Min SDK Version: 16
  - Target SDK Version: 28
- **Android Gradle Plugin**: 8.10.0 (Updated from 7.3.0)
- **Kotlin**: 2.0.21 (Updated from 1.7.10)
- **Windows**: Windows 11 Home Single Language 64-bit (24H2, 2009)

### IDE Recommendations

- **VS Code** (version 1.100.2) with Flutter extension (3.110.0) ✅ **Currently Installed**
- **IntelliJ IDEA Ultimate Edition** (version 2025.1) ✅ **Currently Installed**
  - Flutter plugin available from JetBrains Plugin Repository
  - Dart plugin available from JetBrains Plugin Repository
- **Android Studio** (optional but recommended) ⚠️ **Not Currently Installed**
  - Download from: https://developer.android.com/studio/index.html

### Development Environment Status
- ✅ Flutter SDK properly configured
- ✅ Android toolchain ready (SDK 35.0.0)
- ✅ Web development ready (Chrome available)
- ✅ VS Code with Flutter extension
- ✅ IntelliJ IDEA available
- ⚠️ Visual Studio not installed (only needed for Windows app development)
- ⚠️ Android Studio not installed (optional for Flutter development)

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

## Recent Build System Updates (May 2025)

### Android Build Modernization

To resolve compatibility issues and improve build reliability, the following updates have been implemented:

#### Gradle and Plugin Updates
- **Gradle**: Upgraded from 8.13 to 8.14
- **Android Gradle Plugin**: Updated from 7.3.0 to 8.10.0
- **Kotlin**: Updated from 1.7.10 to 2.0.21

#### Android App Configuration Enhancements
- **Core Library Desugaring**: Added support for Java 8+ APIs on older Android versions
  - Enabled `coreLibraryDesugaringEnabled true`
  - Added dependency: `com.android.tools:desugar_jdk_libs:2.1.5`
- **MultiDex Support**: Added `multiDexEnabled true` to handle apps with more than 65K methods
- **Dependency Management**: Temporarily disabled `flutter_login_facebook` due to compatibility issues

#### Build System Cleanup
- Removed conflicting `.android/` and `.ios/` hidden folders
- Regenerated build artifacts with updated configuration
- Cleaned up stale platform configurations

### Known Issues Resolved
- ✅ Build failures due to outdated Android Gradle Plugin
- ✅ Java 8+ API compatibility on older Android versions
- ✅ Method count exceeded 65K limit (MultiDex)
- ✅ Facebook login dependency conflicts
- ✅ Gradle version compatibility issues

### Breaking Changes
- `flutter_login_facebook` package is temporarily commented out
- Projects may need to regenerate platform folders after updates

## Building for Release

### Android

```bash
# Build APK for release
flutter build apk --release

# Build App Bundle for Google Play Store (recommended)
flutter build appbundle --release
```

### Build Requirements
- Ensure Java 17+ is installed and configured
- Android SDK with platform-tools and build-tools
- Valid signing key configuration in `android/key.properties`
- `google-services.json` file in `android/app/` directory

### CI/CD Pipeline
The project includes a GitHub Actions workflow (`.github/workflows/buildapk.yml`) that:
- Automatically builds APK on pushes to `frontend` branch
- Validates `google-services.json` configuration
- Creates GitHub releases with generated APK files
- Uses Flutter 3.22.1 and JDK 17 for consistent builds
- **Note**: Local environment uses Flutter 3.29.3 and Java SE 24.0.1 (both compatible)

## Troubleshooting

### Common Build Issues

1. **Java Version Issues**:
   ```bash
   # Verify Java version (current system has Java SE 24.0.1)
   java -version
   # Should show: Java(TM) SE Runtime Environment (build 24.0.1+9-30) or later
   # Minimum required: JDK 17+
   ```

2. **Flutter Environment**:
   ```bash
   # Current system: Flutter 3.29.3, Dart 3.7.2
   flutter --version
   
   # Update Flutter SDK if needed
   flutter upgrade
   
   # Check system configuration
   flutter doctor -v
   
   # Clean build cache
   flutter clean
   flutter pub get
   ```

3. **Android Build Failures**:
   ```bash
   # Clean Android build cache
   cd android
   ./gradlew clean
   cd ..
   flutter clean
   flutter pub get
   ```

4. **Gradle Issues**:
   - Ensure Gradle 8.14 is being used
   - Check `android/gradle/wrapper/gradle-wrapper.properties`
   - Verify Android Gradle Plugin version 8.10.0 in `android/settings.gradle`
   - Current Android SDK: 35.0.0 with build-tools 35.0.0

5. **MultiDex Issues**:
   - Ensure `multiDexEnabled true` is set in `android/app/build.gradle`
   - This is required for apps with more than 65K methods

6. **Core Library Desugaring**:
   - Required for Java 8+ API support on older Android versions
   - Verify `coreLibraryDesugaringEnabled true` is configured

### Platform-Specific Issues

#### Android
- Verify `google-services.json` is present in `android/app/`
- Check signing configuration in `android/key.properties`
- Ensure minimum SDK version compatibility

#### Development Environment
- Use VS Code (1.100.2) with Flutter extension (3.110.0) ✅ **Current Setup**
- Use IntelliJ IDEA Ultimate Edition (2025.1) ✅ **Available**
- Android Studio recommended but not required ⚠️ **Not Installed**
- Enable developer options on Android devices for testing
- Configure USB debugging for device testing

#### Windows-Specific Notes
- Windows 11 Home Single Language 64-bit (24H2) ✅ **Current OS**
- Visual Studio not required for Flutter Android development
- Chrome (136.0.7103.114) and Edge (136.0.3240.92) available for web testing

If you encounter persistent issues, try:
1. Delete `.dart_tool/` and `build/` directories
2. Run `flutter pub cache repair`
3. Restart your IDE and development server

## System Compatibility Status

### Current Development Environment ✅
Based on `flutter doctor -v` output (as of May 29, 2025):

```
✅ Flutter 3.29.3 (stable) - COMPATIBLE
✅ Dart 3.7.2 - COMPATIBLE  
✅ Java SE 24.0.1+9-30 - COMPATIBLE (exceeds minimum JDK 17)
✅ Android SDK 35.0.0 - COMPATIBLE
✅ Build Tools 35.0.0 - COMPATIBLE
✅ Windows 11 Home Single Language 64-bit - COMPATIBLE
✅ VS Code 1.100.2 + Flutter Extension 3.110.0 - READY
✅ IntelliJ IDEA Ultimate 2025.1 - READY
✅ Chrome 136.0.7103.114 - WEB READY
✅ Edge 136.0.3240.92 - WEB READY
⚠️  Android Studio - NOT INSTALLED (optional)
⚠️  Visual Studio - NOT INSTALLED (Windows apps only)
```

### Version Compatibility Notes
- **Flutter**: Local version (3.29.3) is newer than CI/CD version (3.22.1) ✅
- **Java**: Local version (24.0.1) is newer than minimum required (17) ✅
- **Android SDK**: Version 35.0.0 is latest stable ✅
- **All Android licenses accepted** ✅

The development environment is fully functional for Flutter Android and Web development.

## License

[Include license information here]
