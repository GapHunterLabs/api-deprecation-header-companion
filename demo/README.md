# Demo data for screenshots

`OrderController.java` — `legacyOrders` is deprecated with no header
signal (flagged), `legacyOrdersV2` sets both headers correctly (not
flagged).

## How to get the screenshot

1. `./gradlew runIde` from `api-deprecation-header-companion`, open
   this `demo/` folder as the project.
2. Full Screen, open `OrderController.java` — a warning icon should
   appear on `legacyOrders` but not on `legacyOrdersV2`.
3. Screenshot with both methods visible, save into
   `api-deprecation-header-companion/docs/screenshots/`. Close the
   sandbox.
