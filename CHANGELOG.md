# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [6.0.0] - 2026-06-30

### Added

- Comprehensive test suite covering `*ApiImpl` classes (WireMock integration tests), MapStruct mappers, domain
  models, enums, query objects, and qualifiers.
- JaCoCo code coverage reporting integrated into the Maven build.
- `@throws` javadoc documented on every public API method that may throw on error or parse failure, using the
  feature-specific exception type (e.g. `Cta4jBusException` on bus interfaces; `Cta4jArrivalsException`/
  `Cta4jFollowException`/`Cta4jLocationsException`/`Cta4jTrainException` on train interfaces).
- Structured exception hierarchy: `Cta4jBusException` (bus, shared across every feature) and `Cta4jTrainException`
  (train, shared across every feature; used directly by `StationsApiImpl` and train `Qualifiers`), plus
  feature-specific `Cta4jArrivalsException`/`Cta4jFollowException`/`Cta4jLocationsException`, each exposing a
  `getRawErrorCode()`/`getErrorCode()` pair backed by the new `ArrivalsErrorCode`/`FollowErrorCode`/
  `LocationsErrorCode` enums (verified against the CTA Train Tracker API's documented error codes).
- `Cta4jException.getEndpoint()` — a required, always-populated accessor for the endpoint or URL associated with
  any exception the SDK throws.
- `BusApiConstants`/`TrainApiConstants` — public constants holders for each transit type's scheme, default host,
  and per-feature endpoint paths.

### Changed

- `StopsPredictionsQuery` and `VehiclesPredictionsQuery` now enforce the CTA API's 10-ID-per-request limit at
  construction time, throwing `IllegalArgumentException` instead of deferring the error to the API.
- `StopsPredictionsQuery.Builder.maxResults` and `VehiclesPredictionsQuery.Builder.maxResults` parameter type
  changed from `Integer` to `int`.
- Internal wire layer refactored: the generic `CtaBustimeResponse<T>` envelope replaced with feature-specific typed
  response records per endpoint, improving type safety and removing cross-feature coupling.
- `BusApiContext`/`TrainApiContext` replaced with `BusApiConfig`/`TrainApiConfig`, adding `scheme` and `port`
  fields to support configurable HTTP schemes.
- `CtaError` (bus) refactored from a concrete record to an interface, allowing each feature to define its own typed
  error record with resource-specific fields for accurate not-found classification.
- `Cta4jException` reverts to a plain, non-`final` class (see Removed below) so the new exception hierarchy can
  extend it; `getMessage()` now returns CTA's raw reported text unmodified instead of a
  `"Error response from %s: ..."` template, since `getEndpoint()` and the error-code accessors now carry that
  context as structured fields.

### Removed

- `Cta4jException` is no longer `final` and can once again be subclassed — this reverses that change from earlier
  in this same 6.0.0 cycle; see the new exception hierarchy above.
- The ad hoc magic-number not-found checks introduced earlier this cycle (`FollowApiImpl.NOT_FOUND_ERROR_CODE`,
  `ArrivalsApiImpl.INVALID_MAPID_ERROR_CODE`/`INVALID_STPID_ERROR_CODE`), replaced by comparisons against the new
  error-code enums.
- `train.common.internal.wire.CtaError` and `ApiUtils.buildErrorMessage` (both bus and train) — message formatting
  is now owned by the exception classes themselves (`Cta4jBusException` joins bus error messages; train error text
  is passed through unmodified).

## [5.0.0] - 2026-05-22

### Changed

- Renamed `route` field to `routeId` on `Vehicle` and `Prediction` records for consistency with the rest of the API.

### Breaking Changes ⚠️

- `Vehicle.route()` has been renamed to `Vehicle.routeId()`.
- `Prediction.route()` has been renamed to `Prediction.routeId()`.

## [4.1.0] - 2026-05-21

### Added

- `PredictionsApi.findByStopId(String stopId)` — a convenience default method that retrieves predictions for a single
  stop ID without requiring a full `StopsPredictionsQuery` to be constructed.

### Changed

- Bumped `tools.jackson.core:jackson-databind` from **3.1.2** → **3.1.3**
- Bumped `org.apache.httpcomponents.client5:httpclient5-fluent` from **5.6** → **5.6.1**
- Bumped `org.slf4j:slf4j-api` from **2.0.17** → **2.0.18**

## [4.0.3] - 2026-04-14

### Changed

- Bumped `tools.jackson.core:jackson-databind` from **3.1.1** → **3.1.2**

## [4.0.2] - 2026-04-06

### Changed

- Updated project URL from `cta4j.com` to `https://github.com/lbkulinski/cta4j-java-sdk`.
- Bumped `tools.jackson.core:jackson-databind` from **3.1.0** → **3.1.1**

## [4.0.1] - 2026-03-15

### Changed

- Updated project URL from `cta4j.app` to `cta4j.com`.
- Excluded internal packages from published Javadoc.

## [4.0.0] – 2026-03-15

### Added

- JSpecify nullability annotations (using `@NullMarked` / `@Nullable`) on public API types and methods for improved
  nullability contracts.
- `org.jspecify:jspecify:1.0.0` dependency to provide these annotations.
- A new composed Bus API surface (`BusApi`) that exposes domain-specific sub-APIs (e.g. routes, stops, vehicles,
  predictions, detours) instead of a flat client.
  - Dedicated API interfaces for each Bus domain (e.g. `RoutesApi`, `StopsApi`, `VehiclesApi`, `PredictionsApi`,
    `DetoursApi`, etc.), improving discoverability and separation of concerns.
  - Clear separation between public domain models, internal wire models, and implementation details, enabling safer
    evolution of the SDK.
- A new composed Train API surface (`TrainApi`) that similarly organizes train-related functionality into
  domain-specific sub-APIs.
  - Dedicated API interfaces for each Train domain (e.g. `StationsApi`, `ArrivalsApi`, `FollowApi`, etc.), improving
    discoverability and separation of concerns.
  - Clear separation between public domain models, internal wire models, and implementation details, enabling safer
    evolution of the SDK.

### Changed

- Major refactor of the Bus/Train API architecture, replacing the previous flat `Bus/TrainClient`-style interface with
  a hierarchical, capability-based API.
- Method names across the Bus/Train API were aligned to more consistent, domain-driven naming conventions.
- Error-handling behavior for some Bus/Train endpoints was standardized to better reflect CTA API semantics.
- Package structure was reorganized to clearly distinguish public APIs, internal implementations, and wire / CTA
  response models.
- Public APIs are now explicitly null-annotated, tightening contracts and surfacing potential misuse at compile time.
- Bumped `tools.jackson.core:jackson-databind` from **3.0.4** → **3.1.0**
- Bumped `org.jetbrains:annotations` from **26.0.2-1** → **26.1.0**

### Removed

- Deprecated or legacy Bus/Train client entry points that no longer fit the new API model.

### Breaking Changes ⚠️

- This release is not source-compatible with earlier versions.
- Consumers must migrate from the previous Bus/Train client interfaces to the new `BusApi`/`TrainApi` and its sub-APIs.
- Method signatures, return types, and package names for Bus/Train-related APIs have changed as part of the refactor.

## [3.0.4] - 2025-12-30

### Changed

- Bumped `tools.jackson.core:jackson-databind` from **3.0.2** → **3.0.3**
- Bumped `org.apache.httpcomponents.client5:httpclient5` from **5.5.1** → **5.6**
- Bumped `org.apache.maven.plugins:maven-source-plugin` from **3.3.1** → **3.4.0**
- BREAKING CHANGE: Removed `module-info.java`, dropping explicit JPMS module support. The library is now an automatic module, which may change the effective module name and impact consumers using the Java module system.

## [3.0.3] - 2025-11-28

### Changed

- Changed edge case where coordinates could be null in Follow Train API responses.
  - If both the position is `null` and the arrivals list is empty, an empty `Optional` is returned.

## [3.0.2] - 2025-11-28

### Fixed

- Fixed edge case where coordinates could be null in Follow Train API responses.

## [3.0.1] - 2025-11-28

### Changed

- Separated GitHub Actions workflows for build and release.
- Corrected LICENSE file copyright.
- Migrated from `jackson-databind` `2.20.1` to `3.0.2`.

## [3.0.0] - 2025-10-18

### Added

- A new type `UpcomingBusArrival` representing upcoming bus arrival information.
- A new type `BusCoordinates` representing bus coordinates (latitude, longitude, heading).
- An `arrivals` field of type `List<UpcomingBusArrival>` has been added to the `Bus` class to provide information about upcoming bus arrivals.

### Changed

- BREAKING CHANGE: Organized classes into packages by functionality:
  - `com.cta4j.bus` for bus-related classes
  - `com.cta4j.train` for train-related classes
  - `com.cta4j.common` for shared/common classes
- BREAKING CHANGE: Public concrete client classes replaced by interfaces with a fluent `Builder` API (e.g. `BusClient`, `TrainClient`)
  - Create clients via `XxxClient.builder()...build()` instead of instantiating implementation classes.
- BREAKING CHANGE: The `Bus` class has moved latitude, longitude, and heading fields into a field of type `BusCoordinates`.
- Implementation classes are now internal and may be excluded from source/javadoc artifacts.

## [2.0.2] - 2025-10-17

### Removed

- `ch.qos.logback:logback-classic:1.5.19`
- `ch.qos.logback:logback-core:1.5.19`

## [2.0.1] - 2025-10-17

### Added

- Dependency: logback-core 1.5.19
  - Temporary fix for vulnerability in logback-classic (CVE-2025-11226)

### Changed

- Bumped org.apache.maven.plugins:maven-javadoc-plugin from 3.8.0 to 3.12.0
- Bumped org.apache.maven.plugins:maven-gpg-plugin from 3.2.4 to 3.2.8

## 2.0.0 - 2025-10-17

### Added

- Dependency: org.jetbrains.annotations 26.0.2-1
- `Train` class and `getTrain` method in `TrainClient` class.
- `@ApiStatus.Internal` and `@JsonIgnoreProperties(ignoreUnknown = true)` to relevant internal classes.

### Changed

- Improve `HttpUtils` response handling.
- Improve handling for erroneous responses from the CTA API.
- Update `exports` statements to `opens` statements in `module-info.java` for Jackson reflection compatibility.

### Removed

- `TrainLocation` class and `getTrainLocation` method in `TrainClient` class.
- Internal classes from Maven Javadoc and source JARs.

## 1.3.0 - 2025-10-12

### Added

- Additional validation and logging during deserialization of CTA API responses.
- `exports` statements in `module-info.java` for Jackson reflection compatibility.
- `logback.xml` configuration file for logging.

### Changed

- Maven compiler target and source to 21 from 25 for increase compatibility.
- Various primitive type fields to wrapper classes or `String` to handle null values from the CTA API.

## 1.2.0 - 2025-10-12

### Added

- JavaDoc comments for all public classes and methods.

## 1.1.0 - 2025-10-12

### Added

- JavaDoc comments for some public classes and methods.
- `Bus` class and `getBus` method in `BusClient` class.

### Changed

- Improve serialization of responses from the CTA API.

### Removed

- `Vehicle` class and `getVehicle` method in `BusClient` class.
- `parseString` method from `BusPredictionType` enum.
- `fromExternal` method from `Detour` class.
- `fromExternal` method from bus `Route` class.
- `fromExternal` method from `Stop` class.
- `fromExternal` method from `StopArrival` class.
- `parseString` method from train `Route` enum.
- `fromExternal` method from `StationArrival` enum.
- `fromExternal` method from `TrainCoordinates` class.
- `fromExternal` method from `UpcomingTrainArrival` enum.
- `com.cta4j.util` export from `module-info.java`.

## 1.0.0 - 2025-10-10

### Added

- Initial release of SDK
- `TrainClient` class with methods to interact with CTA Train API.
- `BusClient` class with methods to interact with CTA Bus API.

[Unreleased]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v6.0.0...HEAD
[6.0.0]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v5.0.0...v6.0.0
[5.0.0]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v4.1.0...v5.0.0
[4.1.0]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v4.0.3...v4.1.0
[4.0.3]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v4.0.2...v4.0.3
[4.0.2]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v4.0.1...v4.0.2
[4.0.1]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v4.0.0...v4.0.1
[4.0.0]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v3.0.4...v4.0.0
[3.0.4]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v3.0.3...v3.0.4
[3.0.3]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v3.0.2...v3.0.3
[3.0.2]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v3.0.1...v3.0.2
[3.0.1]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v3.0.0...v3.0.1
[3.0.0]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v2.0.2...v3.0.0
[2.0.2]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v2.0.1...v2.0.2
[2.0.1]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v2.0.0...v2.0.1
