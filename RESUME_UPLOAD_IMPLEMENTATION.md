# Resume Upload Feature Implementation

## Overview
This feature allows users to upload their resume as a PDF file (max 1MB) directly from the profile screen. The implementation follows the established architectural patterns in the app and includes robust error handling for platform-specific issues.

## Architecture Components

### 1. BackendClient Enhancement
- **File**: `lib/util/http/client.dart`
- **New Method**: `upload(String path, String filePath, String uploadKey)`
- **Purpose**: Handles multipart file uploads with authentication headers

### 2. ResumeUploadDataSource
- **File**: `lib/features/profile/datasource/resume_upload_datasource.dart`
- **Purpose**: API communication layer for resume upload
- **Endpoint**: Uses `Constants.uploadResume` (/api/resume/upload)
- **Pattern**: Follows BackendClient pattern with GetIt injection

### 3. ResumeUploadController
- **File**: `lib/features/profile/controller/resume_upload_controller.dart`
- **Purpose**: State management for upload process
- **Features**: 
  - Loading states with WidgetsBinding safety
  - File validation (PDF only, max 1MB)
  - Error and success message handling

### 4. ResumeUploadButton Widget
- **File**: `lib/features/profile/widget/resume_upload_button.dart`
- **Purpose**: Reusable UI component for file upload
- **Features**:
  - File picker integration with error handling
  - Loading indicators
  - Error/success message display
  - Theme consistency with app design
  - **Platform Exception Handling**: Graceful handling of MissingPluginException and PlatformException

## Dependencies Added
- `file_picker: ^6.1.1` - For PDF file selection (with fallback handling)
- `path_provider: ^2.1.4` - For file system operations

## Dependency Injection Setup
- **ResumeUploadDataSource**: Registered in GetIt container
- **ResumeUploadController**: Added to Provider MultiProvider in main.dart

## File Upload Specifications
- **File Type**: PDF only
- **Size Limit**: 1MB maximum
- **Upload Key**: "file" (as specified in requirements)
- **Authentication**: Bearer token automatically included

## Error Handling & Platform Support

### Plugin Exception Handling
The implementation includes comprehensive error handling for common plugin issues:

1. **MissingPluginException**: 
   - Shows user-friendly dialog explaining the issue
   - Provides development guidance for resolution
   - Graceful fallback without app crashes

2. **PlatformException**: 
   - Displays platform-specific error messages
   - Handles permission and configuration issues

3. **File System Errors**:
   - Validates file paths and existence
   - Handles null path scenarios

### Troubleshooting Common Issues

#### File Picker Not Working
If you encounter "MissingPluginException" or "Method not found" errors:

1. **Clean and Rebuild**:
   ```bash
   flutter clean
   flutter pub get
   flutter run
   ```

2. **Platform Support**: Ensure you're running on a supported platform (Android/iOS for mobile, web for browser)

3. **Hot Restart**: Use hot restart (not hot reload) after adding new plugins

4. **Plugin Registration**: The plugin should auto-register, but if issues persist, check platform-specific configurations

## Usage
The upload button is integrated into the UserProfileScreen and provides:
1. File picker for PDF selection
2. Automatic file validation
3. Visual feedback during upload
4. Success/error message display
5. Graceful error handling for platform issues

## Error Handling
- File size validation (1MB limit)
- File type validation (PDF only)
- Network error handling
- API response error handling
- Platform exception handling
- Plugin availability checking
- User-friendly error messages

## Integration with Profile Screen
The upload button is positioned in the profile screen below the user information, maintaining the app's visual hierarchy and theme consistency.

## Development Notes
- The file_picker plugin may show warnings about platform implementations during `flutter pub get` - these are normal and don't affect functionality
- For production deployment, ensure platform-specific permissions are configured
- The implementation gracefully degrades when file picker is unavailable
