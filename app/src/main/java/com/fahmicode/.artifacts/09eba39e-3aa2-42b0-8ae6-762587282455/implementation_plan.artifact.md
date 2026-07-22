# Real-Time Currency Converter Implementation

Implement a currency converter with live exchange rates, a modern UI, and a reliable fallback mechanism.

## User Review Required

> [!IMPORTANT]
> The converter will fetch live rates from `open.er-api.com` on startup or when opened. A set of default rates will be used if the network is unavailable.

## Proposed Changes

### [app]

#### [NEW] [CurrencyApiService.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/network/CurrencyApiService.kt)
- Define a Retrofit interface to fetch rates from `https://open.er-api.com/v6/latest/USD`.
- Data classes for `ExchangeRateResponse`.

#### [MODIFY] [AppRepository.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/AppRepository.kt)
- Add a method `getExchangeRates()` that calls the API.

#### [MODIFY] [MainViewModel.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/MainViewModel.kt)
- **Currency States**:
    - `exchangeRates`: Map of currency codes to their rate relative to USD.
    - `converterFromCurrency`: Selected source currency (default: USD).
    - `converterToCurrency`: Selected target currency (default: BDT).
    - `converterAmount`: Input amount.
    - `lastRatesUpdate`: Timestamp of the last successful API call.
    - `showConverter`: Visibility state for the UI overlay.
- **Logic**:
    - `fetchLiveRates()`: Coroutine to fetch rates and handle errors/fallbacks.
    - `convertCurrency(amount, from, to)`: Math logic using USD as the bridge.

#### [MODIFY] [Screens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/Screens.kt)
- **`CurrencyConverterContent` Composable**:
    - Modern card design with large amount display.
    - "From" and "To" currency pickers with flags/icons.
    - Swap button (↔️).
    - Instant result preview as the user types.
    - "Live" status badge and "Last Updated" text.

#### [MODIFY] [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)
- Add `CurrencyConverterContent` as an overlay, similar to the Calculator.

## Verification Plan

### Automated Tests
- **Build**: Run `:app:assembleDebug`.
- **Static Analysis**: Catch potential network or state issues.

### Manual Verification (via Debug Run)
- Open the converter from the "More" menu.
- Verify live rates are fetched (or fallback is used).
- Test swapping currencies.
- Verify the math is correct across different currency pairs.
