# CTA4J Java SDK — Test Suite Design

**Date:** 2026-06-27
**Scope:** Add a comprehensive test suite to the cta4j-java-sdk. No existing tests exist.

---

## 1. Approach

**Approach 3 (selected):** WireMock integration tests for all `*ApiImpl` classes + unit tests for MapStruct mappers and wire record construction.

- WireMock tests exercise the full pipeline: URL construction → real HTTP → JSON parsing → error-handling logic → domain mapping.
- Mapper unit tests verify field-by-field mapping between wire records and domain objects without any HTTP involvement.
- Avoids `mockStatic` on `HttpClient.get()`, which is fragile and fights the design.

---

## 2. Dependencies (test scope)

Add to `pom.xml` under `<dependencies>`:

| Artifact                              | Purpose                                             |
|---------------------------------------|-----------------------------------------------------|
| `org.junit.jupiter:junit-jupiter:5.x` | JUnit 5 test framework                              |
| `org.wiremock:wiremock:3.x`           | Embedded HTTP server for `*ApiImpl` tests           |
| `org.assertj:assertj-core:3.x`        | Fluent assertions                                   |
| `ch.qos.logback:logback-classic:1.x`  | Logging backend (suppress SLF4J no-binding warning) |

Also add `maven-surefire-plugin` 3.x to `<build><plugins>` to enable JUnit 5 test discovery.

---

## 3. Production code change required

All `*ApiImpl` classes build request URLs with `ApiUtils.SCHEME = "https"` hardcoded. WireMock runs on plain HTTP. Without a change, every impl test would attempt an HTTPS connection to an HTTP server and fail at the connection level before any assertion runs.

**Fix:** Add a `scheme` field to `BusApiConfig` and `TrainApiConfig`.

```java
// BusApiConfig (before)
public record BusApiConfig(String host, String apiKey) { ... }

// BusApiConfig (after)
public record BusApiConfig(String scheme, String host, String apiKey) { ... }
```

The same change applies to `TrainApiConfig`. `BusApiImpl` and `TrainApiImpl` hardcode `ApiUtils.SCHEME` when constructing their config internally — their constructors do not need new parameters. Tests bypass the impl builders entirely and construct `BusApiConfig`/`TrainApiConfig` directly with `"http"`. Each `*ApiImpl` reads `this.config.scheme()` instead of `ApiUtils.SCHEME` directly.

Tests construct configs with `"http"` and a `localhost:port` host pointing at WireMock.

---

## 4. Test file layout

```
src/test/java/com/cta4j/
  bus/
    route/      RoutesApiImplTest, RouteMapperTest
    vehicle/    VehiclesApiImplTest, VehicleMapperTest
    direction/  DirectionsApiImplTest
    stop/       StopsApiImplTest, StopMapperTest
    pattern/    PatternsApiImplTest, RoutePatternMapperTest
    prediction/ PredictionsApiImplTest, PredictionMapperTest
    locale/     LocalesApiImplTest, SupportedLocaleMapperTest
    detour/     DetoursApiImplTest, DetourMapperTest
  train/
    arrival/    ArrivalsApiImplTest, ArrivalMapperTest
    station/    StationsApiImplTest, StationMapperTest
    follow/     FollowApiImplTest, FollowTrainMapperTest
    location/   LocationsApiImplTest, TrainLocationsMapperTest

src/test/resources/
  bus/
    route/      success.json, empty.json, error.json
    vehicle/    success.json, empty.json, not_found.json, error.json
    direction/  success.json, empty.json, not_found.json, error.json
    stop/       success.json, empty.json, not_found.json, error.json
    pattern/    success.json, empty.json, not_found.json, error.json
    prediction/ success.json, empty.json, not_found.json, error.json
    locale/     success.json, empty.json, error.json
    detour/     success.json, empty.json, error.json
  train/
    arrival/    success.json, empty.json, error.json
    station/    success.json, empty.json
    follow/     success.json, not_found.json, error.json
    location/   success.json, empty.json, error.json
```

---

## 5. WireMock test pattern (bus `*ApiImpl`)

Every bus impl test class follows this structure:

```java
class RoutesApiImplTest {
    private WireMockServer server;
    private RoutesApiImpl api;

    @BeforeEach
    void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost:" + server.port(), "testkey");
        api = new RoutesApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    // test methods...
}
```

Train impl tests follow the same shape using `TrainApiConfig`.

WireMock stubs match by path only (`urlPathEqualTo(...)`) for all tests except URL-parameter tests, which additionally use `withQueryParam(...)` to verify correct query string construction.

---

## 6. Coverage cases per `*ApiImpl`

### Bus impls (all 8 follow the same contract)

| Test               | WireMock stub                                                                     | Expected result                                              |
|--------------------|-----------------------------------------------------------------------------------|--------------------------------------------------------------|
| `*_success`        | JSON with data list populated                                                     | Returns mapped domain objects; assert count and field values |
| `*_empty_noErrors` | JSON with both lists null/absent                                                  | Returns `List.of()`                                          |
| `*_notFound`       | JSON with errors where all have resource-typed fields (e.g. `vid`, `rt`, `stpid`) | Returns `List.of()`                                          |
| `*_fatalError`     | JSON with errors where none have resource-typed fields                            | Throws `Cta4jException`                                      |
| `*_invalidJson`    | Raw string `"not-json"`                                                           | Throws `Cta4jException`                                      |

For methods with multiple overloads (`VehiclesApiImpl.findByIds` / `findByRouteIds`, `PredictionsApiImpl` stop vs vehicle):
- Additional test per overload verifying correct query param sent (e.g. `vid=` vs `rt=`).
- Empty input collection → returns `List.of()` immediately without hitting WireMock.

### Train impls (diverge in error model)

**`ArrivalsApiImpl` / `LocationsApiImpl`** — error signaled by `errCd != 0`:

| Test            | Stub / input                             | Expected result               |
|-----------------|------------------------------------------|-------------------------------|
| `*_success`     | `errCd: 0`, eta/route list populated     | Returns mapped domain objects |
| `*_empty`       | `errCd: 0`, eta/route list null or empty | Returns `List.of()`           |
| `*_error`       | `errCd: 1`, `errNm: "..."`               | Throws `Cta4jException`       |
| `*_invalidJson` | Raw `"not-json"`                         | Throws `Cta4jException`       |

**`FollowApiImpl`** — `errCd=501` is not-found:

| Test                    | Stub                                 | Expected result            |
|-------------------------|--------------------------------------|----------------------------|
| `findByRun_found`       | `errCd: 0`, position + eta populated | Returns `Optional.of(...)` |
| `findByRun_notFound`    | `errCd: 501`                         | Returns `Optional.empty()` |
| `findByRun_error`       | `errCd: 1`                           | Throws `Cta4jException`    |
| `findByRun_invalidJson` | Raw `"not-json"`                     | Throws `Cta4jException`    |

**`StationsApiImpl`** — no error envelope, pure JSON array:

| Test               | Stub                            | Expected result               |
|--------------------|---------------------------------|-------------------------------|
| `list_success`     | JSON array with station objects | Returns mapped domain objects |
| `list_empty`       | `[]`                            | Returns `List.of()`           |
| `list_invalidJson` | Raw `"not-json"`                | Throws `Cta4jException`       |

---

## 7. Mapper unit tests

One test class per mapper. No WireMock needed — call `MAPPER.INSTANCE.toDomain(wire)` directly.

**Two tests per mapper:**

1. **All fields populated** — construct a wire record with every field set to a non-null value; call `toDomain`; assert every domain field matches the expected mapped value.
2. **Nullable fields null** — construct a wire record with all `@Nullable` fields as `null`; call `toDomain`; assert domain `@Nullable` fields are `null` and required fields still map correctly.

---

## 8. JSON fixture conventions

- Field names match the Jackson record parameter names exactly (e.g. `"route"` not `"routes"`, as the wire record field is `route`).
- Bus timestamps use `"yyyyMMdd HH:mm:ss"` format (pattern `"yyyyMMdd HH:mm[:ss]"` — bus `Qualifiers.mapTimestamp` accepts both minute and second resolution).
- Train timestamps use `"yyyy-MM-dd'T'HH:mm:ss"` format (train `Qualifiers.mapTimestamp` uses `DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")`).
- Each fixture is minimal — only the fields needed for the scenario, relying on `@JsonIgnoreProperties(ignoreUnknown = true)` to silently ignore extras.
- Error fixtures include both resource-specific fields (for not-found) and generic-only `msg` (for fatal error) as separate files.

---

## 9. Summary of files to create/modify

**Modified:**
- `pom.xml` — add test dependencies + surefire plugin
- `BusApiConfig.java` — add `scheme` field
- `TrainApiConfig.java` — add `scheme` field
- `BusApiImpl.java` — pass `ApiUtils.SCHEME` when constructing `BusApiConfig`
- `TrainApiImpl.java` — pass `ApiUtils.SCHEME` when constructing `TrainApiConfig`
- All 12 `*ApiImpl` classes — use `this.config.scheme()` instead of `ApiUtils.SCHEME`

**Created:**
- 23 test classes under `src/test/java/`
- ~40 JSON fixture files under `src/test/resources/`
