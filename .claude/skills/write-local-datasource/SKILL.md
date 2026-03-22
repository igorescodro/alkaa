---
name: write-local-datasource
description: Use when a new feature needs to persist data locally in Alkaa — triggers on tasks like "add database support", "create a new table", "store this data in SQLDelight", or when write-feature Phase 2 requires a new entity in the local database.
---

# Write Local DataSource

## Overview

The local data layer spans eight phases per entity: DataSource interface, SQLDelight schema, DB migrations, DAO interface, DAO implementation, local mapper, LocalDataSource implementation, and DI registration. All phases must follow strict conventions.

## Phases

1. **DataSource Interface** — Contract the RepositoryImpl depends on; uses repository models (not local types) → see `references/DATASOURCE_INTERFACE.md`
2. **SQLDelight Schema** — `.sq` schema file with named queries and field prefixes → see `references/SCHEMA.md`
3. **DB Migrations** — `.sqm` file required for any structural change to an existing table → see `references/MIGRATIONS.md`
4. **DAO Interface** — Observable streams vs. single reads contract → see `references/DAO.md`
5. **DAO Implementation** — `asFlow().mapToList()` and `executeAsOneOrNull()` patterns → see `references/DAO.md`
6. **Local Mapper** — SQLDelight type ↔ repository model (`toRepo`/`fromRepo`) → see `references/MAPPER.md`
7. **LocalDataSource Implementation** — Injects DAO + mapper; never accesses `*Queries` directly → see `references/LOCAL_DATASOURCE.md`
8. **DI Registration** — `singleOf` for DAOs/DataSources, `factoryOf` for mappers → see `references/DI.md`

## Rules

| Rule | Details |
|------|---------|
| **Flow vs. suspend in DAO** | `Flow<List<T>>` for reactive reads; `suspend` for mutations and point-in-time reads |
| **No direct Queries access** | LocalDataSource injects the DAO — never `*Queries` directly |
| **executeAsOneOrNull** | Always nullable for single reads — never `executeAsOne()` |
| **cleanTable required** | Every table needs a `cleanTable:` query for E2E test teardown |
| **Field prefix** | Column names prefixed with table name in snake_case (e.g., `category_id`) |
| **DI scope** | `singleOf` for DataSources and DAOs; `factoryOf` for mappers |
| **Migration required** | Any structural change to an existing table needs a `.sqm` file |

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| `suspend fun findAll()` for a Flow return | Use `fun findAll(): Flow<List<T>>` — no suspend for reactive reads |
| Calling `.first()` in DaoImpl for a Flow return | Return `Flow` directly; callers decide when to collect |
| Using `executeAsOne()` for single reads | Always `executeAsOneOrNull()` — assume nullable |
| Omitting `cleanTable:` in the `.sq` file | Required for E2E tests to reset state between runs |
| Local mapper in `data/repository/mapper/` | Belongs in `data/local/mapper/` with `toRepo`/`fromRepo` |
| Registering DataSource or DAO as `factoryOf` | Always `singleOf` — they share database state |
| Adding `DatabaseProvider` registration again | Already registered once; duplicate causes a Koin conflict |
| Accessing `*Queries` in LocalDataSource | Injects the DAO — never the queries object |
| Changing `CREATE TABLE` without a `.sqm` file | Existing users never see the change; always add a migration |
| Adding a `NOT NULL` column without `DEFAULT` | SQLite rejects the statement on existing rows |
| Editing an existing `.sqm` file | `.sqm` files are immutable; create a new file for each change |
| Wrong `.sqm` file number | Number must equal the count of existing `.sqm` files |
| Wrapping migration SQL in `BEGIN`/`END TRANSACTION` | The driver manages the transaction; wrapping can cause crashes |

## Testing

After completing the local data layer, use the `write-unit-tests` skill to test data sources and use cases. Use the `write-e2e-tests` skill for full-flow coverage — E2E tests use DAOs directly (`by inject()`) to seed and clean data.

## Verify

```bash
.claude/skills/write-local-datasource/scripts/verify_migrations.sh
```
