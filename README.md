# Expense Tracker with AI Categorization

![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue)

> 🚧 This project is actively being built. Features are added incrementally.

An Android app that tracks expenses and automatically categorizes them
using Google Gemini AI.

## Tech Stack
- **UI**: Jetpack Compose + Material 3
- **Architecture**: Clean Architecture (Domain / Data / UI layers)
- **Database**: Room with Kotlin Flow
- **AI**: Google Gemini 1.5 Flash
- **DI**: Hilt
- **Charts**: Vico
- **Camera**: CameraX (receipt scanning)

## Features
- [x] Project setup — Clean Architecture scaffold
- [x] Data layer — Room DB, DAO, Repository
- [x] Dependency injection — Hilt wiring
- [ ] AI layer — Gemini categorization service
- [ ] Expense ViewModel + UseCases
- [ ] Dashboard screen with charts
- [ ] Add expense screen with AI categorization
- [ ] Receipt scanning with camera

## Architecture
[Clean Architecture with 3 layers: UI → Domain → Data]

## Setup
1. Clone the repo
2. Get a free Gemini API key at aistudio.google.com
3. Add to `local.properties`: `GEMINI_API_KEY=your_key_here`
4. Run the app