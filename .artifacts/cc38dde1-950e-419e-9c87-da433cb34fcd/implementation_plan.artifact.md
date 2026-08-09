# Fix Navigation Issue with Calculator & Dashboard

The user reports that when navigating to the Calculator screen, clicking "Dashboard" in the bottom navigation bar does not return them to the Dashboard, effectively "sticking" them on the Calculator screen. This is likely due to inconsistent or incorrect `popUpTo` logic in the navigation calls.

## Proposed Changes

I will update the navigation logic in `MainActivity.kt` to use the idiomatic `popUpTo(navController.graph.findStartDestination().id)` pattern for all top-level-like navigation. This ensures that the backstack is correctly managed and that navigating to the start destination (Dashboard) always pops intermediate screens like the Calculator.

### app

#### [MODIFY] [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)

1.  Add `import androidx.navigation.NavGraph.Companion.findStartDestination` to enable the idiomatic way of finding the start destination ID.
2.  Update `BottomNavigationBar` to use `popUpTo(navController.graph.findStartDestination().id)` in the `onClick` handler.
3.  Update the navigation calls for `Notes` and `Calculator` in `MainActivity.onCreate` (within the `SettingsScreen` overlay) to also use the `popUpTo` pattern. This ensures that opening these screens from the "More" menu doesn't build an unnecessary backstack.
4.  Update the `onSeeMore` navigation in `NavigationHost` to use the same pattern.

## Verification Plan

### Manual Verification
- Deploy the app.
- Navigate to the Dashboard.
- Open the "More" menu and click "Calculator".
- Verify that the Calculator screen is shown.
- Click the "Dashboard" icon in the Bottom Navigation Bar.
- **Expected Result:** The app should navigate back to the Dashboard screen.
- Verify similar behavior for the "Notes" screen.
- Verify that clicking "History" or "Budget" also correctly pops the Calculator/Notes screens.
