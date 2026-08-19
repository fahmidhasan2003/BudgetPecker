# Implementation Plan - Fix Calculator Text Styling and Accessibility

The user wants to refactor the display area of the `CalculatorScreen` to improve the visual hierarchy between the input expression and the result, using a dynamic `isEvaluated` state.

## User Review Required

> [!IMPORTANT]
> The navigation fix previously implemented moved the result to the `expression` field and cleared `liveResult` when `=` was pressed. This refactor will change that behavior: the `expression` will remain as the formula (but styled smaller) and the `liveResult` will become the prominent final result.

## Proposed Changes

### Calculator Screen Refactor

1.  **State Management**:
    - Add `var isEvaluated by remember { mutableStateOf(false) }` to `CalculatorScreen`.
2.  **Input Logic (`onAction`)**:
    - Update `onAction` to set `isEvaluated = true` when `=` is pressed.
    - When `isEvaluated` is true, the next input will either:
        - Start a fresh expression (if a digit is pressed).
        - Use the result as the base for a new expression (if an operator is pressed).
    - Reset `isEvaluated = false` on any new input or deletion.
3.  **Display Styling**:
    - Refactor the two `Text` components in the `Result Card`.
    - **Typing State**:
        - `expression` (Top): Large (40sp), Bold, Primary Color.
        - `liveResult` (Bottom): Smaller (26sp), Medium weight, Muted Gray.
    - **Evaluated State**:
        - `expression` (Top): Smaller (26sp), Medium weight, Muted Gray.
        - `liveResult` (Bottom): Large (40sp), Bold, Primary Color.
4.  **Animations**:
    - Use `animateIntSizeAsState` or `animateFloatAsState` for font sizes if possible, or just standard conditional modifiers to ensure a smooth transition.

### app

#### [MODIFY] [CalculatorScreen.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/CalculatorScreen.kt)
- Add `isEvaluated` state.
- Update `onAction` handler logic.
- Update UI components for expression and result display with dynamic styling.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify compilation.

### Manual Verification
1.  Open Calculator.
2.  Type "100+50".
    - **Verify**: "100+50" is large and bold; "150" is small and gray.
3.  Press "=".
    - **Verify**: "100+50" shrinks and turns gray; "150" becomes large, bold, and primary color.
4.  Press "+".
    - **Verify**: "150+" becomes the new expression, large and bold.
5.  Type "50" and press "=".
    - **Verify**: "150+50" shrinks; "200" becomes large and bold.
