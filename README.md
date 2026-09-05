# BudgetPecker

BudgetPecker is an offline-first Android personal finance and note management application designed to provide users with a seamless and private way to track their expenses and organize their thoughts. It combines powerful budget tracking with an integrated note-management component called **Notepecker**.

## Core Features

- **Cumulative Balance Calculation with Monthly Rollover**
  - **Previous Month's Carry-Forward:** The closing balance of the previous month automatically carries forward to the following month.
  - **Total Balance:** Calculated cumulatively through the end of the selected month, providing a holistic view of your financial health.
  - **Monthly Specifics:** Monthly Income and Monthly Expense remain specific to the selected month for granular tracking.
- **Category-wise Monthly Budget Tracking:** Organize your spending by categories and monitor your budget performance month by month.
- **Integrated Note Management (Notepecker):** A dedicated space within the app to capture notes, ideas, and reminders, keeping your finances and thoughts in one place.
- **Modern Jetpack Compose UI:** A reactive and fluid user interface built with the latest Android UI toolkit.
- **Light/Dark Theme Support:** Automatically adapts to system settings or user preference for comfortable viewing in any environment.

## Tech Stack & Architecture

BudgetPecker is built using modern Android development practices to ensure performance, maintainability, and a great user experience.

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Local Database:** [Room Database](https://developer.android.com/training/data-storage/room)
- **Concurrency / Reactive Data:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Testing:** JUnit 4 / Unit Tests (with Robolectric and Roborazzi support)

## Repository & Git Hygiene

To maintain a clean and trackable history, this project follows specific Git conventions.

### Commit Tracking Format
All commits should follow the format:
`[SDBXXXX_YYYYMMDD_HHMMSS]`

### Version Control Exclusions
The following build/generated artifacts and local environment files are intentionally excluded from version control to prevent bloat and security risks:
- `.artifacts/`
- `Custom/`
- `build/`
- `.idea/`
- `local.properties`
- `.gradle/`

## Setup & Installation

Follow these steps to get the project running on your local machine:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/fahmidhasan2003/BudgetPecker.git
   ```
2. **Open in Android Studio:**
   - Launch Android Studio.
   - Select **Open** and navigate to the cloned `budgetpecker` directory.
3. **Gradle Sync:**
   - Wait for Android Studio to complete the Gradle synchronization process. This will download all necessary dependencies.
4. **Select Device:**
   - Choose a physical Android device (via USB/Wi-Fi) or an Android Virtual Device (AVD) from the device manager.
5. **Build and Run:**
   - Click the **Run** button (green play icon) or press `Shift + F10` to build and deploy the application to your device.

## Testing
The project includes unit tests to ensure the reliability of the business logic. You can run tests directly from Android Studio or via Gradle:
```bash
./gradlew test
```

---
*BudgetPecker - Simplify your finances, one peck at a time.*
