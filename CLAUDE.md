# CLAUDE.md

This is a Kotlin Multiplatform (KMP) task management app targeting Android, iOS, and Desktop.

## Build & Development Commands

```bash
# Build
./gradlew :desktop-app:assemble       # Desktop (fastest)

# Test
./gradlew desktopTest                 # Desktop unit tests (fastest for iteration)

# Code quality
./gradlew ktlintFormat                # Auto-fix lint
./gradlew :desktop-app:ktlint         # Lint check
./gradlew :desktop-app:detekt         # Static analysis
./gradlew :desktop-app:check          # ktlint + detekt
```

## Architecture

Hexagonal architecture with inward-pointing dependencies:

```
Platform Apps (app, ios-app, desktop-app)
  └─ shared (entry point, Koin init, AlkaaMultiplatformApp)
      └─ features (UI/presentation, ViewModels)
          └─ domain (use cases, models, repository interfaces)
              └─ data (repository impl, local/SQLDelight, datastore)
```

**Feature modules use API/impl split**: e.g. `features:task-api` exposes interfaces, `features:task`
provides implementations bound via Koin. Other features depend only on API modules.

## Module Structure

- `app/`, `desktop-app/`, `ios-app/` — Platform entry points; wire the multiplatform app into each target
- `shared/` — Multiplatform app root; Koin initialization via `KoinHelper.kt`, hosts `AlkaaMultiplatformApp`
- `features/` — All feature modules; each feature has an API module (shared interfaces) and an impl module (Koin-bound implementations). Features: task, category, alarm, search, preference, tracker, home, navigation, glance
- `domain/` — Use cases, domain models, and repository interfaces; no framework dependencies
- `data/repository/` — Repository implementations with mappers bridging domain and local models
- `data/local/` — SQLDelight database schema, DAOs, and local data sources
- `data/datastore/` — User preferences stored via DataStore (`alkaa_settings.preferences_pb`)
- `libraries/` — Shared utilities used across features: designsystem (Kuvio), coroutines, navigation, test, parcelable, permission, appstate
- `plugins/` — Gradle convention plugins (`com.escodro.multiplatform`, `com.escodro.kotlin-quality`, `com.escodro.kotlin-parcelable`) that standardize build configuration across modules
- `resources/` — Compose Multiplatform shared resources (strings, drawables) for all platforms

## KMP Conventions

**Dependencies — Version Catalog**: All dependencies are declared in `gradle/libs.versions.toml` and
referenced as `alias(libs.plugins.*)` or `implementation(libs.*)` in build files. Never use raw
coordinate strings.

**Build config — Convention Plugins**: Apply the appropriate plugin instead of writing raw build
boilerplate. Common plugins:
- `alias(libs.plugins.escodro.multiplatform)` — standard KMP module setup
- `alias(libs.plugins.escodro.kotlin.parcelable)` — adds `@CommonParcelize` support
- `alias(libs.plugins.compose)` + `alias(libs.plugins.compose.compiler)` — Compose Multiplatform

**Platform-specific code — `expect`/`actual`**: Use `expect` declarations in `commonMain` and `actual`
implementations in platform source sets (`androidMain`, `iosMain`, `jvmMain`). Never use
platform-specific APIs directly in `commonMain`.
