# Diagnostics

The app provides `TracerDiagnostics`, an adapter from the reusable `Diagnostics`
API to this app's Tracer project. `AuthVk` receives it through
`VkAuthViewModelFactory` and the app's `Auth` instance; no Tracer dependency is
added to reusable business modules.

The app provides the Tracer adapter to AuthVk and OwlGame. OwlGame receives the
same `Diagnostics` instance through `OwlGameModule`; its sync, selected gameplay
operations, and background animal-arrival check emit breadcrumbs and non-fatal
reports for handled failures. Todos coverage remains at the app-level
foreground sync handler. Expected Todos network failures and the
`APP_UPDATE_REQUIRED` response are breadcrumbs only.

AuthVk and OwlGame exclude coroutine cancellation from non-fatal reporting.
Unexpected handled errors and selected server failures are sent as non-fatals.

Tracer reports deliberately omit exception messages and causes because they can
contain server or user data. The original stack trace is retained. Breadcrumbs
contain operation stages, safe HTTP statuses, and exception class names only.
Never add tokens, VK IDs, device IDs, session values, encryption keys, request or
response bodies, note contents, or game data to diagnostic events.

## Build and publish order

`pro.progr:diagnostics:0.1.0-alpha` must already be available in GitHub Packages.
Then build and publish AuthVk as `pro.progr:auth-vk:1.1.2-alpha` and OwlGame as
`pro.progr:owlgame:0.0.3-alpha`. After both packages are published, sync and
build SaveAnOwl, which resolves those versions.

## Manual smoke check

1. Run the debug app and complete a successful VK sign-in. This exercises the
   AuthVk breadcrumbs and should leave no error report.
2. In a test-only run, simulate a handled unexpected failure and call
   `diagnostics.reportNonFatal(...)`. Check that the event appears in Tracer and
   that its Logs tab contains earlier breadcrumbs.
3. Confirm that no diagnostic text contains a VK token, user/device identifier,
   session value, personal key, or request/response body.

Do not leave a forced failure path enabled in a release build.
