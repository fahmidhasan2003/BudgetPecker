# Refactor Currency Converter to Full Screen

Refactor the existing Currency Converter feature from `Screens.kt` into a standalone `CurrencyConverterScreen.kt` file, update its UI to be full-screen, and integrate it into the app's navigation system.

## Proposed Changes

### UI Components

#### [NEW] [CurrencyConverterScreen.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/CurrencyConverterScreen.kt)
- Create this file in `com.fahmicode.ui.screens`.
- Move `CurrencyConverterContent`, `CurrencyPicker`, and `getCurrencySymbol` from `Screens.kt`.
- Rename `CurrencyConverterContent` to `CurrencyConverterScreen`.
- Update `CurrencyConverterScreen` signature:
  ```kotlin
  @Composable
  fun CurrencyConverterScreen(
      viewModel: MainViewModel,
      onBackClick: () -> Unit
  )
  ```
- Change the root `Column` modifier to `Modifier.fillMaxSize()`.
- Update the header:
  - Use `IconButton` with `Icons.AutoMirrored.Filled.ArrowBack` at the start.
  - Center the "Currency Converter" title.
  - Remove the "Close" button.
- Ensure all necessary imports are included.

#### [MODIFY] [Screens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/Screens.kt)
- Remove `CurrencyConverterContent`, `CurrencyPicker`, and `getCurrencySymbol`.
- Update `SettingsScreen` signature to include `onConverterClick: () -> Unit`.
- Update the "Converter" item in `SettingsScreen` grid to call `onConverterClick()`.

### ViewModel

#### [MODIFY] [MainViewModel.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/MainViewModel.kt)
- Remove `var showConverter = mutableStateOf(false)` as it will be replaced by navigation.

### Navigation & Activity

#### [MODIFY] [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)
- Update `BottomNavItem` to include `object Converter : BottomNavItem("converter", "Converter", Icons.Default.CurrencyExchange)`.
- In `MainActivity` content:
  - Remove the `val showConverter by mainViewModel.showConverter` block and the corresponding `if (showConverter)` UI logic.
  - Update `SettingsScreen` call in the "More Menu" overlay to pass `onConverterClick = { ... navigate to converter ... }`.
- In `NavigationHost`:
  - Add a `composable(BottomNavItem.Converter.route)` destination.
  - Use `CurrencyConverterScreen(viewModel = viewModel, onBackClick = { navController.popBackStack() })`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify the build.

### Manual Verification
- Verify that clicking "Converter" in the "More" menu navigates to the full-screen Currency Converter.
- Verify that the back button in the Currency Converter screen returns to the previous screen.
- Verify that the Currency Converter still functions correctly (amount entry, currency selection, live rate updates).
