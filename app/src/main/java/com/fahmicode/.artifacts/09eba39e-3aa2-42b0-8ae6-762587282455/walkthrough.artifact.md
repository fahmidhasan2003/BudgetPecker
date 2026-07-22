# Real-Time Currency Converter Walkthrough

I have implemented a comprehensive Currency Converter in BudgetPecker, featuring live exchange rates from an external API and a modern, responsive UI.

## Changes Overview

### [app]

#### [CurrencyApiService.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/data/network/CurrencyApiService.kt)
- **Networking**: Integrated Retrofit with Moshi to fetch live exchange rates from `open.er-api.com`. This API provides USD-based rates that are updated regularly.

#### [MainViewModel.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/MainViewModel.kt)
- **Live Updates**: Implemented `fetchLiveRates()` to fetch data on app launch.
- **Reliability**: Added a fallback mechanism with static rates (for USD, BDT, EUR, GBP, etc.) to ensure the converter works even without an internet connection.
- **State Management**: Created reactive states for rates, update timestamps, and UI visibility.

#### [Screens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/Screens.kt)
- **`CurrencyConverterContent`**: A new, modern UI featuring:
    - **Live Result**: Instant conversion as you type.
    - **Currency Pickers**: Easy selection for "From" and "To" currencies.
    - **Swap (↔️)**: Quick swap between selected currencies.
    - **Status Indicator**: Visual badge showing if rates are "Live" or "Offline".
    - **Formatted Display**: Large, easy-to-read converted values with symbols.

#### [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)
- **Integration**: Connected the "Converter" option in the "More" menu to the new overlay dialog.

## Verification Results

### Automated Verification
- **Build Status**: Successful (`:app:assembleDebug` passed).
- **Network Logic**: Verified that the API response is correctly parsed and fallbacks are triggered on failure.

### Live UI Verification
The app was successfully deployed to the connected device.

> [!TIP]
> Open the "More" menu and tap **Converter** to try it out! It will automatically try to fetch the latest BDT/USD rates for you.
