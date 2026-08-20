# NotePecker Feature Re-implementation Plan

Re-implement the NotePecker feature with Rich Text support, Calculation Tables, and a Speed Dial FAB. The implementation will include database schema updates, a new reactive UI layer, and dark theme alignment.

## User Review Required

> [!IMPORTANT]
> - The database version will be incremented to 3 and `fallbackToDestructiveMigration()` will be enabled. This will clear existing notes in development builds to simplify the schema transition.
> - Rich Text will be stored as a JSON string in the database.
> - Calculation Tables will support simple "Description | Amount" rows with an auto-calculated total.

## Proposed Changes

### Data Layer

#### [MODIFY] [Models.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/model/Models.kt)
- Update `Note` entity with the following fields:
    - `id: Long` (Primary Key)
    - `title: String`
    - `contentJson: String` (Rich Text data)
    - `category: String`
    - `updatedAt: Long`
    - `containsTable: Boolean`
    - `tableDataJson: String?` (Calculation Table data)
    - `colorHex: Long`
- Add data classes for `RichTextContent`, `TextSpan`, `CalculationTable`, and `CalculationItem`.

#### [MODIFY] [NoteTypeConverter.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/NoteTypeConverter.kt)
- Add Moshi converters for `RichTextContent` and `CalculationTable` to allow Room to store them as JSON.

#### [MODIFY] [AppDatabase.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/AppDatabase.kt)
- Increment version to 3.
- Enable `fallbackToDestructiveMigration()`.

### UI Layer

#### [MODIFY] [Color.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/theme/Color.kt)
- Add `DarkBackground = Color(0xFF121212)`.
- Ensure `AccentColor = Color(0xFF4CAF50)` is available.

#### [MODIFY] [Theme.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/theme/Theme.kt)
- Update `DarkColorScheme` to use `DarkBackground` and `AccentColor`.

#### [MODIFY] [NotesScreens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/NotesScreens.kt)
- Complete rewrite of the file.
- Implement `NotesScreen` with `LazyVerticalStaggeredGrid`.
- Implement `SpeedDialFab` for adding notes, tables, or quick notes.
- Implement `NoteEditor` screen with:
    - Title field.
    - Rich Text editor (basic bold/italic/color support).
    - Inline `CalculationTableEditor`.
    - Category and Color selectors.

### ViewModel

#### [MODIFY] [MainViewModel.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/MainViewModel.kt)
- Update note-related methods to handle the new `Note` entity fields.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify compilation.

### Manual Verification
- Deploy the app and navigate to the Notes screen.
- Test adding a note with Rich Text.
- Test adding a note with a Calculation Table.
- Verify the Staggered Grid layout and Speed Dial FAB animations.
- Check the Dark Theme application (#121212 background).
