# Android Material 3 Alarm Clock with Challenge System

## Objective
Build a fully functional Android alarm clock application that follows Material Design 3 guidelines and includes an innovative challenge system that allows users to add custom challenges (like math problems) to snooze or stop alarms.

## Core Requirements

### 1. Alarm Clock Functionality
- Create, edit, and delete alarms
- Set alarm time with hour and minute picker
- Enable/disable individual alarms
- Persist alarms to local storage (Room database or SharedPreferences)
- Trigger alarm at specified time with system notification and sound
- Display active alarm in-app UI
- Support multiple alarms

### 2. Material Design 3 Compliance
- Use Material 3 color scheme (dynamic colors when available, fallback to predefined palette)
- Implement Material 3 components:
  - TopAppBar with proper styling
  - FAB (Floating Action Button) for adding alarms
  - Material 3 cards for alarm items
  - Bottom sheets for alarm creation/editing
  - Material dialogs for confirmations
  - Proper elevation and shadows
  - Consistent spacing and typography (Roboto font)
- Ensure dark mode support with proper Material 3 dark theme colors
- Use appropriate Material 3 icons from Material Icons library

### 3. Challenge System
- Allow users to create custom challenges for each alarm:
  - **Math Challenge**: Generate random math problems (addition, subtraction, multiplication, division) of configurable difficulty
  - **Other Challenge Types** (extensible): Quote memory, pattern recognition, or other custom challenge types
- Challenge configuration per alarm:
  - Challenge type selector
  - Difficulty level (easy, medium, hard)
  - Enable/disable challenges
- Challenge execution when alarm triggers:
  - Display challenge in a dialog or full-screen overlay
  - User must correctly complete the challenge to snooze (10-minute default)
  - User must correctly complete the challenge to stop/dismiss the alarm
  - Show feedback on incorrect answers (allow retries)
  - Track challenge attempts

### 4. Snooze Functionality
- Default snooze duration: 10 minutes (configurable in settings)
- Snooze button must be preceded by challenge completion if enabled
- Display remaining snooze time in the active alarm notification
- Multiple snooze support with challenge on each snooze

### 5. Stop/Dismiss Functionality
- Clear stop button to dismiss alarm completely
- Stop button must be preceded by challenge completion if enabled
- Optional confirmation dialog for stopping alarms
- Visual and audio feedback when alarm is stopped

## Stopping Conditions for Ralph Loop

The Ralph loop should iterate and improve the application until ALL of the following conditions are met:

1. **Feature Completeness**
   - All core functionality works without crashes
   - Alarms persist across app restarts
   - Alarms trigger at the correct time
   - Challenge system engages and validates answers correctly
   - Snooze and stop functionality work as expected

2. **Material Design 3 Compliance**
   - All UI components follow Material 3 design guidelines
   - Dynamic color support is implemented (API 31+)
   - Dark mode works correctly across all screens
   - Proper accessibility (content descriptions, contrast ratios)
   - No deprecated Material Design components in use

3. **Code Quality**
   - No runtime exceptions or crashes
   - Proper error handling for edge cases
   - Code follows Android best practices (MVVM or similar architecture)
   - Clear separation of concerns (UI, logic, data layers)

4. **Testing & Stability**
   - App doesn't crash when:
     - Creating multiple alarms
     - Enabling/disabling alarms
     - Editing alarm details
     - Completing/failing challenges
     - Snoozing multiple times
     - Device is rotated
     - App is backgrounded and restored
   - Alarms work reliably after device restart

5. **User Experience**
   - Challenge completion is intuitive and clear
   - Alarm UI is clean and easy to use
   - Feedback is provided for all user actions
   - No confusing or redundant UI elements

6. **Documentation** (if applicable)
   - Code comments explain non-obvious logic
   - README explains how to use the app
   - Setup instructions for building the app

## Non-Requirements
- Backend server integration
- Cloud synchronization
- Voice control
- Sleep tracking
- Multiple user accounts
- Wear OS support (initial release)

## Tech Stack Recommendations
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (preferred) or Material Design Library
- **Database**: Room
- **Architecture**: MVVM with ViewModel and LiveData/Flow
- **Notifications**: NotificationManager
- **Alarms**: AlarmManager

## Success Metrics
- App is installable and runs without crashes
- At least 2 alarm types are fully functional
- Challenge system works reliably
- Material 3 design is visually consistent throughout
- User can complete the entire alarm flow (create → trigger → challenge → snooze/stop)
