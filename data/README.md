# Data

Layer responsible for providing data to the application. It handles data fetching from different sources and provides a unified representation through repositories.

## Modules

* **repository**: Implementation of the domain repositories, orchestrating different data sources.
* **local**: Local database implementation using SQLDelight.
* **datastore**: Key-value storage using Jetpack DataStore for user preferences.
