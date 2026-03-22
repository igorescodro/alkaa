# Feature API Module Design

A `<name>-api` module is **only needed when another feature will depend on this one**. Default to a single module and add the API split only when cross-feature sharing is required.

**In `<name>-api`** (no implementations allowed):
- Abstract ViewModel base classes
- Sealed state classes shared across features
- View model data classes exposed to other features
- Screen interfaces (when a feature injects a screen into another feature's navigation)

**In `<name>`** (private to the feature):
- Concrete ViewModel implementations
- Composables and screen implementations
- Mappers (repository and view)
- NavGraph implementation
- Koin module
