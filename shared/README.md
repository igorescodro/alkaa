# Shared

The **Shared** module is the core of the Kotlin Multiplatform project. It integrates all the layers (Domain, Data, Features, and Libraries) and provides a unified entry point for the platform-specific app modules (Android, iOS, Desktop).

## Responsibility

* Dependency Injection orchestration for all modules.
* Exposure of the shared logic and UI to the platform-specific apps.
