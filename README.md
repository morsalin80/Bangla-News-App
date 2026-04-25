# BanglaNews

A Bengali news Android application built with Jetpack Compose.

## Features

- Browse news by categories
- View news articles with images
- Clean Architecture with MVVM pattern

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM
- **Networking**: Retrofit + Gson
- **DI**: Manual injection via Factory pattern
- **Ads**: Google Mobile Ads SDK

## Build

```bash
./gradlew assembleDebug
```

## Project Structure

```
app/src/main/java/com/example/banglanews/
├── model/          # Data layer (API, models, repository)
├── view/           # UI layer (Activities, Composables)
├── viewmodel/      # ViewModels
└── ui/theme/       # Theme configuration
```