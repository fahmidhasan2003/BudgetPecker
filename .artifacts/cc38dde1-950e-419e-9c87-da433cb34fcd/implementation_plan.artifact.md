# Implementation Plan - Add Back Arrow to CalculatorScreen

The user wants to add a back arrow icon button to the `CalculatorScreen` for better navigation, especially when the screen is opened from the "More" menu.

## Proposed Changes

### UI Implementation in `CalculatorScreen`

1.  **Modify Signature**: Update `CalculatorScreen` to accept `onBackClick: () -> Unit`.
2.  **Add Back Button**:
    - Wrap the header area in a `Box` to allow the back button to be placed in the top-left while keeping the title centered (or adjust the `Row` layout).
    - Use `IconButton` with `Icons.AutoMirrored.Filled.ArrowBack`.
    - Style the icon to match the app's theme.

### Navigation Logic in `MainActivity`

1.  **Update NavigationHost**: Pass `navController.popBackStack()` to the `onBackClick` parameter of `CalculatorScreen`.

## Proposed Changes

### app

#### [MODIFY] [CalculatorScreen.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/CalculatorScreen.kt)
- Update `CalculatorScreen` signature.
- Implement the Back Arrow button in the header.

#### [MODIFY] [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)
- Update the `NavigationHost` route for `BottomNavItem.Calculator.route` to pass the back callback.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to ensure no syntax errors.

### Manual Verification
1.  Open the app.
2.  Navigate to the "More" menu.
3.  Click "Calculator".
4.  Verify that a back arrow appears at the top-left of the Calculator screen.
5.  Click the back arrow and verify it returns to the previous screen (More menu).
