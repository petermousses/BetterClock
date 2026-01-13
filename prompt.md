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
  - Optional: Challenge timeout (default 5 minutes)
- Challenge execution when alarm triggers:
  - Display challenge in a full-screen overlay that cannot be swiped away
  - User must correctly complete the challenge to snooze (10-minute default)
  - User must correctly complete the challenge to stop/dismiss the alarm
  - Show immediate feedback on incorrect answers (red highlight, brief error message)
  - Allow unlimited retries without penalty
  - Track challenge attempts (show attempt count to user)
  - If challenge times out with no completion attempt, alarm continues sounding (no auto-dismiss)

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

### 6. Accessibility Requirements
- All UI elements have descriptive content descriptions for screen readers
- Minimum touch target size of 48dp for all interactive elements
- Color contrast ratio of at least 4.5:1 for text (WCAG AA standard)
- Text should be resizable up to 200% without breaking layout
- All functionality accessible via keyboard navigation
- Vibration feedback available as alternative to audio-only feedback

### 7. Performance & Battery Considerations
- Alarms use AlarmManager for reliable, battery-efficient triggering
- No continuous background services running when not needed
- Challenge generation should complete in <100ms
- App should use <50MB RAM during normal operation
- Database queries should complete within <500ms
- Notification sounds should be reasonable duration (not system battery drain)

### 8. Settings & Configuration
- Snooze duration configurable (5-60 minutes, default 10)
- Option to enable/disable challenges globally
- Default challenge difficulty selectable (easy/medium/hard)
- Alarm sound selectable from system sounds
- Vibration toggle
- 24-hour or 12-hour time format option

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
   - All unit tests pass (challenge logic, database operations, time calculations)
   - All integration tests pass (persistence, challenge integration, notifications)
   - All UI tests pass (all screens and critical user flows)
   - All background/system tests pass (alarm triggering, notifications, device restart)
   - All edge case tests pass (multiple alarms, state transitions, configuration changes)
   - Code coverage meets minimum requirements (70% overall, 80% for business logic)
   - App doesn't crash when:
     - Creating multiple alarms
     - Enabling/disabling alarms
     - Editing alarm details
     - Completing/failing challenges multiple times
     - Snoozing multiple times in succession
     - Device is rotated during any screen
     - App is backgrounded and restored
     - App is force-closed and reopened
   - Alarms work reliably after device restart
   - Performance targets met:
     - Challenge generation <100ms
     - Database queries <500ms
     - RAM usage <50MB during normal operation

5. **User Experience**
   - Challenge completion is intuitive and clear
   - Alarm UI is clean and easy to use
   - Feedback is provided for all user actions
   - No confusing or redundant UI elements

6. **Documentation** (if applicable)
   - Code comments explain non-obvious logic
   - README explains how to use the app
   - Setup instructions for building the app

## 6. Testing Requirements

Comprehensive test coverage is required across all layers:

### Unit Tests
- **Challenge Logic**
  - Math problem generation produces correct problems for each difficulty level
  - Answer validation correctly identifies correct and incorrect answers
  - Challenge timeout logic triggers appropriately
- **Database Layer**
  - Alarms can be created, read, updated, and deleted correctly
  - Alarm persistence survives data serialization/deserialization
  - Room queries return expected results for various filter conditions
- **Time Logic**
  - Alarm trigger times are calculated correctly
  - Snooze duration calculations are accurate
  - Time zone handling (if applicable) works correctly

### Integration Tests
- **Alarm Persistence**
  - Alarms saved to database are retrievable on app restart
  - Editing an alarm correctly updates all fields in database
  - Deleting alarms removes them from all app data
- **Challenge System Integration**
  - Challenges are properly associated with correct alarm
  - Challenge attempt tracking is persistent
  - Challenge state is correctly cleared when alarm is stopped
- **Notification System**
  - Notifications are created when alarm triggers
  - Notification channels are properly configured
  - Pending intents are correctly set for snooze/stop actions

### UI Tests (Instrumented)
- **Alarm List Screen**
  - Alarms display correctly with all details visible
  - Add/edit/delete actions work from UI
  - Enable/disable toggles persist changes
  - Empty state displays when no alarms exist
- **Alarm Creation/Edit Flow**
  - All input fields accept and save values correctly
  - Challenge type and difficulty selectors work
  - Time picker allows valid hour/minute selection
  - Form validation prevents invalid inputs (if applicable)
- **Challenge Execution Screen**
  - Challenge displays clearly and readably
  - Math problems are solvable and answer validation works
  - Incorrect answer feedback appears immediately
  - Snooze and stop buttons only appear after correct answer
  - Full-screen overlay cannot be dismissed by swipe or back button
- **Post-Alarm Screen**
  - Snooze button shows remaining snooze duration
  - Stop button properly dismisses alarm
  - UI returns to normal state after alarm is handled

### Background/System Tests
- **Alarm Triggering**
  - Alarms trigger at correct time even when app is closed
  - Alarms trigger correctly after device restart
  - Multiple alarms at same time are handled (both trigger, both show challenges)
  - AlarmManager broadcasts are received correctly
- **Notification Interaction**
  - Tapping notification brings app to foreground with active alarm
  - Notification actions (snooze/stop from notification) work correctly
  - Notification persists until alarm is dismissed

### Edge Case Tests
- **Multiple Alarms**
  - Multiple alarms on same day trigger independently
  - Two alarms at exact same time both trigger
  - Editing one alarm doesn't affect others
  - Deleting one alarm doesn't affect others
- **State Transitions**
  - App backgrounded during challenge completion doesn't break state
  - Device rotation during challenge doesn't lose challenge progress
  - Challenge state persists if app is force-closed mid-challenge
- **Configuration Changes**
  - Changing snooze duration in settings applies to new snoozes
  - Changing default challenge difficulty applies to new alarms
  - Changes don't affect already-configured alarms

### Test Coverage Targets
- Minimum 80% code coverage for business logic (challenge, alarm, time logic)
- Minimum 70% code coverage overall
- All public API methods have corresponding tests
- Critical paths (alarm trigger → challenge → snooze/stop) have end-to-end tests

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
- **Testing Frameworks**:
  - Unit tests: JUnit 4 + Mockito or MockK
  - Integration tests: Robolectric (for isolated testing) or Espresso (for device/emulator)
  - UI tests: Espresso + Compose UI Test (if using Compose)
  - Code coverage: JaCoCo
- **Material 3 Design**: Material 3 Compose components or Material Design Library

## Success Metrics
- App is installable and runs without crashes
- At least 2 alarm types are fully functional
- Challenge system works reliably
- Material 3 design is visually consistent throughout
- User can complete the entire alarm flow (create → trigger → challenge → snooze/stop)

## Completion Signal

  Output the following when ALL stopping conditions are met:

  <promise>COMPLETE</promise>
