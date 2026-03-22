# Interactor Reference

An **Interactor** is a domain-defined interface that lets a use case trigger side effects in another
feature — without importing that feature directly.

## 1a. Concept

Use an Interactor when a use case needs to cross feature boundaries. Examples:
- Completing a task → cancel its alarm (`AlarmInteractor`) + dismiss its notification (`NotificationInteractor`)
- Adding or updating a task → refresh the home screen widget (`GlanceInteractor`)

**Benefits:** no cyclical dependencies, no feature-to-feature coupling, business logic stays centralized in the domain layer.

**Definition:**
- **What:** a domain-defined interface whose implementation lives in the owning feature module
- **Where:** interface in `domain/interactor/`, implementation in `features/<name>/interactor/`
- **When:** a use case needs to trigger an action in another feature (e.g., completing a task must
  also cancel its alarm and dismiss its notification)

## 1b. Interface

Location: `domain/src/commonMain/kotlin/com/escodro/domain/interactor/`

```kotlin
// Defines the contract in the domain layer.
// Only domain models as parameters — never feature-layer types.
// fun for fire-and-forget; suspend fun when the caller awaits completion.
interface AlarmInteractor {
    fun schedule(task: Task, timeInMillis: Long)
    fun cancel(task: Task)
    fun update(task: Task)
}
```

**Rules:**
- Only domain models as parameters — never feature-layer types
- `fun` for fire-and-forget; `suspend fun` when the caller awaits completion
- One interface per distinct cross-feature contract (a single feature may provide multiple —
  e.g., `alarm` provides both `AlarmInteractor` and `NotificationInteractor`)

## 1c. Implementation

Location: `features/<name>/src/commonMain/kotlin/com/escodro/<name>/interactor/` for
cross-platform impls; `features/<name>/src/androidMain/...` for Android-only impls (only when the implementation uses Android-only APIs with no `commonMain` equivalent).

```kotlin
// internal — the domain only knows the interface, never the implementation.
internal class AlarmInteractorImpl(
    private val notificationScheduler: NotificationScheduler,
    private val mapper: TaskMapper,
) : AlarmInteractor {

    override fun cancel(task: Task) {
        val alarmTask = mapper.fromDomain(task)
        notificationScheduler.cancelTaskNotification(alarmTask)
    }
}
```

**Rules:**
- `internal` visibility — never `public`
- Map domain models to feature-layer types using the feature's own mapper
- Inject feature-specific dependencies (schedulers, platform APIs) here
- Android-only implementations intentionally have no `commonMain` counterpart — the domain
  receives `null` on non-Android platforms because no module registers the binding there.
  This is by design, not an oversight.

## 1d. DI Registration

**Feature module** binds `*Impl` to the domain interface:

```kotlin
// features/alarm/di/AlarmModule.kt
factoryOf(::AlarmInteractorImpl) bind AlarmInteractor::class
factoryOf(::NotificationInteractorImpl) bind NotificationInteractor::class
```

**Domain module** chooses `get()` or `getOrNull()` based on one of three criteria. Note: `factoryOf(::CompleteTask)` in the domain module automatically resolves non-nullable constructor parameters as `get()` and nullable parameters as `getOrNull()`, so both styles result in the same behavior:

| Criterion | Use |
|-----------|-----|
| Structurally required — the use case cannot function correctly without it | `get()` |
| Platform-optional — no binding exists on non-Android platforms (e.g., `GlanceInteractor`) | `getOrNull()` + nullable parameter |
| Optional for graceful degradation — the use case can function without the side effect | `getOrNull()` + nullable parameter |

Examples from `DomainModule.kt`:
- `CompleteTask` uses `get()` for both `AlarmInteractor` and `NotificationInteractor` — both are
  structurally required (a completed task must cancel its alarm and dismiss its notification).
- `UpdateTaskTitle` uses `get()` for `AlarmInteractor` (must reschedule) and `getOrNull()` for
  `GlanceInteractor` (Android-only widget).
- `DeleteTask` uses `getOrNull()` for `AlarmInteractor` (graceful degradation — can delete
  without cancelling an alarm).

## 1e. Fakes for Testing

Location: `domain/src/commonTest/kotlin/com/escodro/domain/usecase/fake/`

```kotlin
internal class AlarmInteractorFake : AlarmInteractor {
    private val alarmMap: MutableMap<Long, Long> = mutableMapOf()

    var updatedTask: Task? = null

    override fun schedule(task: Task, timeInMillis: Long) {
        alarmMap[task.id] = timeInMillis
    }

    override fun cancel(task: Task) {
        alarmMap.remove(task.id)
    }

    override fun update(task: Task) {
        updatedTask = task
    }

    fun isAlarmScheduled(alarmId: Long): Boolean =
        alarmMap.contains(alarmId)

    fun getAlarmTime(alarmId: Long): Long? =
        alarmMap[alarmId]

    fun clear() {
        alarmMap.clear()
        updatedTask = null
    }
}
```

**Rules:**
- Implement the domain interface — no real platform code
- Expose state-inspection helpers (e.g., `isAlarmScheduled()`) so tests can assert side effects
- Expose a `clear()` reset method and call it in `@BeforeTest`
