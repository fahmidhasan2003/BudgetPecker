# Walkthrough - Calculator Percentage Fix & Switch Contrast

I have fixed the percentage calculation logic in the calculator and improved the contrast of the Dark Mode switch in the settings.

## Changes Made

### Calculator Percentage Logic
- **Live Calculation**: Updated `evaluateExpression` to correctly handle `%` during live typing. It no longer removes `%` from the end of the expression prematurely.
- **Markup/Markdown Logic**: Implemented standard financial calculator behavior:
    - `100 + 5%` = `105` (adds 5% of 100 to 100)
    - `100 - 5%` = `95` (subtracts 5% of 100 from 100)
    - `100 * 5%` = `5` (multiplies by 0.05)
    - `100 / 5%` = `2000` (divides by 0.05)
- **Parser Update**: Modified the internal recursive descent parser in `CalculatorScreen.kt` to handle `%` as a context-aware postfix operator.

### Switch Contrast
- **Visibility**: Updated the `Switch` component in `Screens.kt` (Settings & Preferences) with explicit high-contrast colors.
- **Colors**:
    - Checked: Primary track with OnPrimary thumb.
    - Unchecked: SurfaceVariant track with OnSurfaceVariant thumb.
    - This ensures the switch is clearly visible in both Light and Dark themes.

## Verification

### Manual Verification Results
- **Percentage Test**:
    - Typed `10000 - 5%`. Live result immediately showed `9500`.
    - Pressed `=`. Result `9500` was committed to history and main display.
- **Switch Test**:
    - Opened Settings & Preferences.
    - Verified the "Dark Mode" switch stands out clearly against the dialog background.
- **Deployment**:
    - Successfully deployed to the connected USB device in DEBUG mode.

render_diffs(file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/CalculatorScreen.kt)
render_diffs(file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/Screens.kt)
