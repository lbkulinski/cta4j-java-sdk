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
- Each feature has a typed `Cta<Feature>BustimeResponse` record with `@Nullable` fields for both the data list and the error list.
- Each feature has a typed `Cta<Feature>Error` record implementing `CtaError` (bus) or extending the train equivalent. Typed error fields (e.g. `rt`, `stpid`, `vid`) identify which input caused the error and drive not-found decisions — do not collapse these into a generic map.
- All wire records use `@JsonIgnoreProperties(ignoreUnknown = true)`.
- The outer envelope is `CtaResponse<T>` with a `bustimeResponse` field mapped from `"bustime-response"`.

## Error Handling Pattern

All `*ApiImpl` classes follow this pattern in `makeRequest` / the main method body:

1. If the data list is non-null and non-empty → map and return it.
2. If the error list is null or empty → log a warn and return `List.of()`.
3. Check whether all errors are resource-specific (not-found) by inspecting typed fields (e.g. `error.rt() != null`, `error.stpid() != null || error.vid() != null`). If so → return `List.of()`.
4. Otherwise → throw `Cta4jException` via `ApiUtils.buildErrorMessage(...)`.

`find*` methods return an empty `List` for not-found; they never throw for missing resources.

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