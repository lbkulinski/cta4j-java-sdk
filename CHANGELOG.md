# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

- Separated GitHub Actions workflows for build and release.

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

[Unreleased]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v3.0.0...HEAD
[3.0.0]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v2.0.2...v3.0.0
[2.0.2]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v2.0.1...v2.0.2
[2.0.1]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v2.0.0...v2.0.1