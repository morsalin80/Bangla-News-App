BanglaNews Android App

Overview
- A Kotlin-based Android app built with Jetpack Compose that fetches and displays Bangla news by category.
- Core features include category tabs, a responsive Compose UI, and pull-to-refresh for reloading data.
- The app uses Retrofit for network calls and follows an MVVM architecture with a NewsRepository and a NewsViewModel.

Tech stack
- Language: Kotlin
- UI: Jetpack Compose
- Networking: Retrofit + Gson
- Architecture: MVVM (ViewModel, Repository, LiveData/StateFlow)
- Theming: Compose Theme (Theme.kt, Color.kt, Type.kt)
- Optional refresh: official Compose pull-to-refresh (no deprecated SwipeRefresh dependency)

Getting started
- Prerequisites: JDK 11+, Android Studio ( Arctic Fox / Chipmunk or newer )
- Open the project in Android Studio and allow Gradle to sync.
- Build and run on an Android device or emulator.

Running and testing
- Open the app to see category tabs (e.g. top, politics, business, etc.).
- Tap a category to fetch and display articles.
- Pull down on the list to refresh the current category (uses the official Compose pull-to-refresh API).
- Tapping an article opens the article in the browser via its link.

Code structure (high level)
- app/src/main/java/com/example/banglanews/view
  - MainActivity.kt: Entry activity; hosts the NewsApp composable and the top-level Scaffold with TopAppBar.
  - NewsCard: Composable for rendering a single news item (title, description, and Read Full Article button). Implementations may live here or in a separate file depending on refactors.
- app/src/main/java/com/example/banglanews/viewmodel
  - NewsViewModel.kt: Holds articles state and loading state; fetches data via NewsRepository.
- app/src/main/java/com/example/banglanews/model
  - NewsApiService.kt, RetrofitClient.kt, NewsResponse.kt, NewsArticle.kt: Networking and data models.
- app/src/main/res/values
  - strings.xml, colors.xml, themes.xml: UI strings and theming.

Notes and best practices
- API key: The current implementation uses a hardcoded API key. For production, consider moving credentials to a secure config (env vars, Gradle properties, or a secure vault) and obfuscating them as needed.
- Dependency updates: We migrated away from the deprecated Accompanist SwipeRefresh in favor of the official Compose PullRefresh API. See the build.gradle files and theme for related changes.
- Status bar and insets: The app uses a Scaffold with a TopAppBar to ensure proper insets handling across devices with notches and various status bar heights.

Known limitations and future work
- Per-category caching and more robust error handling (e.g. empty results, network errors).
- Pagination or infinite scrolling for large result sets.
- CI/CD integration and automated tests (unit + instrumentation tests).

Contributing
- If you want to contribute, please fork the repo, create a feature branch, and submit a pull request with a concise description of changes.

License
- This project is a sample app; licensing is not specified here.
