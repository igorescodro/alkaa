# SQLDelight Schema

**Location:** `data/local/src/commonMain/sqldelight/com/escodro/local/<Name>.sq`

```sql
-- Import custom Kotlin types at the top
import kotlin.Boolean;
import kotlinx.datetime.LocalDateTime;

CREATE TABLE IF NOT EXISTS Category (
    `category_id`    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `category_name`  TEXT NOT NULL,
    `category_color` TEXT NOT NULL
);

selectAll:
SELECT * FROM Category;

selectByCategoryId:
SELECT * FROM Category WHERE category_id = ?;

insert:
INSERT INTO Category (category_name, category_color) VALUES (?, ?);

update:
UPDATE Category SET category_name = ?, category_color = ? WHERE category_id = ?;

delete:
DELETE FROM Category WHERE category_id = ?;

cleanTable:
DELETE FROM Category;
```

**Rules:**
- Field names prefixed with the table name in snake_case (e.g., `category_id`, `task_title`)
- Custom Kotlin types declared via `INTEGER AS MyType` with a matching adapter in `DatabaseProvider`
- Every table needs `insert:`, `update:`, `delete:`, and `cleanTable:` — `cleanTable:` is required for E2E test teardown
- FOREIGN KEY with `ON DELETE CASCADE` for child tables (e.g., Task referencing Category)
- For inserts that need to return the new ID: add a `lastInsertedId: SELECT LAST_INSERT_ROWID();` query
