# CLAUDE.md

## Project Overview

Java SDK for the CTA Bus Tracker, Train Tracker, and Customer Alerts APIs. Published to Maven Central. Consumers instantiate `BusApi`, `TrainApi`, or `AlertApi` via their respective builders and access sub-APIs from there.

## Entry Points

```java
BusApi busApi = BusApi.builder("apiKey").build();
TrainApi trainApi = TrainApi.builder("apiKey").build();
AlertApi alertApi = AlertApi.builder().build();
```

`BusApi`/`TrainApi` builders require an API key and accept an optional `.host(String)` override; `TrainApi.Builder` also accepts `.stationsUrl(String)`. The CTA Customer Alerts API is unauthenticated, so `AlertApi.builder()` takes no API key and only accepts an optional `.host(String)` override.

## API Surface

**Bus (`BusApi`)**
- `vehicles()` — `VehiclesApi`
- `routes()` — `RoutesApi`
- `directions()` — `DirectionsApi`
- `stops()` — `StopsApi`
- `patterns()` — `PatternsApi`
- `predictions()` — `PredictionsApi`
- `locales()` — `LocalesApi`
- `detours()` — `DetoursApi`
- `systemTime()` — current API system time

**Train (`TrainApi`)**
- `stations()` — `StationsApi`
- `arrivals()` — `ArrivalsApi`
- `follow()` — `FollowApi`
- `locations()` — `LocationsApi`

**Alert (`AlertApi`)**
- `routeStatus()` — `RouteStatusApi`
- `detailedAlerts()` — `DetailedAlertsApi`

## Package Layout

Transit type (`bus`, `train`, `alert`) then feature then layer:

```
com.cta4j.bus.route/
  RoutesApi.java              ← public interface
  model/Route.java            ← public domain model
  internal/
    impl/RoutesApiImpl.java   ← package-private implementation
    wire/CtaRoute.java        ← Jackson deserialization record
    mapper/RouteMapper.java   ← MapStruct mapper
```

Shared types live in `bus/common/`, `train/common/`, or `alert/common/`. Cross-cutting types (geo, exceptions) live in `common/` or `exception/`.

## Wire Layer Conventions

- All wire records are `@ApiStatus.Internal` and not part of the public API.
- **Bus**: each feature has a typed `Cta<Feature>BustimeResponse` record (envelope field `bustimeResponse`, mapped from `"bustime-response"`) with `@Nullable` fields for both the data list and a typed `Cta<Feature>Error` list. Each `Cta<Feature>Error` implements `CtaError` and overrides `notFound()` using its own typed fields (e.g. `rt`, `stpid`, `vid`) to identify which input caused the error — do not collapse these into a generic map.
- **Train**: there is no shared `CtaError`-style record. Each feature's wire response record carries `errCd`/`errNm` fields directly; `errCd` is parsed to an `int` and mapped via `<Feature>ErrorCode.fromCode(int)` to a feature-specific enum (e.g. `ArrivalsErrorCode`) whose constants identify resource-specific ("not found") codes.
- **Alert**: like Train, there is no shared `CtaError`-style record, and each feature's error shape is its own — do not assume `RouteStatus` and `DetailedAlerts` match each other. `CtaRoutes` (route status) carries `ErrorCode`/`ErrorMessage` as `List<String>`, since the CTA API can return multiple distinct codes for a single request; `CtaAlerts` (detailed alerts) carries a single non-nullable `ErrorCode` `String` and a `@Nullable ErrorMessage` `String`. Both map their code(s) via a feature-specific `<Feature>ErrorCode.fromCode(int)` enum (`RouteStatusErrorCode`, `DetailedAlertsErrorCode`), following the Train pattern.
- All wire records use `@JsonIgnoreProperties(ignoreUnknown = true)`.
- **Bus and Train** share a single generic `CtaResponse<T>` record per module (`bus/common/internal/wire/CtaResponse`, `train/common/internal/wire/CtaResponse`) with a fixed field name — `bustimeResponse` (bus) or `ctatt` (train).
- **Alert** has no shared generic envelope type; each feature declares its own concretely-typed response record with its own field name (e.g. `CtaRouteStatusResponse.ctaRoutes` mapped from `"CTARoutes"`, `CtaDetailedAlertsResponse.ctaAlerts` mapped from `"CTAAlerts"`).

## Error Handling Pattern

**Bus** `*ApiImpl` classes returning a `List` follow this pattern in `makeRequest`:

1. If the data list is non-null and non-empty → map and return it.
2. Otherwise, call `BusApiUtils.checkErrors(errors, endpoint)` (`bus/common/internal/util/BusApiUtils`): it logs a warn and returns if the error list is null/empty, returns if every error's `notFound()` is `true`, or throws `Cta4jBusException` otherwise.
3. Return `List.of()`.

`SystemTimeApiImpl` is the one exception: it returns a single `Instant`, not a `List`, so a missing value has no valid "empty" result — it throws directly instead of calling `BusApiUtils.checkErrors`.

**Train** `*ApiImpl` classes follow a related but distinct pattern (no shared helper — each impl inlines it with its own exception type): parse `errCd` to the feature's `*ErrorCode` enum; if it's a resource-specific not-found code → return an empty result (or `Optional.empty()`); if it isn't `OK` → throw the feature-specific exception (e.g. `Cta4jArrivalsException`) using `errNm` as the message, falling back to a default message when `errNm` is `null` or blank.

**Alert** `*ApiImpl` classes follow the same inlined, no-shared-helper pattern as Train, but each feature's `errCd`/`errNm` shape differs (see Wire Layer Conventions above): `RouteStatusApiImpl` reads the first of a possibly-multi-value error-code list (logging a warn if more than one distinct code is present) and falls back to `"An unknown error occurred."` when the message is null/blank; `DetailedAlertsApiImpl` reads the single scalar `errCd`/`errNm` directly. Both throw their feature-specific exception (`Cta4jRouteStatusException`/`Cta4jDetailedAlertsException`, both extending the shared `Cta4jAlertException`) for any code other than `OK`/not-found.

`find*` methods return an empty `List` (or `Optional.empty()`) for not-found; they never throw for missing resources.

## Annotations

- Annotations always stack one per line above the declaration.
- They should adhere to the following order: Jackson/framework → `@ApiStatus.Internal` → `@NullMarked`.
- This applies to classes, interfaces, records, fields, and methods alike.

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaFoo(...) { }
```

- **Exception — Record components:** A component with a single annotation may keep it inline with the type
  (`@Nullable String foo`) instead of stacking. Two or more annotations on a component still stack one per
  line above the type, and when any component in a record stacks, blank-line-separate every component in
  that record (see `CtaLocation` vs. the single-annotation `CtaStation`).

## Null Safety

- All classes, records, interfaces, and enums in `src/main` must have `@NullMarked`.
- Use `@Nullable` on fields or parameters that can be null.
- Guard all public method parameters with `Objects.requireNonNull`.
- Defensively copy all incoming `List` parameters with `List.copyOf(...)` in compact constructors.

## Mappers

MapStruct mappers live in `internal/mapper/`. They are interfaces annotated with `@Mapper` and accessed via a public `INSTANCE` field. Mapper implementations are generated at compile time — do not write them by hand.

## Code Style

- `final` on all classes not designed for extension.
- Always reference instance fields and methods with `this.`.
- No comments unless the why is non-obvious.
- No `Optional` for fields or parameters. Use method overloading or `@Nullable` fields instead.
- Prefer `List.of()` for empty returns; use `List.copyOf()` for defensive copies.
- All files must end with a trailing newline.

## Javadoc Conventions

This project uses Markdown documentation comments (`///`, JEP 467, JDK 23+)
instead of traditional `/** */` HTML Javadoc. Do not use `{@code}`, `{@link}`,
or HTML tags — use plain Markdown (backticks, `[Type]` links, etc.).

References:
- Content/style conventions (summary sentence, tag usage):
  https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html
- Markdown comment syntax (`///`, CommonMark extensions, JDK 23+):
  https://docs.oracle.com/en/java/javase/25/javadoc/using-markdown-documentation-comments.html

- **Summary sentence:** The first line is a standalone summary fragment
  ending in a period, third-person descriptive ("Returns the active
  arrivals for a station," not "This method returns...").
- **Single-item accessor verb:** Always "Returns the X" — no exceptions.
  This applies uniformly to every single-value accessor, including
  wire-code accessors on domain-value enums (e.g. `getCode()` on
  `TransitMode`, `DynamicAction`, `TrainLine`) and numeric/status-code
  accessors on error-code enums or exceptions alike. Do not use "Gets the
  X" to distinguish the two.
- **Builder creator methods:** One template for every builder, top-level
  client builders and query-parameter builders alike — no terser variant.
  The static `builder(...)` method: "Creates a new `Builder` for
  constructing a/an `X`.", `@return` tag "a new `Builder`". The `build()`
  method: "Builds a configured `X` instance.", `@return` tag "a new `X`".
- **Builder setter `@return`:** Always backticked, "this `Builder` instance" —
  never the unbacked "this builder instance" variant.
- **Tag order:** `@param` → `@return` → `@deprecated` → `@since` → `@throws`
  → `@see`.
- **@param / @throws descriptions:** Lowercase phrase, no trailing period.
- **Code references:** Use backtick spans (`` `RoutesApi` ``, `` `List<Route>` ``)
  instead of `{@code}`. Use Markdown reference links (`[RoutesApi]`) instead
  of `{@link}` only when the cross-reference meaningfully aids understanding.
- **What gets documented:** Public interfaces (`*Api`), public domain models
  (`model/`), and builders always. Wire records (`internal/wire/`), mappers,
  and `*ApiImpl` classes are `@ApiStatus.Internal` and are not documented
  unless the "why" is non-obvious (per Code Style).
- **Package docs:** Every package containing at least one non-internal
  type (public `*Api` interfaces, public domain models, builders — per
  "What gets documented" above) gets a `package-info.java` with a
  one-paragraph summary of the package's responsibility, written in the
  same Markdown style. Packages containing only `internal/wire`, mapper,
  or `*ApiImpl` classes do not require a package-info.java.
