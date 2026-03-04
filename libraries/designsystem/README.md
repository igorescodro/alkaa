# Design System

The "Source of Truth" for the application's user interface.

## Kuvio

The new component library is called **Kuvio** — from the Finnish word *kuvio*, meaning **pattern**,
**figure**, or **design**. The name reflects the library's purpose: providing the visual building
blocks and repeatable patterns that shape the app's interface.

All Kuvio components live under the `components/kuvio/` package and are prefixed with `Kuvio` (e.g.
`KuvioDialog`, `KuvioBadgeCounter`).

## Responsibility

* **Theme**: Definition of colors, typography, and shapes (Material Design 3).
* **Components**: Reusable UI components used across all features (buttons, text fields, dialogs,
  etc.).
    * **Kuvio**: The new design system component library, replacing legacy components over time.
* **Icons**: Centralized access to app icons.
