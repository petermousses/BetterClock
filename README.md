# BetterClock

A Material Design 3 Android alarm clock application with an innovative challenge system that requires users to solve puzzles before snoozing or dismissing alarms.

## Features

### Alarm Management
- Create, edit, and delete multiple alarms
- Set alarm time with intuitive time picker
- Enable/disable individual alarms with toggle switches
- Label alarms for easy identification
- Repeat alarms on specific days of the week
- One-time and recurring alarm support

### Challenge System
- **Math Challenges**: Solve arithmetic problems (addition, subtraction, multiplication, division)
- **Memory Challenges**: Remember and type back character sequences
- **Pattern Challenges**: Complete number patterns
- Three difficulty levels: Easy, Medium, Hard
- Challenge required to snooze or stop alarms (when enabled)
- Unlimited retry attempts with attempt tracking

### Material Design 3
- Dynamic color support (Android 12+)
- Dark mode support
- Modern UI components (Cards, FAB, Bottom Sheets, Segmented Buttons)
- Proper accessibility with content descriptions
- Minimum 48dp touch targets

### Settings
- Configurable snooze duration (5-60 minutes)
- Enable/disable challenges globally
- Default challenge difficulty selection
- 24-hour or 12-hour time format
- Vibration toggle
- Confirm stop dialog option

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModel and StateFlow
- **Database**: Room
- **Preferences**: DataStore
- **Alarms**: AlarmManager with exact alarms
- **Notifications**: NotificationManager with foreground service
- **Navigation**: Jetpack Navigation Compose
- **Material Design**: Material 3 Compose

## Project Structure

```
app/src/main/java/com/betterclock/
├── alarm/              # Alarm scheduling and triggering
│   ├── AlarmReceiver.kt
│   ├── AlarmScheduler.kt
│   ├── AlarmService.kt
│   └── BootReceiver.kt
├── challenge/          # Challenge generation and validation
│   ├── Challenge.kt
│   └── ChallengeGenerator.kt
├── data/               # Data layer
│   ├── dao/
│   ├── model/
│   └── repository/
├── ui/                 # UI layer
│   ├── components/
│   ├── screens/
│   └── theme/
├── viewmodel/          # ViewModels
├── BetterClockApplication.kt
└── MainActivity.kt
```

## Building the App

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or later
- Android SDK 34

### Build Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/BetterClock.git
   cd BetterClock
   ```

2. Open in Android Studio or build from command line:
   ```bash
   ./gradlew assembleDebug
   ```

3. Install on device:
   ```bash
   ./gradlew installDebug
   ```

### Running Tests

Unit tests:
```bash
./gradlew test
```

Instrumented tests (requires connected device/emulator):
```bash
./gradlew connectedAndroidTest
```

## Permissions

The app requires the following permissions:
- `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM` - For precise alarm timing
- `POST_NOTIFICATIONS` - For alarm notifications
- `VIBRATE` - For vibration alerts
- `RECEIVE_BOOT_COMPLETED` - To reschedule alarms after device restart
- `USE_FULL_SCREEN_INTENT` - For full-screen alarm display
- `FOREGROUND_SERVICE` - For alarm sound playback

## Usage

### Creating an Alarm
1. Tap the "Add Alarm" FAB on the main screen
2. Select the desired time using the time picker
3. Optionally add a label
4. Configure repeat days if needed
5. Enable challenges and select difficulty if desired
6. Tap "Create Alarm"

### When an Alarm Triggers
1. The alarm will sound and vibrate (if enabled)
2. If challenges are enabled, solve the displayed puzzle
3. Once solved, Snooze or Stop buttons become available
4. Tap Snooze to delay the alarm or Stop to dismiss it

### Managing Alarms
- Toggle the switch to enable/disable alarms
- Tap an alarm card to edit its settings
- Tap the delete icon to remove an alarm

## License

MIT License - See LICENSE file for details
