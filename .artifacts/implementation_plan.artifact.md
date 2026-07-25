# ইমপ্লিমেন্টেশন প্ল্যান - ক্যালকুলেটর রিফ্যাক্টর (ডেডিকেটেড স্ক্রিন)

ক্যালকুলেটর ফিচারটিকে একটি পপআপ/ডায়ালগ থেকে আলাদা একটি ডেডিকেটেড স্ক্রিনে রূপান্তর করা হবে। এটি 'More' মেনু থেকে অ্যাক্সেস করা যাবে এবং `Screens.kt` ফাইলটিকে পরিচ্ছন্ন রাখবে।

## প্রস্তাবিত পরিবর্তনসমূহ

### UI কম্পোনেন্টসমূহ

#### [NEW] [CalculatorScreen.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/CalculatorScreen.kt)
- ক্যালকুলেটরের সমস্ত UI কোড এই নতুন ফাইলে সরানো হবে।
- `Screens.kt` থেকে `CalculatorContent`, `CalculatorButton`, এবং `evaluateExpression` ফাংশনগুলো এখানে নিয়ে আসা হবে।
- ইউআই আগের ফুলস্ক্রিন ক্যালকুলেটরের মতোই থাকবে, তবে উপরে কোনো ব্যাক বাটন বা ক্লোজ বাটন থাকবে না।

#### [MODIFY] [Screens.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/screens/Screens.kt)
- ক্যালকুলেটর সংক্রান্ত সমস্ত কোড মুছে ফেলা হবে।
- `SettingsScreen` (More Menu) আপডেট করা হবে যাতে 'Calculator' এ ক্লিক করলে নতুন স্ক্রিনে নেভিগেট করে।

### নেভিগেশন এবং স্টেট

#### [MODIFY] [MainActivity.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/MainActivity.kt)
- `BottomNavItem` এ ক্যালকুলেটরের জন্য একটি রুট যোগ করা হবে।
- `NavigationHost` এ ক্যালকুলেটর স্ক্রিনের জন্য একটি নতুন রুট (`composable`) যোগ করা হবে যাতে এটি বটম নেভিগেশন বারের সাথে কাজ করে।
- আগের পপআপ লজিকগুলো `MainActivity` থেকে সরিয়ে ফেলা হবে।

#### [MODIFY] [MainViewModel.kt](file:///H:/FAHMID/budgetpecker/app/src/main/java/com/fahmicode/ui/MainViewModel.kt)
- `showCalculator` এবং `isCalculatorFullscreen` এর মত অপ্রয়োজনীয় স্টেটগুলো মুছে ফেলা হবে।
- `calculatorHistory` এবং এর সাথে সম্পর্কিত লজিকগুলো অপরিবর্তিত থাকবে।

---

## যাচাইকরণ পরিকল্পনা

### অটোমেটেড টেস্ট
- `app:assembleDebug` রান করে কোড কম্পাইল হচ্ছে কি না তা নিশ্চিত করা হবে।

### ম্যানুয়াল ভেরিফিকেশন
- 'More' মেনু থেকে ক্যালকুলেটর ওপেন হচ্ছে কি না পরীক্ষা করা।
- ক্যালকুলেটর স্ক্রিনে থাকাকালীন বটম নেভিগেশন বার দেখা যাচ্ছে কি না এবং কাজ করছে কি না দেখা।
- ক্যালকুলেটরের হিসাব এবং হিস্ট্রি ঠিকঠাক কাজ করছে কি না যাচাই করা।
