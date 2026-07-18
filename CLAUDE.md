# CLAUDE.md

## Project Overview

Java SDK for the CTA Bus Tracker and Train Tracker APIs. Published to Maven Central. Consumers instantiate `BusApi` or `TrainApi` via their respective builders and access sub-APIs from there.

## Entry Points

```java
BusApi busApi = BusApi.builder("apiKey").build();
TrainApi trainApi = TrainApi.builder("apiKey").build();
```

Both builders accept an optional `.host(String)` override; `TrainApi.Builder` also accepts `.stationsUrl(String)`.

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

## Package Layout

Transit type (`bus`, `train`) then feature then layer:

```
com.cta4j.bus.route/
  RoutesApi.java              ← public interface
  model/Route.java            ← public domain model
  internal/
    impl/RoutesApiImpl.java   ← package-private implementation
    wire/CtaRoute.java        ← Jackson deserialization record
    mapper/RouteMapper.java   ← MapStruct mapper
```

Shared types live in `bus/common/` or `train/common/`. Cross-cutting types (geo, exceptions) live in `common/` or `exception/`.

## Wire Layer Conventions

- All wire records are `@ApiStatus.Internal` and not part of the public API.
- **Bus**: each feature has a typed `Cta<Feature>BustimeResponse` record (envelope field `bustimeResponse`, mapped from `"bustime-response"`) with `@Nullable` fields for both the data list and a typed `Cta<Feature>Error` list. Each `Cta<Feature>Error` implements `CtaError` and overrides `notFound()` using its own typed fields (e.g. `rt`, `stpid`, `vid`) to identify which input caused the error — do not collapse these into a generic map.
- **Train**: there is no shared `CtaError`-style record. Each feature's wire response record carries `errCd`/`errNm` fields directly; `errCd` is parsed to an `int` and mapped via `<Feature>ErrorCode.fromCode(int)` to a feature-specific enum (e.g. `ArrivalsErrorCode`) whose constants identify resource-specific ("not found") codes.
- All wire records use `@JsonIgnoreProperties(ignoreUnknown = true)`.
- The outer envelope is `CtaResponse<T>`, with a `bustimeResponse` field (bus) or a `ctatt` field (train).

## Error Handling Pattern

**Bus** `*ApiImpl` classes returning a `List` follow this pattern in `makeRequest`:

1. If the data list is non-null and non-empty → map and return it.
2. Otherwise, call `ApiUtils.checkErrors(errors, endpoint)` (`bus/common/internal/util/ApiUtils`): it logs a warn and returns if the error list is null/empty, returns if every error's `notFound()` is `true`, or throws `Cta4jBusException` otherwise.
3. Return `List.of()`.

`SystemTimeApiImpl` is the one exception: it returns a single `Instant`, not a `List`, so a missing value has no valid "empty" result — it throws directly instead of calling `ApiUtils.checkErrors`.

**Train** `*ApiImpl` classes follow a related but distinct pattern (no shared helper — each impl inlines it with its own exception type): parse `errCd` to the feature's `*ErrorCode` enum; if it's a resource-specific not-found code → return an empty result (or `Optional.empty()`); if it isn't `OK` → throw the feature-specific exception (e.g. `Cta4jArrivalsException`) using `errNm` as the message, falling back to a default message when `errNm` is `null` or blank.

`find*` methods return an empty `List` (or `Optional.empty()`) for not-found; they never throw for missing resources.

## Annotation Ordering

Always apply annotations in this order: **Jackson/framework → `@ApiStatus.Internal` → `@NullMarked`**

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaFoo(...) { }
```

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
