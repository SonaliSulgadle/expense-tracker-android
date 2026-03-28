# Expense Tracker with AI Categorization

![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

> 🚧 Core features complete — polish in progress.

An Android expense tracking app that automatically categorizes
expenses using Google Gemini AI. Built with production-quality
Clean Architecture, modern Jetpack libraries, and a premium
dark Amber/Gold UI theme.

---

## Screenshots

> Coming soon — recording demo GIF

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture (Domain / Data / UI) |
| Language | Kotlin 2.0 |
| Database | Room + Kotlin Flow |
| AI | Google Gemini 2.5 Flash Lite (REST via Retrofit) |
| Dependency Injection | Hilt |
| Navigation | Navigation Compose |
| Networking | Retrofit + OkHttp |
| Preferences | DataStore |
| Logging | Timber |
| Charts | Custom Compose Canvas |
| Testing | JUnit4 + Mockito-Kotlin + Turbine |

---

## Features

### Completed ✅
- [x] Add expenses with description and amount
- [x] AI-powered automatic categorization — Gemini 2.5 Flash Lite
- [x] Smart emoji resolution — 60+ keyword rules, fully offline
- [x] Dashboard with monthly total, daily average, category stats
- [x] Spending by category — custom animated bar chart (Compose Canvas)
- [x] Expense list with swipe-to-delete and undo snackbar
- [x] Category detail screen — stats, top items breakdown, full list
- [x] Expenses grouped by month with section headers
- [x] Dark premium Amber/Gold theme
- [x] Light theme support
- [x] App icon
- [x] Reactive UI — Room Flow drives all screen updates automatically
- [x] Retry logic with exponential backoff on Gemini API failures
- [x] Graceful AI fallback — app works even when Gemini is unavailable
- [x] User name onboarding with DataStore persistence

### In Progress 🚧
- [ ] Unit tests — UseCases, ViewModels, Utils
- [ ] Accessibility content descriptions

### Planned 📋
- [ ] Receipt scanning — CameraX + Gemini Vision API
- [ ] Demo GIF + screenshots in README

---

## Architecture

Clean Architecture — UI never accesses Data directly.
```
UI Layer
  Compose screens + ViewModels
  Observes StateFlow, calls UseCases only
        ↓
Domain Layer  
  UseCases + Domain models + Repository interfaces
  Pure Kotlin — zero Android imports
        ↓
Data Layer
  Room + Retrofit + DataStore
  Implements domain interfaces
```

### Key Engineering Decisions

**Gemini via Retrofit instead of SDK**
The official `google-generativeai` Android SDK was deprecated in
December 2025. Migrated to direct REST API calls — eliminating
dependency risk, gaining full HTTP visibility via
`HttpLoggingInterceptor`, and making `GeminiService` independently
testable via interface injection.

**Custom Canvas chart instead of Vico**
Vico 3.x required AGP 9.0+ which would have been a breaking
upgrade. Built a custom bar chart using `Animatable`, `drawRoundRect`,
and `Brush.verticalGradient` — demonstrating deeper Compose
knowledge and eliminating all external chart dependencies.

**`CategoryRepository` interface**
`GeminiService` implements a domain-layer interface.
`AddExpenseUseCase` has zero knowledge of Gemini or Retrofit.
Switching AI providers requires one `@Binds` change.

**Two-layer AI categorization**
Gemini handles ambiguous text classification. Emoji resolution
uses 60+ local keyword rules — no API cost, works offline,
deterministic output.

---

## AI Data Flow
```
User types "starbucks latte"
        ↓
GeminiService → Retrofit → Gemini REST API
    category: "Food & Drink", confidence: 0.95
        ↓
EmojiResolver (local, offline)
    "starbucks" keyword match → ☕
        ↓
Expense saved:
    description: "starbucks latte"
    category:    "Food & Drink"  
    emoji:       "☕"
    confidence:  0.95
    isAiCategorized: true
```

---

## Setup

**Requirements:** Android Studio Hedgehog+, min SDK 26

1. Clone the repository
2. Get a free Gemini API key at [aistudio.google.com](https://aistudio.google.com)
    - Free tier: 1,000 requests/day with `gemini-2.5-flash-lite`
3. Create `local.properties` in the project root:
```
   GEMINI_API_KEY=your_key_here
```
4. Build and run

---