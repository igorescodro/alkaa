# DB Migrations

**Trigger:** Any structural change to an existing table, or a new table added to an app with existing users. Always update the `.sq` file first (target state), then add a migration file.

SQLDelight derives the DB version from the count of `.sqm` files. File `N.sqm` upgrades version N → N+1. To find the next file number, count existing `.sqm` files.

```
data/local/src/commonMain/sqldelight/
├── com/escodro/local/          ← .sq files (always reflect the final schema)
└── migrations/
    ├── 1.sqm                   ← upgrades version 1 → 2
    ├── 2.sqm                   ← upgrades version 2 → 3
    └── 3.sqm                   ← upgrades version 3 → 4
```

**Add a column** — most common case:
```sql
ALTER TABLE Task ADD COLUMN task_priority INTEGER NOT NULL DEFAULT 0;
```

**Recreate a table** — required when dropping a column, changing a constraint, or adding `AUTOINCREMENT`:
```sql
ALTER TABLE Category RENAME TO Category_temp;
CREATE TABLE IF NOT EXISTS Category (
    `category_id`    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `category_name`  TEXT NOT NULL,
    `category_color` TEXT NOT NULL
);
INSERT INTO Category(category_id, category_name, category_color)
SELECT category_id, category_name, category_color FROM Category_temp;
DROP TABLE Category_temp;
```

**Rules:**
- `.sqm` files contain raw SQL only — no query labels
- `NOT NULL` columns require `DEFAULT <value>` when added to existing tables
- Never edit an existing `.sqm` file — create a new one for each change
- No `BEGIN`/`END TRANSACTION` — the driver manages the transaction
- Wrong file number causes silent issues: number must equal the count of existing `.sqm` files

**Verify:**
```bash
./gradlew check
```
This includes `verifySqlDelightMigration`, which confirms the migration output matches the `.sq` schema.
