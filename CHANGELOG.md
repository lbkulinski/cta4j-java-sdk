# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## 2.0.1 - 2025-10-17

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

[Unreleased]: https://github.com/lbkulinski/cta4j-java-sdk/compare/v2.0.0...HEAD
