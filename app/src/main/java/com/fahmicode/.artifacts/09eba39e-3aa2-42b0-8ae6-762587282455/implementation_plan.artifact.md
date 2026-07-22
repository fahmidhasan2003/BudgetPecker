# Redesign CurrencyConverterContent

Refactor the `CurrencyConverterContent` composable to follow the Windows Calculator design with an integrated keypad and enhanced dropdown formatting.

## User Review Required

> [!IMPORTANT]
> This change focuses exclusively on `CurrencyConverterContent`. The separate `CurrencyPicker` composable will be bypassed by implementing local picker logic to adhere to the strict "Modify ONLY" instruction.

## Proposed Changes

### [app]

#### [MODIFY] [Screens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/Screens.kt)
- **State & Logic**:
    - Manage `amount` string, `fromCurrency`, and `toCurrency`.
    - evaluation logic using live rates.
    - Keypad input handling (append digits, decimal, clear, delete).
- **Layout**:
    - **Header**: Title and close button.
    - **Display Section (Top Half)**:
        - Two rows: "From" (source) and "To" (target).
        - Left: Clickable currency code with dropdown arrow.
        - Right: Large read-only amount text.
        - Subtext showing the exchange rate summary and "Live Rates" status.
    - **Keypad Section (Bottom Half)**:
        - 4x4 Grid:
            - `[7] [8] [9] [C]`
            - `[4] [5] [6] [⌫]`
            - `[1] [2] [3] [.]`
            - `[00] [0] [ ] [ ]` (Balanced layout).
- **Dropdown Items**:
    - Updated selection menu to show "Code - Full Name (Symbol)".

## Verification Plan

### Automated Tests
- **Build**: Run `:app:assembleDebug`.

### Manual Verification
- Open Currency Converter and verify:
    - Dialog matches Calculator aspect ratio.
    - Keypad layout is 4x4 with the specific grid requested.
    - No native keyboard appears.
    - Dropdown items show the detailed "Code - Name (Symbol)" format.
    - Conversions update instantly.
