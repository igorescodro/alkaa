# DI Registration and Custom Type Adapters

## DI Registration

Add to `data/local/src/commonMain/kotlin/com/escodro/local/di/LocalModule.kt`:

```kotlin
val localModule = module {
    // DataSources — singleOf, bind to interface
    singleOf(::CategoryLocalDataSource) bind CategoryDataSource::class

    // Mappers — factoryOf, no interface binding
    factoryOf(::CategoryMapper)

    // DAOs — singleOf, bind to interface
    singleOf(::CategoryDaoImpl) bind CategoryDao::class

    // DatabaseProvider — registered once inside localModule
    singleOf(::DatabaseProvider)
    includes(platformLocalModule)
}
```

**DI rules:**
- `singleOf` for DataSources, DAOs, and DatabaseProvider — they hold or share database state
- `factoryOf` for Mappers — stateless, cheap to create, no interface binding needed
- `DatabaseProvider` is registered once inside `localModule` via `singleOf(::DatabaseProvider)` — do NOT add a second registration to the same module block or to any platform module

---

## Custom Type Adapters

If the new schema uses `INTEGER AS MyType`, register the adapter in `DatabaseProvider.createDatabase()`:

```kotlin
val database = AlkaaDatabase(
    driver = driver,
    TaskAdapter = Task.Adapter(
        task_due_dateAdapter = dateTimeAdapter,
        task_alarm_intervalAdapter = alarmIntervalAdapter,
    ),
)
```

Adapters live in `data/local/src/commonMain/kotlin/com/escodro/local/converter/`.
