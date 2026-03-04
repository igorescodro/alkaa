# CLAUDE.md

This is a Kotlin Multiplatform (KMP) task management app targeting Android, iOS, and Desktop.

## Build & Development Commands

```bash
# Build
./gradlew assemble                    # Build all modules
./gradlew :app:assemble               # Android APK only
./gradlew :desktop-app:assemble       # Desktop only

# Test
./gradlew allTests                    # All platforms
./gradlew desktopTest                 # Desktop (fastest for iteration)
./gradlew connectedAndroidTest        # Android device/emulator

# Code quality (all run via `check`)
./gradlew ktlint                      # Lint check
./gradlew ktlintFormat                # Auto-fix lint
./gradlew detektAll                   # Static analysis
./gradlew check                       # ktlint + detekt + lint
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

- `app/`, `desktop-app/`, `ios-app/` — Platform entry points
- `shared/` — Multiplatform app root, Koin initialization
- `features/` — Feature modules (task, category, alarm, search, preference, tracker, home,
  navigation, glance)
- `domain/` — Use cases, domain models, repository interfaces
- `data/repository/` — Repository implementations with mappers
- `data/local/` — SQLDelight database
- `data/datastore/` — Preferences via DataStore
- `libraries/` — Shared utilities (designsystem, coroutines, navigation, test, parcelable,
  permission, appstate)
- `plugins/` — Gradle convention plugins
- `resources/` — Compose Multiplatform resources

## Key Patterns

**DI (Koin)**: One module per feature/layer, all composed in `shared/.../KoinHelper.kt`. Use
`factoryOf(::Impl) bind Interface::class` for bindings. Platform-specific modules via
`expect val platformTaskModule: Module`.

**Use cases**: Interface + `*Impl` class in domain. Interfaces use `operator fun invoke()` for
callable syntax.

**Mappers**: Separate model types per layer (domain, repository, view). Mapper classes convert
between them.

**Navigation**: Navigation3 with `NavGraph` interface per feature. Each feature provides an
`entry<Destination>` block. Destinations are in `navigation-api`.

**SQLDelight**: Schema in `.sq` files under `data/local/src/commonMain/sqldelight/`.
Platform-specific drivers (Android/Native/JVM).

**DataStore**: Preferences stored in `alkaa_settings.preferences_pb`. Platform-specific path via
expect/actual.

## Testing

- **Framework**: `kotlin("test")` with `runTest` from kotlinx-coroutines-test
- **Dispatcher**: Delegate `CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl()` — handles
  `@BeforeTest`/`@AfterTest` setup
- **Test doubles**: Fake pattern (not mocks). Fakes implement domain interfaces with controllable
  state (e.g. `LoadAllCategoriesFake`)
- **Compose UI**: `libs.compose.uiTest` for UI testing
- **Base class**: `AlkaaTest` expect/actual for platform setup (Robolectric on Android)
- **Fastest feedback loop**: `./gradlew desktopTest` runs JVM tests without emulator

## Code Quality

**.editorconfig** rules for ktlint:

- `ktlint_function_naming_ignore_when_annotated_with = Composable` — allows PascalCase `@Composable`
  functions
- Several standard rules disabled: `property-naming`, `function-signature`,
  `multiline-expression-wrapping`, `class-signature`, `chain-method-continuation`

**detekt.yml** (`config/filters/detekt.yml`):

- `FunctionNaming` ignores `@Composable`
- `MagicNumber` disabled
- `LongMethod` excluded in tests
- Comments rules excluded in test directories
