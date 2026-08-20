# Implementation Plan - Refactor Global FAB and Fix NotePecker UI

Refactor the global FAB behavior in `MainActivity` to be dynamic and fix several UI issues in the NotePecker feature to align with the provided design.

## Goal Description
1.  **Dynamic Center FAB**: Implement logic in `MainActivity` to change the center bottom button's function based on the current navigation destination.
    - Default (Dashboard, History, etc.): Trigger "Add Transaction".
    - Notes Screen: Transform into a Note Speed Dial FAB with "Text Note", "Calculation Table", "Image", and "Audio" options.
2.  **NotePecker UI Enhancements**:
    - Update `NotesScreens.kt` to include a centered "NotePecker" title and a dedicated search row with Layout (List/Grid) and Filter icons.
    - Apply `#121212` background to Notes and Editor screens.
    - Ensure note card colors carry over to the Editor background.
    - Add "SL" (Serial Number) column to Calculation Tables.

## User Review Required
> [!IMPORTANT]
> - The database version will not be incremented as no schema changes are strictly required for UI placeholders (Image/Audio), but `imageUrl` and `audioUrl` fields will be added to the `Note` model for future-proofing.
> - The "Quick Note" option will be removed from the speed dial.
> - The global FAB will be integrated into the `BottomNavigationBar` logic in `MainActivity` to maintain the central position requested.

## Proposed Changes

### [app]

#### [MODIFY] [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)
- Add state to track `showNoteSpeedDial` globally.
- Update `BottomNavigationBar`:
    - Check `currentRoute` in the `onClick` of the central `Add` item.
    - If route is "notes", toggle speed dial visibility.
    - Otherwise, trigger the transaction dialog.
- Render the `NoteSpeedDialOverlay` when active.

#### [MODIFY] [NotesScreens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/NotesScreens.kt)
- **Top Bar & Search**:
    - Rewrite `Scaffold`'s `topBar` to include the "NotePecker" title and a secondary row for the search field (`#1E1E1E` background).
    - Add Grid/List toggle and Filter icons next to the search field.
- **Backgrounds**:
    - Apply `#121212` to `NotesScreen` and `NoteEditor`.
- **NoteEditor**:
    - Bind the `selectedColor` to the `Scaffold`'s `containerColor` in the editor to ensure card color persistence.
- **Calculation Table**:
    - Update `CalculationTableEditor` to include the "SL" column.

#### [MODIFY] [Models.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/model/Models.kt)
- Add `imageUrl: String? = null` and `audioUrl: String? = null` to the `Note` entity.
- Update `FabAction` enum: remove `QuickNote`, add `NewImage`, `NewAudio`.

#### [MODIFY] [MainViewModel.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/MainViewModel.kt)
- Add methods/states if needed to trigger note creation from the global FAB (e.g., `triggerNewNote(FabAction)`).

## Verification Plan
### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify compilation.

### Manual Verification
- Verify the center button toggles between Add Transaction and Note Speed Dial.
- Check the Notes screen TopBar and Search Bar styling.
- Verify the Calculation Table serial numbers.
- Ensure the background color is `#121212` and card colors persist in the editor.
