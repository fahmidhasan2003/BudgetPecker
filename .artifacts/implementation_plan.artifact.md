# Implementation Plan - Smart Notes Feature

This plan outlines the steps to implement a complete Smart Notes feature in the BudgetPecker app, integrating it with the existing Room database and Jetpack Compose UI.

## User Review Required

> [!IMPORTANT]
> The database version will be incremented from 1 to 2. I will implement a migration to add the `notes` table to avoid data loss.

> [!NOTE]
> The "Import to Expense" feature will use the existing `Transaction` entity and categories.

## Proposed Changes

### Data Layer

#### [MODIFY] [Models.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/model/Models.kt)
- Add `ChecklistItem` data class.
- Add `Note` entity with fields: `id`, `title`, `content`, `checklistItems`, `amount`, `expenseCategory`, `dateMillis`, and `colorHex`.

#### [NEW] [NoteTypeConverter.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/NoteTypeConverter.kt)
- Implement converters for `List<ChecklistItem>` to JSON String and vice versa using Gson (or manual JSON parsing if Gson is not available). I'll check `build.gradle` for Gson.

#### [NEW] [NoteDao.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/dao/NoteDao.kt)
- Define `getAllNotes()`, `insertOrUpdateNote()`, and `deleteNote()`.

#### [MODIFY] [AppDatabase.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/AppDatabase.kt)
- Add `Note` entity.
- Add `@TypeConverters(NoteTypeConverter::class)`.
- Increment version to 2.
- Add Migration from 1 to 2 for the `notes` table.
- Expose `noteDao()`.

#### [MODIFY] [AppRepository.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/AppRepository.kt)
- Add `NoteDao` to constructor.
- Add methods for notes operations.

#### [MODIFY] [MainViewModel.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/MainViewModel.kt)
- Add `notes` StateFlow.
- Add methods: `addNote()`, `updateNote()`, `deleteNote()`.

---

### UI & Navigation

#### [NEW] [NotesScreens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/NotesScreens.kt)
- Implement `NotesScreen` (Full Screen Grid).
- Implement `NotesPopup` (List Layout).
- Implement `NoteDetailDialog` for creating/editing notes.
- Implement "Import to Expense" confirmation dialog.

#### [MODIFY] [Screens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/Screens.kt)
- Update `SettingsScreen` to connect the "Notepad" button to navigation.

#### [MODIFY] [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)
- Add `NotesScreen` route to `NavigationHost`.
- Handle navigation to `NotesScreen`.

---

## Verification Plan

### Automated Tests
- I will check if the build compiles after changes.
- Since there are no existing unit tests visible in the file list, I will rely on manual verification and build checks.

### Manual Verification
- Deploy to device/emulator.
- Add a note with a checklist and verify strikethrough logic.
- Search for a note.
- Change note color.
- Import a note to expense and verify it appears in the History/Dashboard.
- Verify that existing data (Transactions, Budgets) is preserved after migration.
