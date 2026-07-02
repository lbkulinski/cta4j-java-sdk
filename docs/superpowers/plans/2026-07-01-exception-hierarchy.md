# Structured Exception Hierarchy Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the flat `Cta4jException` with a sealed hierarchy (`Cta4jException` → `Cta4jBusException`/`Cta4jTrainException` → per-endpoint train exceptions) carrying structured `endpoint()`/`errorCode()`/`rawErrorCode()` accessors, backed by real per-endpoint error-code enums verified against `train-api.pdf`.

**Architecture:** `Cta4jException` (sealed) gains an `endpoint()` field. `Cta4jBusException` (final) adds nothing else — bus has no numeric codes. `Cta4jTrainException` (sealed, itself instantiable) gains three final leaf subclasses (`Cta4jArrivalsException`/`Cta4jFollowException`/`Cta4jLocationsException`), each with an `errorCode()` enum accessor and a `rawErrorCode()` int fallback for codes the enum doesn't recognize. `getMessage()` returns CTA's raw text verbatim — no synthesized "Error response from X: ..." template.

**Tech Stack:** Java 21 (sealed classes, switch expressions), JUnit 5, AssertJ, WireMock, Maven.

## Global Constraints

- Spec: `docs/superpowers/specs/2026-07-01-exception-hierarchy-design.md`
- `Cta4jException` and all subclasses are public API (no `@ApiStatus.Internal`) — annotation ordering per CLAUDE.md is Jackson/framework → `@ApiStatus.Internal` → `@NullMarked`.
- Guard all public method parameters with `Objects.requireNonNull`; `final` on all classes not designed for extension; `this.` on all instance field/method references; no comments unless the why is non-obvious.
- Every step must leave `mvn test -Djacoco.skip=true` passing before moving to the next task, unless the step explicitly says a compile failure is expected (TDD red step).

---

### Task 1: `ArrivalsErrorCode` enum

**Files:**
- Create: `src/main/java/com/cta4j/train/arrival/model/ArrivalsErrorCode.java`
- Test: `src/test/java/com/cta4j/train/arrival/model/ArrivalsErrorCodeTest.java`

**Interfaces:**
- Produces: `ArrivalsErrorCode` enum with `getCode(): int` and static `fromCode(int): ArrivalsErrorCode` (never throws, defaults to `UNKNOWN`). Consumed by Task 7 (`Cta4jArrivalsException`) and Task 15 (`ArrivalsApiImpl`).

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.train.arrival.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArrivalsErrorCodeTest {
    @Test
    void getCode_returnsMappedCode_forEachConstant() {
        assertThat(ArrivalsErrorCode.MISSING_PARAMETER.getCode()).isEqualTo(100);
        assertThat(ArrivalsErrorCode.INVALID_API_KEY.getCode()).isEqualTo(101);
        assertThat(ArrivalsErrorCode.DAILY_LIMIT_EXCEEDED.getCode()).isEqualTo(102);
        assertThat(ArrivalsErrorCode.INVALID_MAPID.getCode()).isEqualTo(103);
        assertThat(ArrivalsErrorCode.MAPID_NOT_INTEGER.getCode()).isEqualTo(104);
        assertThat(ArrivalsErrorCode.TOO_MANY_MAPIDS.getCode()).isEqualTo(105);
        assertThat(ArrivalsErrorCode.INVALID_ROUTE.getCode()).isEqualTo(106);
        assertThat(ArrivalsErrorCode.TOO_MANY_ROUTES.getCode()).isEqualTo(107);
        assertThat(ArrivalsErrorCode.INVALID_STPID.getCode()).isEqualTo(108);
        assertThat(ArrivalsErrorCode.TOO_MANY_STPIDS.getCode()).isEqualTo(109);
        assertThat(ArrivalsErrorCode.INVALID_MAX.getCode()).isEqualTo(110);
        assertThat(ArrivalsErrorCode.MAX_NOT_POSITIVE.getCode()).isEqualTo(111);
        assertThat(ArrivalsErrorCode.STPID_NOT_INTEGER.getCode()).isEqualTo(112);
        assertThat(ArrivalsErrorCode.INVALID_PARAMETER.getCode()).isEqualTo(500);
        assertThat(ArrivalsErrorCode.SERVER_ERROR.getCode()).isEqualTo(900);
        assertThat(ArrivalsErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }

    @Test
    void fromCode_returnsMatchingConstant_forEachDocumentedCode() {
        assertThat(ArrivalsErrorCode.fromCode(100)).isEqualTo(ArrivalsErrorCode.MISSING_PARAMETER);
        assertThat(ArrivalsErrorCode.fromCode(101)).isEqualTo(ArrivalsErrorCode.INVALID_API_KEY);
        assertThat(ArrivalsErrorCode.fromCode(102)).isEqualTo(ArrivalsErrorCode.DAILY_LIMIT_EXCEEDED);
        assertThat(ArrivalsErrorCode.fromCode(103)).isEqualTo(ArrivalsErrorCode.INVALID_MAPID);
        assertThat(ArrivalsErrorCode.fromCode(104)).isEqualTo(ArrivalsErrorCode.MAPID_NOT_INTEGER);
        assertThat(ArrivalsErrorCode.fromCode(105)).isEqualTo(ArrivalsErrorCode.TOO_MANY_MAPIDS);
        assertThat(ArrivalsErrorCode.fromCode(106)).isEqualTo(ArrivalsErrorCode.INVALID_ROUTE);
        assertThat(ArrivalsErrorCode.fromCode(107)).isEqualTo(ArrivalsErrorCode.TOO_MANY_ROUTES);
        assertThat(ArrivalsErrorCode.fromCode(108)).isEqualTo(ArrivalsErrorCode.INVALID_STPID);
        assertThat(ArrivalsErrorCode.fromCode(109)).isEqualTo(ArrivalsErrorCode.TOO_MANY_STPIDS);
        assertThat(ArrivalsErrorCode.fromCode(110)).isEqualTo(ArrivalsErrorCode.INVALID_MAX);
        assertThat(ArrivalsErrorCode.fromCode(111)).isEqualTo(ArrivalsErrorCode.MAX_NOT_POSITIVE);
        assertThat(ArrivalsErrorCode.fromCode(112)).isEqualTo(ArrivalsErrorCode.STPID_NOT_INTEGER);
        assertThat(ArrivalsErrorCode.fromCode(500)).isEqualTo(ArrivalsErrorCode.INVALID_PARAMETER);
        assertThat(ArrivalsErrorCode.fromCode(900)).isEqualTo(ArrivalsErrorCode.SERVER_ERROR);
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsNotRecognized() {
        assertThat(ArrivalsErrorCode.fromCode(999)).isEqualTo(ArrivalsErrorCode.UNKNOWN);
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=ArrivalsErrorCodeTest test -Djacoco.skip=true`
Expected: FAIL to compile — `ArrivalsErrorCode` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.train.arrival.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents an error code returned by the CTA Train Tracker Arrivals API.
 */
@NullMarked
public enum ArrivalsErrorCode {
    MISSING_PARAMETER(100),
    INVALID_API_KEY(101),
    DAILY_LIMIT_EXCEEDED(102),
    INVALID_MAPID(103),
    MAPID_NOT_INTEGER(104),
    TOO_MANY_MAPIDS(105),
    INVALID_ROUTE(106),
    TOO_MANY_ROUTES(107),
    INVALID_STPID(108),
    TOO_MANY_STPIDS(109),
    INVALID_MAX(110),
    MAX_NOT_POSITIVE(111),
    STPID_NOT_INTEGER(112),
    INVALID_PARAMETER(500),
    SERVER_ERROR(900),
    UNKNOWN(-1);

    private final int code;

    ArrivalsErrorCode(int code) {
        this.code = code;
    }

    /**
     * Gets the CTA error code associated with this constant.
     *
     * @return the CTA error code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns the {@code ArrivalsErrorCode} corresponding to the given code.
     *
     * @param code the CTA error code
     * @return the corresponding {@code ArrivalsErrorCode}, or {@link #UNKNOWN} if the code is not recognized
     */
    public static ArrivalsErrorCode fromCode(int code) {
        return switch (code) {
            case 100 -> MISSING_PARAMETER;
            case 101 -> INVALID_API_KEY;
            case 102 -> DAILY_LIMIT_EXCEEDED;
            case 103 -> INVALID_MAPID;
            case 104 -> MAPID_NOT_INTEGER;
            case 105 -> TOO_MANY_MAPIDS;
            case 106 -> INVALID_ROUTE;
            case 107 -> TOO_MANY_ROUTES;
            case 108 -> INVALID_STPID;
            case 109 -> TOO_MANY_STPIDS;
            case 110 -> INVALID_MAX;
            case 111 -> MAX_NOT_POSITIVE;
            case 112 -> STPID_NOT_INTEGER;
            case 500 -> INVALID_PARAMETER;
            case 900 -> SERVER_ERROR;
            default -> UNKNOWN;
        };
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=ArrivalsErrorCodeTest test -Djacoco.skip=true`
Expected: `Tests run: 3, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/arrival/model/ArrivalsErrorCode.java src/test/java/com/cta4j/train/arrival/model/ArrivalsErrorCodeTest.java
git commit -m "feat: add ArrivalsErrorCode enum"
```

---

### Task 2: `FollowErrorCode` enum

**Files:**
- Create: `src/main/java/com/cta4j/train/follow/model/FollowErrorCode.java`
- Test: `src/test/java/com/cta4j/train/follow/model/FollowErrorCodeTest.java`

**Interfaces:**
- Produces: `FollowErrorCode` enum, same shape as Task 1. Consumed by Task 8 (`Cta4jFollowException`) and Task 15 (`FollowApiImpl`).

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.train.follow.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FollowErrorCodeTest {
    @Test
    void getCode_returnsMappedCode_forEachConstant() {
        assertThat(FollowErrorCode.MISSING_PARAMETER.getCode()).isEqualTo(100);
        assertThat(FollowErrorCode.INVALID_API_KEY.getCode()).isEqualTo(101);
        assertThat(FollowErrorCode.DAILY_LIMIT_EXCEEDED.getCode()).isEqualTo(102);
        assertThat(FollowErrorCode.INVALID_PARAMETER.getCode()).isEqualTo(500);
        assertThat(FollowErrorCode.RUN_NOT_FOUND.getCode()).isEqualTo(501);
        assertThat(FollowErrorCode.UNABLE_TO_DETERMINE_STOPS.getCode()).isEqualTo(502);
        assertThat(FollowErrorCode.UNABLE_TO_FIND_PREDICTIONS.getCode()).isEqualTo(503);
        assertThat(FollowErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }

    @Test
    void fromCode_returnsMatchingConstant_forEachDocumentedCode() {
        assertThat(FollowErrorCode.fromCode(100)).isEqualTo(FollowErrorCode.MISSING_PARAMETER);
        assertThat(FollowErrorCode.fromCode(101)).isEqualTo(FollowErrorCode.INVALID_API_KEY);
        assertThat(FollowErrorCode.fromCode(102)).isEqualTo(FollowErrorCode.DAILY_LIMIT_EXCEEDED);
        assertThat(FollowErrorCode.fromCode(500)).isEqualTo(FollowErrorCode.INVALID_PARAMETER);
        assertThat(FollowErrorCode.fromCode(501)).isEqualTo(FollowErrorCode.RUN_NOT_FOUND);
        assertThat(FollowErrorCode.fromCode(502)).isEqualTo(FollowErrorCode.UNABLE_TO_DETERMINE_STOPS);
        assertThat(FollowErrorCode.fromCode(503)).isEqualTo(FollowErrorCode.UNABLE_TO_FIND_PREDICTIONS);
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsNotRecognized() {
        assertThat(FollowErrorCode.fromCode(999)).isEqualTo(FollowErrorCode.UNKNOWN);
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=FollowErrorCodeTest test -Djacoco.skip=true`
Expected: FAIL to compile — `FollowErrorCode` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.train.follow.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents an error code returned by the CTA Train Tracker Follow API.
 */
@NullMarked
public enum FollowErrorCode {
    MISSING_PARAMETER(100),
    INVALID_API_KEY(101),
    DAILY_LIMIT_EXCEEDED(102),
    INVALID_PARAMETER(500),
    RUN_NOT_FOUND(501),
    UNABLE_TO_DETERMINE_STOPS(502),
    UNABLE_TO_FIND_PREDICTIONS(503),
    UNKNOWN(-1);

    private final int code;

    FollowErrorCode(int code) {
        this.code = code;
    }

    /**
     * Gets the CTA error code associated with this constant.
     *
     * @return the CTA error code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns the {@code FollowErrorCode} corresponding to the given code.
     *
     * @param code the CTA error code
     * @return the corresponding {@code FollowErrorCode}, or {@link #UNKNOWN} if the code is not recognized
     */
    public static FollowErrorCode fromCode(int code) {
        return switch (code) {
            case 100 -> MISSING_PARAMETER;
            case 101 -> INVALID_API_KEY;
            case 102 -> DAILY_LIMIT_EXCEEDED;
            case 500 -> INVALID_PARAMETER;
            case 501 -> RUN_NOT_FOUND;
            case 502 -> UNABLE_TO_DETERMINE_STOPS;
            case 503 -> UNABLE_TO_FIND_PREDICTIONS;
            default -> UNKNOWN;
        };
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=FollowErrorCodeTest test -Djacoco.skip=true`
Expected: `Tests run: 3, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/follow/model/FollowErrorCode.java src/test/java/com/cta4j/train/follow/model/FollowErrorCodeTest.java
git commit -m "feat: add FollowErrorCode enum"
```

---

### Task 3: `LocationsErrorCode` enum

**Files:**
- Create: `src/main/java/com/cta4j/train/location/model/LocationsErrorCode.java`
- Test: `src/test/java/com/cta4j/train/location/model/LocationsErrorCodeTest.java`

**Interfaces:**
- Produces: `LocationsErrorCode` enum, same shape as Task 1. Consumed by Task 9 (`Cta4jLocationsException`) and Task 15 (`LocationsApiImpl`).

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.train.location.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LocationsErrorCodeTest {
    @Test
    void getCode_returnsMappedCode_forEachConstant() {
        assertThat(LocationsErrorCode.MISSING_PARAMETER.getCode()).isEqualTo(100);
        assertThat(LocationsErrorCode.INVALID_API_KEY.getCode()).isEqualTo(101);
        assertThat(LocationsErrorCode.DAILY_LIMIT_EXCEEDED.getCode()).isEqualTo(102);
        assertThat(LocationsErrorCode.INVALID_ROUTE.getCode()).isEqualTo(106);
        assertThat(LocationsErrorCode.TOO_MANY_ROUTES.getCode()).isEqualTo(107);
        assertThat(LocationsErrorCode.INVALID_PARAMETER.getCode()).isEqualTo(500);
        assertThat(LocationsErrorCode.UNKNOWN.getCode()).isEqualTo(-1);
    }

    @Test
    void fromCode_returnsMatchingConstant_forEachDocumentedCode() {
        assertThat(LocationsErrorCode.fromCode(100)).isEqualTo(LocationsErrorCode.MISSING_PARAMETER);
        assertThat(LocationsErrorCode.fromCode(101)).isEqualTo(LocationsErrorCode.INVALID_API_KEY);
        assertThat(LocationsErrorCode.fromCode(102)).isEqualTo(LocationsErrorCode.DAILY_LIMIT_EXCEEDED);
        assertThat(LocationsErrorCode.fromCode(106)).isEqualTo(LocationsErrorCode.INVALID_ROUTE);
        assertThat(LocationsErrorCode.fromCode(107)).isEqualTo(LocationsErrorCode.TOO_MANY_ROUTES);
        assertThat(LocationsErrorCode.fromCode(500)).isEqualTo(LocationsErrorCode.INVALID_PARAMETER);
    }

    @Test
    void fromCode_returnsUnknown_whenCodeIsNotRecognized() {
        assertThat(LocationsErrorCode.fromCode(999)).isEqualTo(LocationsErrorCode.UNKNOWN);
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=LocationsErrorCodeTest test -Djacoco.skip=true`
Expected: FAIL to compile — `LocationsErrorCode` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.train.location.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents an error code returned by the CTA Train Tracker Locations API.
 */
@NullMarked
public enum LocationsErrorCode {
    MISSING_PARAMETER(100),
    INVALID_API_KEY(101),
    DAILY_LIMIT_EXCEEDED(102),
    INVALID_ROUTE(106),
    TOO_MANY_ROUTES(107),
    INVALID_PARAMETER(500),
    UNKNOWN(-1);

    private final int code;

    LocationsErrorCode(int code) {
        this.code = code;
    }

    /**
     * Gets the CTA error code associated with this constant.
     *
     * @return the CTA error code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns the {@code LocationsErrorCode} corresponding to the given code.
     *
     * @param code the CTA error code
     * @return the corresponding {@code LocationsErrorCode}, or {@link #UNKNOWN} if the code is not recognized
     */
    public static LocationsErrorCode fromCode(int code) {
        return switch (code) {
            case 100 -> MISSING_PARAMETER;
            case 101 -> INVALID_API_KEY;
            case 102 -> DAILY_LIMIT_EXCEEDED;
            case 106 -> INVALID_ROUTE;
            case 107 -> TOO_MANY_ROUTES;
            case 500 -> INVALID_PARAMETER;
            default -> UNKNOWN;
        };
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=LocationsErrorCodeTest test -Djacoco.skip=true`
Expected: `Tests run: 3, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/location/model/LocationsErrorCode.java src/test/java/com/cta4j/train/location/model/LocationsErrorCodeTest.java
git commit -m "feat: add LocationsErrorCode enum"
```

---

### Task 4: Widen `Cta4jException` (non-final, add `endpoint()`)

**Files:**
- Modify: `src/main/java/com/cta4j/exception/Cta4jException.java`
- Test: Create `src/test/java/com/cta4j/exception/Cta4jExceptionTest.java`

**Interfaces:**
- Produces: `Cta4jException(String message, String endpoint)`, `Cta4jException(String message, String endpoint, Throwable cause)`, `endpoint(): String`. The old `(String message)`/`(String message, Throwable cause)` constructors are removed — every call site in the codebase is retargeted in later tasks. Not yet `sealed` (added in Task 10, once `Cta4jBusException`/`Cta4jTrainException` exist).
- Consumed by: every task below.

This task alone will break the build (every existing call site uses the old constructors) until Tasks 5-19 retarget them. That's expected — Tasks 5-19 must all land before `mvn compile` succeeds again project-wide. Do not skip ahead past this point without completing through Task 19.

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.exception;

import com.cta4j.common.exception.Cta4jException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jExceptionTest {
    @Test
    void constructor_setsMessageAndEndpoint() {
        Cta4jException exception = new Cta4jException("something went wrong", "/some/endpoint");

        assertThat(exception.getMessage()).isEqualTo("something went wrong");
        assertThat(exception.endpoint()).isEqualTo("/some/endpoint");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndCause() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jException exception = new Cta4jException("something went wrong", "/some/endpoint", cause);

        assertThat(exception.getMessage()).isEqualTo("something went wrong");
        assertThat(exception.endpoint()).isEqualTo("/some/endpoint");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void constructor_throwsNullPointerException_whenEndpointIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new Cta4jException("message", null));
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=Cta4jExceptionTest test -Djacoco.skip=true`
Expected: FAIL to compile — no `(String, String)` constructor exists yet.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.exception;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A custom exception class for handling cta4j-specific errors.
 */
@NullMarked
public class Cta4jException extends RuntimeException {
    private final String endpoint;

    /**
     * Constructs a {@code Cta4jException}.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jException(String message, String endpoint) {
        super(message);

        this.endpoint = Objects.requireNonNull(endpoint);
    }

    /**
     * Constructs a {@code Cta4jException}.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param cause the cause of the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jException(String message, String endpoint, Throwable cause) {
        super(message, cause);

        this.endpoint = Objects.requireNonNull(endpoint);
    }

    /**
     * Gets the API endpoint associated with this exception.
     *
     * @return the API endpoint
     */
    public String endpoint() {
        return this.endpoint;
    }
}
```

Note: not `final` yet (was `final` before this task) and not yet `sealed` — Task 10 adds `sealed permits Cta4jBusException, Cta4jTrainException` once both exist.

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=Cta4jExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 3, Failures: 0, Errors: 0` for `Cta4jExceptionTest` (the rest of the project will not compile yet — that's expected until Task 19).

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/exception/Cta4jException.java src/test/java/com/cta4j/exception/Cta4jExceptionTest.java
git commit -m "feat: add endpoint field to Cta4jException, remove final"
```

---

### Task 5: `Cta4jBusException`

**Files:**
- Create: `src/main/java/com/cta4j/bus/exception/Cta4jBusException.java`
- Test: `src/test/java/com/cta4j/bus/exception/Cta4jBusExceptionTest.java`

**Interfaces:**
- Consumes: `Cta4jException(String, String)`, `Cta4jException(String, String, Throwable)`, `endpoint()` (Task 4); `com.cta4j.bus.common.internal.wire.CtaError.msg(): String` (existing).
- Produces: `Cta4jBusException(List<? extends CtaError> errors, String endpoint)`, `Cta4jBusException(String message, String endpoint, Throwable cause)`, `Cta4jBusException(String message, String endpoint)`. Consumed by Tasks 11-13.

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.bus.exception;

import com.cta4j.bus.common.internal.wire.CtaError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Cta4jBusExceptionTest {
    @Test
    void constructor_joinsSingleErrorMessage() {
        CtaError error = () -> "No data found for route";

        Cta4jBusException exception = new Cta4jBusException(List.of(error), "/bustime/api/v3/getroutes");

        assertThat(exception.getMessage()).isEqualTo("No data found for route");
        assertThat(exception.endpoint()).isEqualTo("/bustime/api/v3/getroutes");
    }

    @Test
    void constructor_joinsMultipleErrorMessages() {
        CtaError first = () -> "Invalid route";
        CtaError second = () -> "Unknown parameter";

        Cta4jBusException exception = new Cta4jBusException(List.of(first, second), "/bustime/api/v3/getroutes");

        assertThat(exception.getMessage()).isEqualTo("Invalid route; Unknown parameter");
    }

    @Test
    void constructor_setsMessageEndpointAndCause() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jBusException exception = new Cta4jBusException("parse failed", "/bustime/api/v3/getroutes", cause);

        assertThat(exception.getMessage()).isEqualTo("parse failed");
        assertThat(exception.endpoint()).isEqualTo("/bustime/api/v3/getroutes");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void constructor_setsMessageAndEndpoint_withNoErrorsOrCause() {
        Cta4jBusException exception = new Cta4jBusException("Multiple stops found for ID: 123", "/bustime/api/v3/getstops");

        assertThat(exception.getMessage()).isEqualTo("Multiple stops found for ID: 123");
        assertThat(exception.endpoint()).isEqualTo("/bustime/api/v3/getstops");
        assertThat(exception.getCause()).isNull();
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=Cta4jBusExceptionTest test -Djacoco.skip=true`
Expected: FAIL to compile — `Cta4jBusException` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.bus.exception;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.cta4j.common.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

/**
 * A {@link Cta4jException} thrown by CTA Bus Tracker API calls.
 */
@NullMarked
public final class Cta4jBusException extends Cta4jException {
    /**
     * Constructs a {@code Cta4jBusException} from one or more CTA-reported errors.
     *
     * @param errors the {@link List} of {@link CtaError}s reported by the API
     * @param endpoint the API endpoint associated with this exception
     * @throws NullPointerException if {@code errors} or {@code endpoint} is {@code null}
     */
    public Cta4jBusException(List<? extends CtaError> errors, String endpoint) {
        super(joinMessages(errors), endpoint);
    }

    /**
     * Constructs a {@code Cta4jBusException} for a parse or transport failure.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param cause the cause of the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jBusException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);
    }

    /**
     * Constructs a {@code Cta4jBusException} for an SDK-detected condition with no CTA-reported error or cause.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jBusException(String message, String endpoint) {
        super(message, endpoint);
    }

    private static String joinMessages(List<? extends CtaError> errors) {
        Objects.requireNonNull(errors);

        return errors.stream()
                     .map(CtaError::msg)
                     .reduce("%s; %s"::formatted)
                     .orElse("Unknown error");
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=Cta4jBusExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 4, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/bus/exception/Cta4jBusException.java src/test/java/com/cta4j/bus/exception/Cta4jBusExceptionTest.java
git commit -m "feat: add Cta4jBusException"
```

---

### Task 6: `Cta4jTrainException` (not yet sealed)

**Files:**
- Create: `src/main/java/com/cta4j/train/exception/Cta4jTrainException.java`
- Test: `src/test/java/com/cta4j/train/exception/Cta4jTrainExceptionTest.java`

**Interfaces:**
- Consumes: `Cta4jException(String, String)`, `Cta4jException(String, String, Throwable)` (Task 4).
- Produces: `Cta4jTrainException(String message, String endpoint)`, `Cta4jTrainException(String message, String endpoint, Throwable cause)`. Directly instantiated by Task 15 (`StationsApiImpl`) and Task 17 (train `Qualifiers`). Extended by Tasks 7-9.

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.train.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jTrainExceptionTest {
    @Test
    void constructor_setsMessageAndEndpoint() {
        Cta4jTrainException exception = new Cta4jTrainException("something went wrong", "/api/1.0/ttarrivals.aspx");

        assertThat(exception.getMessage()).isEqualTo("something went wrong");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttarrivals.aspx");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void constructor_setsMessageEndpointAndCause() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jTrainException exception = new Cta4jTrainException("something went wrong", "/api/1.0/ttarrivals.aspx", cause);

        assertThat(exception.getMessage()).isEqualTo("something went wrong");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttarrivals.aspx");
        assertThat(exception.getCause()).isSameAs(cause);
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=Cta4jTrainExceptionTest test -Djacoco.skip=true`
Expected: FAIL to compile — `Cta4jTrainException` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.train.exception;

import com.cta4j.common.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

/**
 * A {@link Cta4jException} thrown by CTA Train Tracker API calls.
 */
@NullMarked
public class Cta4jTrainException extends Cta4jException {
    /**
     * Constructs a {@code Cta4jTrainException}.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jTrainException(String message, String endpoint) {
        super(message, endpoint);
    }

    /**
     * Constructs a {@code Cta4jTrainException}.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param cause the cause of the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jTrainException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);
    }
}
```

Note: not `sealed` yet — Task 10 adds `sealed permits Cta4jArrivalsException, Cta4jFollowException, Cta4jLocationsException` once all three exist (Tasks 7-9).

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=Cta4jTrainExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 2, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/exception/Cta4jTrainException.java src/test/java/com/cta4j/train/exception/Cta4jTrainExceptionTest.java
git commit -m "feat: add Cta4jTrainException"
```

---

### Task 7: `Cta4jArrivalsException`

**Files:**
- Create: `src/main/java/com/cta4j/train/exception/Cta4jArrivalsException.java`
- Test: `src/test/java/com/cta4j/train/exception/Cta4jArrivalsExceptionTest.java`

**Interfaces:**
- Consumes: `Cta4jTrainException(String, String)`, `Cta4jTrainException(String, String, Throwable)` (Task 6); `ArrivalsErrorCode` (Task 1).
- Produces: `Cta4jArrivalsException(String message, String endpoint, int rawErrorCode, ArrivalsErrorCode errorCode)`, `Cta4jArrivalsException(String message, String endpoint, Throwable cause)`, `rawErrorCode(): Integer`, `errorCode(): ArrivalsErrorCode`. Consumed by Task 15 (`ArrivalsApiImpl`).

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.train.exception;

import com.cta4j.train.arrival.model.ArrivalsErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jArrivalsExceptionTest {
    @Test
    void constructor_setsFields_forGenuineError() {
        Cta4jArrivalsException exception = new Cta4jArrivalsException(
            "Invalid mapid: 99999",
            "/api/1.0/ttarrivals.aspx",
            103,
            ArrivalsErrorCode.INVALID_MAPID
        );

        assertThat(exception.getMessage()).isEqualTo("Invalid mapid: 99999");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttarrivals.aspx");
        assertThat(exception.rawErrorCode()).isEqualTo(103);
        assertThat(exception.errorCode()).isEqualTo(ArrivalsErrorCode.INVALID_MAPID);
    }

    @Test
    void constructor_leavesRawErrorCodeAndErrorCodeNull_forParseFailure() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jArrivalsException exception = new Cta4jArrivalsException(
            "Failed to parse response",
            "/api/1.0/ttarrivals.aspx",
            cause
        );

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttarrivals.aspx");
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.rawErrorCode()).isNull();
        assertThat(exception.errorCode()).isNull();
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=Cta4jArrivalsExceptionTest test -Djacoco.skip=true`
Expected: FAIL to compile — `Cta4jArrivalsException` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.train.exception;

import com.cta4j.train.arrival.model.ArrivalsErrorCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A {@link Cta4jTrainException} thrown by CTA Train Tracker Arrivals API calls.
 */
@NullMarked
public final class Cta4jArrivalsException extends Cta4jTrainException {
    private final @Nullable Integer rawErrorCode;
    private final @Nullable ArrivalsErrorCode errorCode;

    /**
     * Constructs a {@code Cta4jArrivalsException} from a genuine CTA-reported error.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param rawErrorCode the raw CTA error code
     * @param errorCode the {@link ArrivalsErrorCode} corresponding to {@code rawErrorCode}
     * @throws NullPointerException if {@code endpoint} or {@code errorCode} is {@code null}
     */
    public Cta4jArrivalsException(String message, String endpoint, int rawErrorCode, ArrivalsErrorCode errorCode) {
        super(message, endpoint);

        this.rawErrorCode = rawErrorCode;
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    /**
     * Constructs a {@code Cta4jArrivalsException} for a parse or transport failure.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param cause the cause of the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jArrivalsException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);

        this.rawErrorCode = null;
        this.errorCode = null;
    }

    /**
     * Gets the raw CTA error code, or {@code null} if this exception was not caused by a CTA-reported error.
     *
     * @return the raw CTA error code, or {@code null}
     */
    public @Nullable Integer rawErrorCode() {
        return this.rawErrorCode;
    }

    /**
     * Gets the {@link ArrivalsErrorCode}, or {@code null} if this exception was not caused by a CTA-reported error.
     *
     * @return the {@link ArrivalsErrorCode}, or {@code null}
     */
    public @Nullable ArrivalsErrorCode errorCode() {
        return this.errorCode;
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=Cta4jArrivalsExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 2, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/exception/Cta4jArrivalsException.java src/test/java/com/cta4j/train/exception/Cta4jArrivalsExceptionTest.java
git commit -m "feat: add Cta4jArrivalsException"
```

---

### Task 8: `Cta4jFollowException`

**Files:**
- Create: `src/main/java/com/cta4j/train/exception/Cta4jFollowException.java`
- Test: `src/test/java/com/cta4j/train/exception/Cta4jFollowExceptionTest.java`

**Interfaces:**
- Consumes: `Cta4jTrainException(String, String)`, `Cta4jTrainException(String, String, Throwable)` (Task 6); `FollowErrorCode` (Task 2).
- Produces: `Cta4jFollowException(String message, String endpoint, int rawErrorCode, FollowErrorCode errorCode)`, `Cta4jFollowException(String message, String endpoint, Throwable cause)`, `rawErrorCode(): Integer`, `errorCode(): FollowErrorCode`. Consumed by Task 15 (`FollowApiImpl`).

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.train.exception;

import com.cta4j.train.follow.model.FollowErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jFollowExceptionTest {
    @Test
    void constructor_setsFields_forGenuineError() {
        Cta4jFollowException exception = new Cta4jFollowException(
            "No trains with runnumber 123 were found.",
            "/api/1.0/ttfollow.aspx",
            501,
            FollowErrorCode.RUN_NOT_FOUND
        );

        assertThat(exception.getMessage()).isEqualTo("No trains with runnumber 123 were found.");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttfollow.aspx");
        assertThat(exception.rawErrorCode()).isEqualTo(501);
        assertThat(exception.errorCode()).isEqualTo(FollowErrorCode.RUN_NOT_FOUND);
    }

    @Test
    void constructor_leavesRawErrorCodeAndErrorCodeNull_forParseFailure() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jFollowException exception = new Cta4jFollowException(
            "Failed to parse response",
            "/api/1.0/ttfollow.aspx",
            cause
        );

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttfollow.aspx");
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.rawErrorCode()).isNull();
        assertThat(exception.errorCode()).isNull();
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=Cta4jFollowExceptionTest test -Djacoco.skip=true`
Expected: FAIL to compile — `Cta4jFollowException` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.train.exception;

import com.cta4j.train.follow.model.FollowErrorCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A {@link Cta4jTrainException} thrown by CTA Train Tracker Follow API calls.
 */
@NullMarked
public final class Cta4jFollowException extends Cta4jTrainException {
    private final @Nullable Integer rawErrorCode;
    private final @Nullable FollowErrorCode errorCode;

    /**
     * Constructs a {@code Cta4jFollowException} from a genuine CTA-reported error.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param rawErrorCode the raw CTA error code
     * @param errorCode the {@link FollowErrorCode} corresponding to {@code rawErrorCode}
     * @throws NullPointerException if {@code endpoint} or {@code errorCode} is {@code null}
     */
    public Cta4jFollowException(String message, String endpoint, int rawErrorCode, FollowErrorCode errorCode) {
        super(message, endpoint);

        this.rawErrorCode = rawErrorCode;
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    /**
     * Constructs a {@code Cta4jFollowException} for a parse or transport failure.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param cause the cause of the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jFollowException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);

        this.rawErrorCode = null;
        this.errorCode = null;
    }

    /**
     * Gets the raw CTA error code, or {@code null} if this exception was not caused by a CTA-reported error.
     *
     * @return the raw CTA error code, or {@code null}
     */
    public @Nullable Integer rawErrorCode() {
        return this.rawErrorCode;
    }

    /**
     * Gets the {@link FollowErrorCode}, or {@code null} if this exception was not caused by a CTA-reported error.
     *
     * @return the {@link FollowErrorCode}, or {@code null}
     */
    public @Nullable FollowErrorCode errorCode() {
        return this.errorCode;
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=Cta4jFollowExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 2, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/exception/Cta4jFollowException.java src/test/java/com/cta4j/train/exception/Cta4jFollowExceptionTest.java
git commit -m "feat: add Cta4jFollowException"
```

---

### Task 9: `Cta4jLocationsException`

**Files:**
- Create: `src/main/java/com/cta4j/train/exception/Cta4jLocationsException.java`
- Test: `src/test/java/com/cta4j/train/exception/Cta4jLocationsExceptionTest.java`

**Interfaces:**
- Consumes: `Cta4jTrainException(String, String)`, `Cta4jTrainException(String, String, Throwable)` (Task 6); `LocationsErrorCode` (Task 3).
- Produces: `Cta4jLocationsException(String message, String endpoint, int rawErrorCode, LocationsErrorCode errorCode)`, `Cta4jLocationsException(String message, String endpoint, Throwable cause)`, `rawErrorCode(): Integer`, `errorCode(): LocationsErrorCode`. Consumed by Task 15 (`LocationsApiImpl`).

- [ ] **Step 1: Write the failing test**

```java
package com.cta4j.train.exception;

import com.cta4j.train.location.model.LocationsErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Cta4jLocationsExceptionTest {
    @Test
    void constructor_setsFields_forGenuineError() {
        Cta4jLocationsException exception = new Cta4jLocationsException(
            "Invalid route identifier: xyz",
            "/api/1.0/ttpositions.aspx",
            106,
            LocationsErrorCode.INVALID_ROUTE
        );

        assertThat(exception.getMessage()).isEqualTo("Invalid route identifier: xyz");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttpositions.aspx");
        assertThat(exception.rawErrorCode()).isEqualTo(106);
        assertThat(exception.errorCode()).isEqualTo(LocationsErrorCode.INVALID_ROUTE);
    }

    @Test
    void constructor_leavesRawErrorCodeAndErrorCodeNull_forParseFailure() {
        Throwable cause = new RuntimeException("root cause");

        Cta4jLocationsException exception = new Cta4jLocationsException(
            "Failed to parse response",
            "/api/1.0/ttpositions.aspx",
            cause
        );

        assertThat(exception.getMessage()).isEqualTo("Failed to parse response");
        assertThat(exception.endpoint()).isEqualTo("/api/1.0/ttpositions.aspx");
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.rawErrorCode()).isNull();
        assertThat(exception.errorCode()).isNull();
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -o -Dtest=Cta4jLocationsExceptionTest test -Djacoco.skip=true`
Expected: FAIL to compile — `Cta4jLocationsException` does not exist.

- [ ] **Step 3: Write minimal implementation**

```java
package com.cta4j.train.exception;

import com.cta4j.train.location.model.LocationsErrorCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A {@link Cta4jTrainException} thrown by CTA Train Tracker Locations API calls.
 */
@NullMarked
public final class Cta4jLocationsException extends Cta4jTrainException {
    private final @Nullable Integer rawErrorCode;
    private final @Nullable LocationsErrorCode errorCode;

    /**
     * Constructs a {@code Cta4jLocationsException} from a genuine CTA-reported error.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param rawErrorCode the raw CTA error code
     * @param errorCode the {@link LocationsErrorCode} corresponding to {@code rawErrorCode}
     * @throws NullPointerException if {@code endpoint} or {@code errorCode} is {@code null}
     */
    public Cta4jLocationsException(String message, String endpoint, int rawErrorCode, LocationsErrorCode errorCode) {
        super(message, endpoint);

        this.rawErrorCode = rawErrorCode;
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    /**
     * Constructs a {@code Cta4jLocationsException} for a parse or transport failure.
     *
     * @param message the detail message
     * @param endpoint the API endpoint associated with this exception
     * @param cause the cause of the exception
     * @throws NullPointerException if {@code endpoint} is {@code null}
     */
    public Cta4jLocationsException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);

        this.rawErrorCode = null;
        this.errorCode = null;
    }

    /**
     * Gets the raw CTA error code, or {@code null} if this exception was not caused by a CTA-reported error.
     *
     * @return the raw CTA error code, or {@code null}
     */
    public @Nullable Integer rawErrorCode() {
        return this.rawErrorCode;
    }

    /**
     * Gets the {@link LocationsErrorCode}, or {@code null} if this exception was not caused by a CTA-reported error.
     *
     * @return the {@link LocationsErrorCode}, or {@code null}
     */
    public @Nullable LocationsErrorCode errorCode() {
        return this.errorCode;
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -o -Dtest=Cta4jLocationsExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 2, Failures: 0, Errors: 0`

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/exception/Cta4jLocationsException.java src/test/java/com/cta4j/train/exception/Cta4jLocationsExceptionTest.java
git commit -m "feat: add Cta4jLocationsException"
```

---

### Task 10: Seal the hierarchy

**Files:**
- Modify: `src/main/java/com/cta4j/train/exception/Cta4jTrainException.java`
- Modify: `src/main/java/com/cta4j/exception/Cta4jException.java`

**Interfaces:**
- No new public members. This task only adds `sealed`/`permits` now that every permitted subclass (Tasks 5-9) exists.

- [ ] **Step 1: Seal `Cta4jTrainException`**

Edit `src/main/java/com/cta4j/train/exception/Cta4jTrainException.java`:

```java
// before
public class Cta4jTrainException extends Cta4jException {

// after
public sealed class Cta4jTrainException extends Cta4jException
    permits Cta4jArrivalsException, Cta4jFollowException, Cta4jLocationsException {
```

- [ ] **Step 2: Run the train exception tests to verify this compiles**

Run: `mvn -o -Dtest=Cta4jTrainExceptionTest,Cta4jArrivalsExceptionTest,Cta4jFollowExceptionTest,Cta4jLocationsExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 8, Failures: 0, Errors: 0`

- [ ] **Step 3: Seal `Cta4jException`**

Edit `src/main/java/com/cta4j/exception/Cta4jException.java`:

```java
// before
public class Cta4jException extends RuntimeException {

// after
public sealed class Cta4jException extends RuntimeException permits Cta4jBusException, Cta4jTrainException {
```

Add the import: `import com.cta4j.bus.exception.Cta4jBusException;` and `import com.cta4j.train.exception.Cta4jTrainException;` to `Cta4jException.java`.

- [ ] **Step 4: Run the full exception test suite to verify this compiles**

Run: `mvn -o -Dtest=Cta4jExceptionTest,Cta4jBusExceptionTest,Cta4jTrainExceptionTest,Cta4jArrivalsExceptionTest,Cta4jFollowExceptionTest,Cta4jLocationsExceptionTest test -Djacoco.skip=true`
Expected: `Tests run: 15, Failures: 0, Errors: 0`

Note: `mvn compile` for the *whole module* will still fail after this task — every existing call site in `bus`/`train` impls, `Qualifiers`, `HttpClient`, and the `*Api` interfaces still uses the old `Cta4jException` constructors removed in Task 4. Tasks 11-19 fix every remaining call site. Do not expect a full `mvn compile` to succeed until Task 19 is done.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/exception/Cta4jException.java src/main/java/com/cta4j/train/exception/Cta4jTrainException.java
git commit -m "feat: seal Cta4jException and Cta4jTrainException hierarchies"
```

---

### Task 11: Retarget bus `*ApiImpl` classes

**Files:**
- Modify: `src/main/java/com/cta4j/bus/route/internal/impl/RoutesApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/stop/internal/impl/StopsApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/pattern/internal/impl/PatternsApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/prediction/internal/impl/PredictionsApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/direction/internal/impl/DirectionsApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/locale/internal/impl/LocalesApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/detour/internal/impl/DetoursApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/vehicle/internal/impl/VehiclesApiImpl.java`
- Modify: `src/main/java/com/cta4j/bus/common/internal/impl/SystemTimeApiImpl.java`

**Interfaces:**
- Consumes: `Cta4jBusException` (Task 5).
- Produces: no new public members — this task only retargets internal throw sites. `ApiUtils.buildErrorMessage(String, List<? extends CtaError>)` calls are removed here since `Cta4jBusException`'s constructor now does the joining itself (the method is deleted in Task 18, after every caller stops using it).

Every file gets the same two edits: add `import com.cta4j.bus.exception.Cta4jBusException;`, retarget the parse-failure `catch` block, and replace the `buildErrorMessage` + `throw` pair with a direct `Cta4jBusException` construction.

- [ ] **Step 1: `RoutesApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ROUTES_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data check unchanged)
        String message = ApiUtils.buildErrorMessage(ROUTES_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ROUTES_ENDPOINT);

            throw new Cta4jBusException(message, ROUTES_ENDPOINT, e);
        }
        // ... (data check unchanged)
        throw new Cta4jBusException(errors, ROUTES_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 2: `StopsApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(STOPS_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data + not-found check unchanged)
        String message = ApiUtils.buildErrorMessage(STOPS_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(STOPS_ENDPOINT);

            throw new Cta4jBusException(message, STOPS_ENDPOINT, e);
        }
        // ... (data + not-found check unchanged)
        throw new Cta4jBusException(errors, STOPS_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 3: `PatternsApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(PATTERNS_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data + not-found check unchanged)
        String message = ApiUtils.buildErrorMessage(PATTERNS_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(PATTERNS_ENDPOINT);

            throw new Cta4jBusException(message, PATTERNS_ENDPOINT, e);
        }
        // ... (data + not-found check unchanged)
        throw new Cta4jBusException(errors, PATTERNS_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 4: `PredictionsApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(PREDICTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data + not-found check unchanged)
        String message = ApiUtils.buildErrorMessage(PREDICTIONS_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(PREDICTIONS_ENDPOINT);

            throw new Cta4jBusException(message, PREDICTIONS_ENDPOINT, e);
        }
        // ... (data + not-found check unchanged)
        throw new Cta4jBusException(errors, PREDICTIONS_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 5: `DirectionsApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(DIRECTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data + not-found check unchanged)
        String message = ApiUtils.buildErrorMessage(DIRECTIONS_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(DIRECTIONS_ENDPOINT);

            throw new Cta4jBusException(message, DIRECTIONS_ENDPOINT, e);
        }
        // ... (data + not-found check unchanged)
        throw new Cta4jBusException(errors, DIRECTIONS_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 6: `LocalesApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(LOCALES_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data check unchanged)
        String message = ApiUtils.buildErrorMessage(LOCALES_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(LOCALES_ENDPOINT);

            throw new Cta4jBusException(message, LOCALES_ENDPOINT, e);
        }
        // ... (data check unchanged)
        throw new Cta4jBusException(errors, LOCALES_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 7: `DetoursApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(DETOURS_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data + not-found check unchanged)
        String message = ApiUtils.buildErrorMessage(DETOURS_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(DETOURS_ENDPOINT);

            throw new Cta4jBusException(message, DETOURS_ENDPOINT, e);
        }
        // ... (data + not-found check unchanged)
        throw new Cta4jBusException(errors, DETOURS_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 8: `VehiclesApiImpl.java`**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(VEHICLES_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ... (data + not-found check unchanged)
        String message = ApiUtils.buildErrorMessage(VEHICLES_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(VEHICLES_ENDPOINT);

            throw new Cta4jBusException(message, VEHICLES_ENDPOINT, e);
        }
        // ... (data + not-found check unchanged)
        throw new Cta4jBusException(errors, VEHICLES_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 9: `SystemTimeApiImpl.java`**

This one has an extra throw site (the "no system time data returned" branch, which has no error list and no cause):

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message, e);
        }
        // ...
        if (errors == null || errors.isEmpty()) {
            String message = "No system time data returned from %s".formatted(SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message);
        }

        String message = ApiUtils.buildErrorMessage(SYSTEM_TIME_ENDPOINT, errors);

        throw new Cta4jException(message);

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(SYSTEM_TIME_ENDPOINT);

            throw new Cta4jBusException(message, SYSTEM_TIME_ENDPOINT, e);
        }
        // ...
        if (errors == null || errors.isEmpty()) {
            String message = "No system time data returned from %s".formatted(SYSTEM_TIME_ENDPOINT);

            throw new Cta4jBusException(message, SYSTEM_TIME_ENDPOINT);
        }

        throw new Cta4jBusException(errors, SYSTEM_TIME_ENDPOINT);
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 10: Run the bus impl test suites to verify they still pass**

Run: `mvn -o -Dtest=RoutesApiImplTest,StopsApiImplTest,PatternsApiImplTest,PredictionsApiImplTest,DirectionsApiImplTest,LocalesApiImplTest,DetoursApiImplTest,VehiclesApiImplTest,SystemTimeApiImplTest test -Djacoco.skip=true`
Expected: existing assertions like `.isInstanceOf(Cta4jException.class)` still pass unchanged (subclass satisfies the check), since `Cta4jBusException extends Cta4jException`.

- [ ] **Step 11: Commit**

```bash
git add src/main/java/com/cta4j/bus/route/internal/impl/RoutesApiImpl.java \
        src/main/java/com/cta4j/bus/stop/internal/impl/StopsApiImpl.java \
        src/main/java/com/cta4j/bus/pattern/internal/impl/PatternsApiImpl.java \
        src/main/java/com/cta4j/bus/prediction/internal/impl/PredictionsApiImpl.java \
        src/main/java/com/cta4j/bus/direction/internal/impl/DirectionsApiImpl.java \
        src/main/java/com/cta4j/bus/locale/internal/impl/LocalesApiImpl.java \
        src/main/java/com/cta4j/bus/detour/internal/impl/DetoursApiImpl.java \
        src/main/java/com/cta4j/bus/vehicle/internal/impl/VehiclesApiImpl.java \
        src/main/java/com/cta4j/bus/common/internal/impl/SystemTimeApiImpl.java
git commit -m "feat: retarget bus ApiImpl classes to Cta4jBusException"
```

---

### Task 12: Retarget bus `*Api` interfaces (javadoc + `findById` default methods)

**Files:**
- Modify: `src/main/java/com/cta4j/bus/route/RoutesApi.java`
- Modify: `src/main/java/com/cta4j/bus/stop/StopsApi.java`
- Modify: `src/main/java/com/cta4j/bus/pattern/PatternsApi.java`
- Modify: `src/main/java/com/cta4j/bus/prediction/PredictionsApi.java`
- Modify: `src/main/java/com/cta4j/bus/direction/DirectionsApi.java`
- Modify: `src/main/java/com/cta4j/bus/locale/LocalesApi.java`
- Modify: `src/main/java/com/cta4j/bus/detour/DetoursApi.java`
- Modify: `src/main/java/com/cta4j/bus/vehicle/VehiclesApi.java`
- Modify: `src/main/java/com/cta4j/bus/BusApi.java`

**Interfaces:**
- Consumes: `Cta4jBusException` (Task 5), `ApiUtils.API_PREFIX` (`com.cta4j.bus.common.internal.util.ApiUtils`, existing).
- Produces: no new public members — javadoc-only changes, except `StopsApi`/`PatternsApi`/`VehiclesApi`'s `findById` default methods, which retarget their direct `Cta4jException` construction.

`StopsApi`, `PatternsApi`, `VehiclesApi` each have a `findById`-style default method that constructs a "multiple results found" exception directly, with no access to the impl's private `ENDPOINT` constant. Recompute the endpoint the same way the impl does (`"%s/<path>".formatted(ApiUtils.API_PREFIX)`), and use `Cta4jBusException`'s 2-arg `(String, String)` constructor since there's no cause or error list here — just an SDK-detected consistency violation.

- [ ] **Step 1: `RoutesApi.java`**

```java
// before
     * @throws Cta4jException if the API returns an error response or the response cannot be parsed

// after
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
```

Replace `import com.cta4j.exception.Cta4jException;` with `import com.cta4j.bus.exception.Cta4jBusException;`.

- [ ] **Step 2: `StopsApi.java`**

```java
// add import
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.exception.Cta4jBusException;

// before
     * @throws Cta4jException if the API returns an error response or the response cannot be parsed
     */
    List<Stop> findByRouteIdAndDirection(String routeId, String direction);

    /**
     * Retrieves stops by their IDs.
     * ...
     * @throws Cta4jException if the API returns an error response or the response cannot be parsed
     */
    List<Stop> findByIds(Collection<String> stopIds);

    /**
     * Retrieves a stop by its ID.
     * ...
     * @throws Cta4jException if multiple stops are found for the given ID, or if the API returns an error
     * response or the response cannot be parsed
     */
    default Optional<Stop> findById(String stopId) {
        Objects.requireNonNull(stopId);

        List<String> ids = List.of(stopId);

        List<Stop> stops = this.findByIds(ids);

        if (stops.isEmpty()) {
            return Optional.empty();
        }

        if (stops.size() > 1) {
            String message = "Multiple stops found for ID: %s".formatted(stopId);

            throw new Cta4jException(message);
        }

        Stop stop = stops.getFirst();

        return Optional.of(stop);
    }

// after
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
     */
    List<Stop> findByRouteIdAndDirection(String routeId, String direction);

    /**
     * Retrieves stops by their IDs.
     * ...
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
     */
    List<Stop> findByIds(Collection<String> stopIds);

    /**
     * Retrieves a stop by its ID.
     * ...
     * @throws Cta4jBusException if multiple stops are found for the given ID, or if the API returns an error
     * response or the response cannot be parsed
     */
    default Optional<Stop> findById(String stopId) {
        Objects.requireNonNull(stopId);

        List<String> ids = List.of(stopId);

        List<Stop> stops = this.findByIds(ids);

        if (stops.isEmpty()) {
            return Optional.empty();
        }

        if (stops.size() > 1) {
            String message = "Multiple stops found for ID: %s".formatted(stopId);
            String endpoint = "%s/getstops".formatted(ApiUtils.API_PREFIX);

            throw new Cta4jBusException(message, endpoint);
        }

        Stop stop = stops.getFirst();

        return Optional.of(stop);
    }
```

- [ ] **Step 3: `PatternsApi.java`** (same shape as `StopsApi`)

```java
// add import
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.exception.Cta4jBusException;

// before (findById body)
        if (patterns.size() > 1) {
            String message = "Multiple route patterns found for ID: %s".formatted(patternId);

            throw new Cta4jException(message);
        }

// after
        if (patterns.size() > 1) {
            String message = "Multiple route patterns found for ID: %s".formatted(patternId);
            String endpoint = "%s/getpatterns".formatted(ApiUtils.API_PREFIX);

            throw new Cta4jBusException(message, endpoint);
        }
```

Also update both `@throws Cta4jException` javadoc lines (on `findByIds` and `findById`) to `@throws Cta4jBusException`, and the one on `findByRouteId`.

- [ ] **Step 4: `VehiclesApi.java`** (same shape)

```java
// add import
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.exception.Cta4jBusException;

// before (findById body)
        if (vehicles.size() > 1) {
            String message = "Expected at most one vehicle for ID: %s, but found %d".formatted(
                id,
                vehicles.size()
            );

            throw new Cta4jException(message);
        }

// after
        if (vehicles.size() > 1) {
            String message = "Expected at most one vehicle for ID: %s, but found %d".formatted(
                id,
                vehicles.size()
            );
            String endpoint = "%s/getvehicles".formatted(ApiUtils.API_PREFIX);

            throw new Cta4jBusException(message, endpoint);
        }
```

Also update all three `@throws Cta4jException` javadoc lines to `@throws Cta4jBusException`.

- [ ] **Step 5: `PredictionsApi.java`, `DirectionsApi.java`, `LocalesApi.java`, `DetoursApi.java`**

Each: replace `import com.cta4j.exception.Cta4jException;` with `import com.cta4j.bus.exception.Cta4jBusException;`, and replace every `@throws Cta4jException` javadoc line with `@throws Cta4jBusException`. No default-method code changes needed in these four (they don't have a `findById`-style default that constructs an exception directly).

- [ ] **Step 6: `BusApi.java`**

```java
// before

import com.cta4j.common.exception.Cta4jException;
// ...
     *@throws Cta4jException if
the API
returns an
error response
or the
response cannot
be parsed
     */

Instant systemTime();

// after
import com.cta4j.bus.exception.Cta4jBusException;
// ...
     *@throws Cta4jBusException if
the API
returns an
error response
or the
response cannot
be parsed
     */

Instant systemTime();
```

- [ ] **Step 7: Run the full bus test suite to verify everything still compiles and passes**

Run: `mvn -o test -Djacoco.skip=true`
Expected: `BUILD SUCCESS` (this is the first point where the *whole module* compiles again, since bus is now fully retargeted — train is still pending Tasks 13-17).

Note: the module will still not fully compile until train is also retargeted (Tasks 13-17), since `HttpClient` (Task 19) and train call sites still reference removed `Cta4jException` constructors. If `mvn test` fails here, confirm the failures are isolated to `train/**` and `HttpClient.java`/`HttpClientTest.java` — nothing under `bus/**` should fail to compile at this point.

- [ ] **Step 8: Commit**

```bash
git add src/main/java/com/cta4j/bus/route/RoutesApi.java \
        src/main/java/com/cta4j/bus/stop/StopsApi.java \
        src/main/java/com/cta4j/bus/pattern/PatternsApi.java \
        src/main/java/com/cta4j/bus/prediction/PredictionsApi.java \
        src/main/java/com/cta4j/bus/direction/DirectionsApi.java \
        src/main/java/com/cta4j/bus/locale/LocalesApi.java \
        src/main/java/com/cta4j/bus/detour/DetoursApi.java \
        src/main/java/com/cta4j/bus/vehicle/VehiclesApi.java \
        src/main/java/com/cta4j/bus/BusApi.java
git commit -m "feat: retarget bus Api interfaces to Cta4jBusException"
```

---

### Task 13: Retarget bus `Qualifiers`

**Files:**
- Modify: `src/main/java/com/cta4j/bus/common/internal/mapper/Qualifiers.java`

**Interfaces:**
- Consumes: `Cta4jBusException` (Task 5).
- Produces: no new public members.

`Qualifiers` is shared across every bus feature's mapper — it has no `ENDPOINT` constant or per-feature context, only the raw field value it's parsing. Use the qualifier method name itself as the `endpoint` argument (the most specific, genuinely meaningful string available at this call site — more specific than a shared HTTP endpoint would be).

- [ ] **Step 1: Update the one throw site**

```java
// add import
import com.cta4j.bus.exception.Cta4jBusException;

// before
    @Named("mapTimestamp")
    public static @Nullable Instant mapTimestamp(@Nullable String timestamp) {
        if (timestamp == null) {
            return null;
        }

        try {
            return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER)
                                .atZone(CHICAGO_ZONE_ID)
                                .toInstant();
        } catch (DateTimeParseException e) {
            String message = "Failed to parse timestamp: %s".formatted(timestamp);

            throw new Cta4jException(message, e);
        }
    }

// after
    @Named("mapTimestamp")
    public static @Nullable Instant mapTimestamp(@Nullable String timestamp) {
        if (timestamp == null) {
            return null;
        }

        try {
            return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER)
                                .atZone(CHICAGO_ZONE_ID)
                                .toInstant();
        } catch (DateTimeParseException e) {
            String message = "Failed to parse timestamp: %s".formatted(timestamp);

            throw new Cta4jBusException(message, "Qualifiers.mapTimestamp", e);
        }
    }
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 2: Run `BusQualifiersTest` to verify it still passes**

Run: `mvn -o -Dtest=BusQualifiersTest test -Djacoco.skip=true`
Expected: existing `.isInstanceOf(Cta4jException.class)` assertions still pass (subclass satisfies the check).

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/cta4j/bus/common/internal/mapper/Qualifiers.java
git commit -m "feat: retarget bus Qualifiers to Cta4jBusException"
```

---

### Task 14: Delete train `CtaError` and `buildErrorMessage(String, CtaError)`; retarget `parseErrCd`

**Files:**
- Modify: `src/main/java/com/cta4j/train/common/internal/util/ApiUtils.java`
- Delete: `src/main/java/com/cta4j/train/common/internal/wire/CtaError.java`
- Delete: `src/test/java/com/cta4j/train/common/internal/wire/CtaErrorTest.java`
- Modify: `src/test/java/com/cta4j/train/common/internal/util/ApiUtilsTest.java`

**Interfaces:**
- Consumes: `Cta4jTrainException` (Task 6).
- Produces: `ApiUtils.parseErrCd(String errCd, String endpoint): int` (signature unchanged, now throws `Cta4jTrainException` instead of `Cta4jException`). `ApiUtils.buildErrorMessage(String, CtaError)` is removed entirely.

`parseErrCd` is shared across `ArrivalsApiImpl`/`FollowApiImpl`/`LocationsApiImpl` with no per-feature context (same reasoning as `Qualifiers`), so it throws the middle-tier `Cta4jTrainException`, not one of the leaf types.

- [ ] **Step 1: Update `ApiUtils.java`**

```java
// before
package com.cta4j.train.common.internal.util;

import com.cta4j.common.exception.Cta4jException;
import com.cta4j.train.common.internal.wire.CtaError;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "lapi.transitchicago.com";
    public static final String DEFAULT_STATIONS_URL = "https://data.cityofchicago.org/resource/8pix-ypme.json";
    public static final String API_PREFIX = "/api/1.0";

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int parseErrCd(String errCd, String endpoint) {
        Objects.requireNonNull(errCd);
        Objects.requireNonNull(endpoint);

        int code;

        try {
            code = Integer.parseInt(errCd);
        } catch (NumberFormatException e) {
            String message = "Failed to parse error code from %s".formatted(endpoint);

            throw new Cta4jException(message, e);
        }

        if (code < 0) {
            String message = "Unknown error code %d from %s".formatted(code, endpoint);

            throw new Cta4jException(message);
        }

        return code;
    }

    public static String buildErrorMessage(String endpoint, CtaError error) {
        Objects.requireNonNull(endpoint);
        Objects.requireNonNull(error);

        return "Error response from %s: [%d] %s".formatted(endpoint, error.code(), error.message());
    }
}

// after
package com.cta4j.train.common.internal.util;

import com.cta4j.train.exception.Cta4jTrainException;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "lapi.transitchicago.com";
    public static final String DEFAULT_STATIONS_URL = "https://data.cityofchicago.org/resource/8pix-ypme.json";
    public static final String API_PREFIX = "/api/1.0";

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int parseErrCd(String errCd, String endpoint) {
        Objects.requireNonNull(errCd);
        Objects.requireNonNull(endpoint);

        int code;

        try {
            code = Integer.parseInt(errCd);
        } catch (NumberFormatException e) {
            String message = "Failed to parse error code from %s".formatted(endpoint);

            throw new Cta4jTrainException(message, endpoint, e);
        }

        if (code < 0) {
            String message = "Unknown error code %d from %s".formatted(code, endpoint);

            throw new Cta4jTrainException(message, endpoint);
        }

        return code;
    }
}
```

- [ ] **Step 2: Delete `CtaError.java` and `CtaErrorTest.java`**

```bash
git rm src/main/java/com/cta4j/train/common/internal/wire/CtaError.java
git rm src/test/java/com/cta4j/train/common/internal/wire/CtaErrorTest.java
```

- [ ] **Step 3: Update `ApiUtilsTest.java` (train)**

Read the current file first (`src/test/java/com/cta4j/train/common/internal/util/ApiUtilsTest.java`), then remove any test method referencing `buildErrorMessage` and update assertions that expect `Cta4jException.class` to expect `Cta4jTrainException.class` instead, e.g.:

```java
// before
    @Test
    void parseErrCd_throwsCta4jException_whenErrCdIsNotNumeric() {
        assertThatExceptionOfType(Cta4jException.class).isThrownBy(() ->
            ApiUtils.parseErrCd("not-a-number", "/api/1.0/ttarrivals.aspx"));
    }

// after
    @Test
    void parseErrCd_throwsCta4jTrainException_whenErrCdIsNotNumeric() {
        assertThatExceptionOfType(Cta4jTrainException.class).isThrownBy(() ->
            ApiUtils.parseErrCd("not-a-number", "/api/1.0/ttarrivals.aspx"));
    }
```

Update the import from `com.cta4j.exception.Cta4jException` to `com.cta4j.train.exception.Cta4jTrainException`, and delete any test method that calls the now-removed `buildErrorMessage(String, CtaError)` overload.

- [ ] **Step 4: Run the test**

Run: `mvn -o -Dtest=ApiUtilsTest#com.cta4j.train.common.internal.util test -Djacoco.skip=true`

(If the test-selector syntax above doesn't resolve due to the duplicate class name between bus/train `ApiUtilsTest`, run instead:)

Run: `mvn -o -Dtest=com.cta4j.train.common.internal.util.ApiUtilsTest test -Djacoco.skip=true`
Expected: all tests pass, none reference `CtaError` or `buildErrorMessage(String, CtaError)`.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/cta4j/train/common/internal/util/ApiUtils.java src/test/java/com/cta4j/train/common/internal/util/ApiUtilsTest.java
git commit -m "refactor: remove train CtaError and buildErrorMessage, retarget parseErrCd to Cta4jTrainException"
```

---

### Task 15: Retarget train `*ApiImpl` classes

**Files:**
- Modify: `src/main/java/com/cta4j/train/arrival/internal/impl/ArrivalsApiImpl.java`
- Modify: `src/main/java/com/cta4j/train/follow/internal/impl/FollowApiImpl.java`
- Modify: `src/main/java/com/cta4j/train/location/internal/impl/LocationsApiImpl.java`
- Modify: `src/main/java/com/cta4j/train/station/internal/impl/StationsApiImpl.java`

**Interfaces:**
- Consumes: `Cta4jArrivalsException` (Task 7), `Cta4jFollowException` (Task 8), `Cta4jLocationsException` (Task 9), `Cta4jTrainException` (Task 6), `ArrivalsErrorCode`/`FollowErrorCode`/`LocationsErrorCode` (Tasks 1-3).
- Produces: no new public members.

- [ ] **Step 1: `ArrivalsApiImpl.java`**

```java
// add imports
import com.cta4j.train.arrival.model.ArrivalsErrorCode;
import com.cta4j.train.exception.Cta4jArrivalsException;

// before
    private static final String ARRIVALS_ENDPOINT = "%s/ttarrivals.aspx".formatted(ApiUtils.API_PREFIX);
    private static final int INVALID_MAPID_ERROR_CODE = 103;
    private static final int INVALID_STPID_ERROR_CODE = 108;
    private static final TypeReference<CtaResponse<CtaArrivalsResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    // ...

        try {
            ctaResponse = JsonMapper.shared()
                                    .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ARRIVALS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaArrivalsResponse arrivalsResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(arrivalsResponse.errCd(), ARRIVALS_ENDPOINT);

        if ((errCd == INVALID_MAPID_ERROR_CODE) || (errCd == INVALID_STPID_ERROR_CODE)) {
            return List.of();
        }

        if (errCd != 0) {
            CtaError error = new CtaError(errCd, arrivalsResponse.errNm());

            String message = ApiUtils.buildErrorMessage(ARRIVALS_ENDPOINT, error);

            throw new Cta4jException(message);
        }

// after
    private static final String ARRIVALS_ENDPOINT = "%s/ttarrivals.aspx".formatted(ApiUtils.API_PREFIX);
    private static final TypeReference<CtaResponse<CtaArrivalsResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    // ...

        try {
            ctaResponse = JsonMapper.shared()
                                    .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ARRIVALS_ENDPOINT);

            throw new Cta4jArrivalsException(message, ARRIVALS_ENDPOINT, e);
        }

        CtaArrivalsResponse arrivalsResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(arrivalsResponse.errCd(), ARRIVALS_ENDPOINT);

        ArrivalsErrorCode errorCode = ArrivalsErrorCode.fromCode(errCd);

        if ((errorCode == ArrivalsErrorCode.INVALID_MAPID) || (errorCode == ArrivalsErrorCode.INVALID_STPID)) {
            return List.of();
        }

        if (errCd != 0) {
            throw new Cta4jArrivalsException(arrivalsResponse.errNm(), ARRIVALS_ENDPOINT, errCd, errorCode);
        }
```

Remove the now-unused imports: `com.cta4j.exception.Cta4jException`, `com.cta4j.train.common.internal.wire.CtaError`.

- [ ] **Step 2: `FollowApiImpl.java`**

```java
// add imports
import com.cta4j.train.follow.model.FollowErrorCode;
import com.cta4j.train.exception.Cta4jFollowException;

// before
    private static final String FOLLOW_ENDPOINT = "%s/ttfollow.aspx".formatted(ApiUtils.API_PREFIX);
    private static final int NOT_FOUND_ERROR_CODE = 501;
    private static final TypeReference<CtaResponse<CtaFollowResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    // ...

        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(FOLLOW_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaFollowResponse followResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(followResponse.errCd(), FOLLOW_ENDPOINT);

        if (errCd == NOT_FOUND_ERROR_CODE) {
            return Optional.empty();
        }

        if (errCd != 0) {
            CtaError error = new CtaError(errCd, followResponse.errNm());

            String message = ApiUtils.buildErrorMessage(FOLLOW_ENDPOINT, error);

            throw new Cta4jException(message);
        }

// after
    private static final String FOLLOW_ENDPOINT = "%s/ttfollow.aspx".formatted(ApiUtils.API_PREFIX);
    private static final TypeReference<CtaResponse<CtaFollowResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    // ...

        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(FOLLOW_ENDPOINT);

            throw new Cta4jFollowException(message, FOLLOW_ENDPOINT, e);
        }

        CtaFollowResponse followResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(followResponse.errCd(), FOLLOW_ENDPOINT);

        FollowErrorCode errorCode = FollowErrorCode.fromCode(errCd);

        if (errorCode == FollowErrorCode.RUN_NOT_FOUND) {
            return Optional.empty();
        }

        if (errCd != 0) {
            throw new Cta4jFollowException(followResponse.errNm(), FOLLOW_ENDPOINT, errCd, errorCode);
        }
```

Remove the now-unused imports: `com.cta4j.exception.Cta4jException`, `com.cta4j.train.common.internal.wire.CtaError`.

- [ ] **Step 3: `LocationsApiImpl.java`**

```java
// add imports
import com.cta4j.train.location.model.LocationsErrorCode;
import com.cta4j.train.exception.Cta4jLocationsException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(POSITIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaLocationResponse locationResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(locationResponse.errCd(), POSITIONS_ENDPOINT);

        if (errCd != 0) {
            CtaError error = new CtaError(errCd, locationResponse.errNm());

            String message = ApiUtils.buildErrorMessage(POSITIONS_ENDPOINT, error);

            throw new Cta4jException(message);
        }

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(POSITIONS_ENDPOINT);

            throw new Cta4jLocationsException(message, POSITIONS_ENDPOINT, e);
        }

        CtaLocationResponse locationResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(locationResponse.errCd(), POSITIONS_ENDPOINT);

        if (errCd != 0) {
            LocationsErrorCode errorCode = LocationsErrorCode.fromCode(errCd);

            throw new Cta4jLocationsException(locationResponse.errNm(), POSITIONS_ENDPOINT, errCd, errorCode);
        }
```

Remove the now-unused imports: `com.cta4j.exception.Cta4jException`, `com.cta4j.train.common.internal.wire.CtaError`.

- [ ] **Step 4: `StationsApiImpl.java`**

```java
// add import
import com.cta4j.train.exception.Cta4jTrainException;

// before
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(this.config.stationsUrl());

            throw new Cta4jException(message, e);
        }

// after
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(this.config.stationsUrl());

            throw new Cta4jTrainException(message, this.config.stationsUrl(), e);
        }
```

Remove the now-unused `import com.cta4j.exception.Cta4jException;`.

- [ ] **Step 5: Run the train impl test suites**

Run: `mvn -o -Dtest=ArrivalsApiImplTest,FollowApiImplTest,LocationsApiImplTest,StationsApiImplTest test -Djacoco.skip=true`
Expected: existing `.isInstanceOf(Cta4jException.class)` assertions still pass unchanged.

- [ ] **Step 6: Commit**

```bash
git add src/main/java/com/cta4j/train/arrival/internal/impl/ArrivalsApiImpl.java \
        src/main/java/com/cta4j/train/follow/internal/impl/FollowApiImpl.java \
        src/main/java/com/cta4j/train/location/internal/impl/LocationsApiImpl.java \
        src/main/java/com/cta4j/train/station/internal/impl/StationsApiImpl.java
git commit -m "feat: retarget train ApiImpl classes to per-endpoint exceptions"
```

---

### Task 16: Add errorCode assertions to existing train impl tests, and retarget `@throws` javadoc on train `*Api` interfaces

**Files:**
- Modify: `src/test/java/com/cta4j/train/arrival/ArrivalsApiImplTest.java`
- Modify: `src/test/java/com/cta4j/train/follow/FollowApiImplTest.java`
- Modify: `src/test/java/com/cta4j/train/location/LocationsApiImplTest.java`
- Modify: `src/main/java/com/cta4j/train/arrival/ArrivalsApi.java`
- Modify: `src/main/java/com/cta4j/train/follow/FollowApi.java`
- Modify: `src/main/java/com/cta4j/train/location/LocationsApi.java`
- Modify: `src/main/java/com/cta4j/train/station/StationsApi.java`

**Interfaces:**
- Consumes: `Cta4jArrivalsException.errorCode()`, `Cta4jFollowException.errorCode()`, `Cta4jLocationsException.errorCode()` (Tasks 7-9).

`ArrivalsApi`, `FollowApi`, `LocationsApi`, `StationsApi` currently have **no** `@throws Cta4jException` javadoc at all (a pre-existing gap) — this task adds it fresh, along with a new assertion on `errorCode()` in each impl test's existing fatal-error test.

- [ ] **Step 1: Add an `errorCode()` assertion to `ArrivalsApiImplTest.findByMapId_throwsCta4jException_whenResponseContainsError`**

```java
// before
    @Test
    void findByMapId_throwsCta4jException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/error.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jException.class);
    }

// after
    @Test
    void findByMapId_throwsCta4jArrivalsException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttarrivals.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/arrival/error.json"))));

        MapArrivalQuery query = MapArrivalQuery.builder("40900").build();

        assertThatThrownBy(() -> this.api.findByMapId(query))
            .isInstanceOf(Cta4jArrivalsException.class)
            .satisfies(e -> assertThat(((Cta4jArrivalsException) e).errorCode()).isEqualTo(ArrivalsErrorCode.INVALID_API_KEY));
    }
```

(`train/arrival/error.json` has `errCd: "1"`, which is not one of the documented codes — confirm by adding `ArrivalsErrorCode.fromCode(1)` maps to `UNKNOWN` and use that in the assertion instead if `1` isn't a valid code. Since `1` isn't in the documented table, use `ArrivalsErrorCode.UNKNOWN` in the assertion.)

Add imports: `com.cta4j.train.arrival.model.ArrivalsErrorCode`, `com.cta4j.train.exception.Cta4jArrivalsException`. Remove `com.cta4j.exception.Cta4jException` if no longer referenced elsewhere in the file (check `findByMapId_throwsCta4jException_whenResponseIsNotJson` and `findByMapId_throwsCta4jException_whenErrCdIsNotNumeric` — those can stay asserting `Cta4jArrivalsException.class` too, since it's still the concrete type thrown).

- [ ] **Step 2: Same treatment for `FollowApiImplTest.findByRun_throwsCta4jException_whenResponseContainsError`**

```java
// before
    @Test
    void findByRun_throwsCta4jException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/error.json"))));

        assertThatThrownBy(() -> this.api.findByRun("123"))
            .isInstanceOf(Cta4jException.class);
    }

// after
    @Test
    void findByRun_throwsCta4jFollowException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/error.json"))));

        assertThatThrownBy(() -> this.api.findByRun("123"))
            .isInstanceOf(Cta4jFollowException.class);
    }
```

Add imports: `com.cta4j.train.exception.Cta4jFollowException`. (Check `train/follow/error.json`'s `errCd` value to decide whether to also assert `errorCode()`; if it's an undocumented code, assert `FollowErrorCode.UNKNOWN`.)

- [ ] **Step 3: Same treatment for `LocationsApiImplTest`'s fatal-error test**

Find the test asserting `.isInstanceOf(Cta4jException.class)` for a fatal `errCd` response, and change it to `Cta4jLocationsException.class`, adding the corresponding import.

- [ ] **Step 4: `ArrivalsApi.java`**

```java
// before
    /**
     * Retrieves train arrivals for the specified map.
     *
     * @param query the {@link MapArrivalQuery} describing the map, and optional line and result-count filters
     * @return a {@link List} of {@link Arrival}s for the specified map, or an empty {@link List} if none are found
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Arrival> findByMapId(MapArrivalQuery query);

// after
    /**
     * Retrieves train arrivals for the specified map.
     *
     * @param query the {@link MapArrivalQuery} describing the map, and optional line and result-count filters
     * @return a {@link List} of {@link Arrival}s for the specified map, or an empty {@link List} if none are found
     * @throws NullPointerException if {@code query} is {@code null}
     * @throws Cta4jArrivalsException if the API returns a fatal error response or the response cannot be parsed
     */
    List<Arrival> findByMapId(MapArrivalQuery query);
```

Apply the same `@throws Cta4jArrivalsException ...` addition to `findByStopId(StopArrivalQuery)`, `findByMapId(String)`, and `findByStopId(String)`. Add `import com.cta4j.train.exception.Cta4jArrivalsException;`.

- [ ] **Step 5: `FollowApi.java`**

```java
// before
    /**
     * Retrieves a train by its run number.
     *
     * @param run the run number of the train
     * @return an {@link Optional} containing the {@link FollowTrain} if found, or an empty {@link Optional} if no
     * train exists with the given run number
     * @throws NullPointerException if {@code run} is {@code null}
     */
    Optional<FollowTrain> findByRun(String run);

// after
    /**
     * Retrieves a train by its run number.
     *
     * @param run the run number of the train
     * @return an {@link Optional} containing the {@link FollowTrain} if found, or an empty {@link Optional} if no
     * train exists with the given run number
     * @throws NullPointerException if {@code run} is {@code null}
     * @throws Cta4jFollowException if the API returns a fatal error response or the response cannot be parsed
     */
    Optional<FollowTrain> findByRun(String run);
```

Add `import com.cta4j.train.exception.Cta4jFollowException;`.

- [ ] **Step 6: `LocationsApi.java`**

Add `@throws Cta4jLocationsException if the API returns a fatal error response or the response cannot be parsed` to `findByLines`'s javadoc, and `import com.cta4j.train.exception.Cta4jLocationsException;`.

- [ ] **Step 7: `StationsApi.java`**

```java
// before
    /**
     * Retrieves all available stations.
     *
     * @return a {@link List} of all available {@link Station}s
     */
    List<Station> list();

// after
    /**
     * Retrieves all available stations.
     *
     * @return a {@link List} of all available {@link Station}s
     * @throws Cta4jTrainException if the response cannot be parsed
     */
    List<Station> list();
```

Add `import com.cta4j.train.exception.Cta4jTrainException;`.

- [ ] **Step 8: Run the train impl and interface-adjacent tests**

Run: `mvn -o -Dtest=ArrivalsApiImplTest,FollowApiImplTest,LocationsApiImplTest,StationsApiImplTest test -Djacoco.skip=true`
Expected: all pass.

- [ ] **Step 9: Commit**

```bash
git add src/test/java/com/cta4j/train/arrival/ArrivalsApiImplTest.java \
        src/test/java/com/cta4j/train/follow/FollowApiImplTest.java \
        src/test/java/com/cta4j/train/location/LocationsApiImplTest.java \
        src/main/java/com/cta4j/train/arrival/ArrivalsApi.java \
        src/main/java/com/cta4j/train/follow/FollowApi.java \
        src/main/java/com/cta4j/train/location/LocationsApi.java \
        src/main/java/com/cta4j/train/station/StationsApi.java
git commit -m "feat: assert errorCode() in train impl tests, document train Api throws"
```

---

### Task 17: Retarget train `Qualifiers`

**Files:**
- Modify: `src/main/java/com/cta4j/train/common/internal/mapper/Qualifiers.java`

**Interfaces:**
- Consumes: `Cta4jTrainException` (Task 6).
- Produces: no new public members.

Same reasoning as bus `Qualifiers` (Task 13) — shared across every train feature's mapper, no per-feature context, so throws the middle-tier `Cta4jTrainException` using the qualifier method name as the `endpoint` argument.

- [ ] **Step 1: Update all 6 throw sites**

```java
// add import, remove com.cta4j.exception.Cta4jException
import com.cta4j.train.exception.Cta4jTrainException;

// before (mapTimestamp)
        } catch (DateTimeParseException e) {
            String message = "Failed to parse timestamp: %s".formatted(timestamp);

            throw new Cta4jException(message, e);
        }

// after
        } catch (DateTimeParseException e) {
            String message = "Failed to parse timestamp: %s".formatted(timestamp);

            throw new Cta4jTrainException(message, "Qualifiers.mapTimestamp", e);
        }
```

```java
// before (map01ToBoolean)
            default -> {
                String message = "Invalid boolean value: %s. Expected 0 or 1".formatted(value);

                throw new Cta4jException(message);
            }

// after
            default -> {
                String message = "Invalid boolean value: %s. Expected 0 or 1".formatted(value);

                throw new Cta4jTrainException(message, "Qualifiers.map01ToBoolean");
            }
```

```java
// before (map15ToTrainDirection, both catch blocks)
        try {
            code = Integer.parseInt(direction);
        } catch (NumberFormatException e) {
            String message = "Failed to parse train direction: %s".formatted(direction);

            throw new Cta4jException(message, e);
        }

        try {
            return TrainDirection.fromCode(code);
        } catch (IllegalArgumentException e) {
            String message = "Failed to parse train direction: %s".formatted(direction);

            throw new Cta4jException(message, e);
        }

// after
        try {
            code = Integer.parseInt(direction);
        } catch (NumberFormatException e) {
            String message = "Failed to parse train direction: %s".formatted(direction);

            throw new Cta4jTrainException(message, "Qualifiers.map15ToTrainDirection", e);
        }

        try {
            return TrainDirection.fromCode(code);
        } catch (IllegalArgumentException e) {
            String message = "Failed to parse train direction: %s".formatted(direction);

            throw new Cta4jTrainException(message, "Qualifiers.map15ToTrainDirection", e);
        }
```

```java
// before (parseCoordinate)
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            String message = "Failed to parse coordinate: %s".formatted(value);

            throw new Cta4jException(message, e);
        }

// after
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            String message = "Failed to parse coordinate: %s".formatted(value);

            throw new Cta4jTrainException(message, "Qualifiers.parseCoordinate", e);
        }
```

```java
// before (parseHeading)
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String message = "Failed to parse heading: %s".formatted(value);

            throw new Cta4jException(message, e);
        }

// after
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String message = "Failed to parse heading: %s".formatted(value);

            throw new Cta4jTrainException(message, "Qualifiers.parseHeading", e);
        }
```

```java
// before (private mapCoordinates helper)
        } catch (NumberFormatException e) {
            String message = "Failed to parse coordinates: lat=%s, lon=%s, heading=%s".formatted(lat, lon, heading);

            throw new Cta4jException(message, e);
        }

// after
        } catch (NumberFormatException e) {
            String message = "Failed to parse coordinates: lat=%s, lon=%s, heading=%s".formatted(lat, lon, heading);

            throw new Cta4jTrainException(message, "Qualifiers.mapCoordinates", e);
        }
```

- [ ] **Step 2: Run `TrainQualifiersTest`**

Run: `mvn -o -Dtest=TrainQualifiersTest test -Djacoco.skip=true`
Expected: existing `.isInstanceOf(Cta4jException.class)` assertions still pass unchanged.

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/cta4j/train/common/internal/mapper/Qualifiers.java
git commit -m "feat: retarget train Qualifiers to Cta4jTrainException"
```

---

### Task 18: Remove bus `ApiUtils.buildErrorMessage(String, List<? extends CtaError>)`

**Files:**
- Modify: `src/main/java/com/cta4j/bus/common/internal/util/ApiUtils.java`
- Modify: `src/test/java/com/cta4j/bus/common/internal/util/ApiUtilsTest.java`

**Interfaces:**
- No public members remain related to `buildErrorMessage` — the method is deleted since `Cta4jBusException` now does its own message-joining (Task 5). By this point (after Task 11), no caller references it anymore.

- [ ] **Step 1: Remove the method from `ApiUtils.java`**

```java
// before
package com.cta4j.bus.common.internal.util;

import com.cta4j.bus.common.internal.wire.CtaError;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "ctabustracker.com";
    public static final String API_PREFIX = "/bustime/api/v3";
    public static final int MAX_IDS_PER_REQUEST = 10;

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String buildErrorMessage(String endpoint, List<? extends CtaError> errors) {
        Objects.requireNonNull(endpoint);
        Objects.requireNonNull(errors);

        errors = List.copyOf(errors);

        String message = errors.stream()
                               .map(CtaError::msg)
                               .reduce("%s; %s"::formatted)
                               .orElse("Unknown error");

        return "Error response from %s: %s".formatted(endpoint, message);
    }

    public static void requireMaxIds(Collection<String> ids, String label) {
        Objects.requireNonNull(ids);
        Objects.requireNonNull(label);

        if (ids.size() > MAX_IDS_PER_REQUEST) {
            String message = "A maximum of %d %s IDs can be requested at once, but %d were provided".formatted(
                MAX_IDS_PER_REQUEST,
                label,
                ids.size()
            );

            throw new IllegalArgumentException(message);
        }
    }
}

// after
package com.cta4j.bus.common.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class ApiUtils {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "ctabustracker.com";
    public static final String API_PREFIX = "/bustime/api/v3";
    public static final int MAX_IDS_PER_REQUEST = 10;

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void requireMaxIds(Collection<String> ids, String label) {
        Objects.requireNonNull(ids);
        Objects.requireNonNull(label);

        if (ids.size() > MAX_IDS_PER_REQUEST) {
            String message = "A maximum of %d %s IDs can be requested at once, but %d were provided".formatted(
                MAX_IDS_PER_REQUEST,
                label,
                ids.size()
            );

            throw new IllegalArgumentException(message);
        }
    }
}
```

- [ ] **Step 2: Update `ApiUtilsTest.java` (bus)**

Remove the four test methods covering `buildErrorMessage` (`buildErrorMessage_returnsSingleMessage_whenErrorsHasOneElement`, `buildErrorMessage_joinsMessages_whenErrorsHasMultipleElements`, `buildErrorMessage_returnsUnknownError_whenErrorsIsEmpty`, `buildErrorMessage_throwsNullPointerException_whenEndpointIsNull`, `buildErrorMessage_throwsNullPointerException_whenErrorsIsNull`) — that behavior now lives in, and is tested by, `Cta4jBusExceptionTest` (Task 5). Keep the `requireMaxIds_*` tests unchanged.

- [ ] **Step 3: Run the test**

Run: `mvn -o -Dtest=com.cta4j.bus.common.internal.util.ApiUtilsTest test -Djacoco.skip=true`
Expected: `Tests run: 4, Failures: 0, Errors: 0` (the four `requireMaxIds` tests only).

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/cta4j/bus/common/internal/util/ApiUtils.java src/test/java/com/cta4j/bus/common/internal/util/ApiUtilsTest.java
git commit -m "refactor: remove bus buildErrorMessage now that Cta4jBusException builds its own message"
```

---

### Task 19: Retarget `HttpClient`

**Files:**
- Modify: `src/main/java/com/cta4j/common/internal/http/HttpClient.java`
- Modify: `src/test/java/com/cta4j/common/internal/http/HttpClientTest.java`

**Interfaces:**
- Consumes: `Cta4jException(String, String, Throwable)` (Task 4).
- Produces: no new public members. This is the last file still using the pre-Task-4 constructor shape — after this task, `mvn compile` should succeed for the whole module.

- [ ] **Step 1: Update `HttpClient.java`**

```java
// before
    public static String get(String url) {
        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            String message = "Invalid URL";

            throw new Cta4jException(message, e);
        }

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (HttpResponseException e) {
            String path = uri.getPath();
            int statusCode = e.getStatusCode();

            String message = "Request to %s failed with status code %d".formatted(path, statusCode);

            throw new Cta4jException(message, e);
        } catch (IOException e) {
            String path = uri.getPath();

            String message = "Request to %s failed due to an I/O error".formatted(path);

            throw new Cta4jException(message, e);
        }

        return response;
    }

// after
    public static String get(String url) {
        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            String message = "Invalid URL";

            throw new Cta4jException(message, url, e);
        }

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (HttpResponseException e) {
            String path = uri.getPath();
            int statusCode = e.getStatusCode();

            String message = "Request to %s failed with status code %d".formatted(path, statusCode);

            throw new Cta4jException(message, path, e);
        } catch (IOException e) {
            String path = uri.getPath();

            String message = "Request to %s failed due to an I/O error".formatted(path);

            throw new Cta4jException(message, path, e);
        }

        return response;
    }
```

- [ ] **Step 2: Run `HttpClientTest`**

Run: `mvn -o -Dtest=HttpClientTest test -Djacoco.skip=true`
Expected: `Tests run: 4, Failures: 0, Errors: 0`.

- [ ] **Step 3: Run the full test suite for the whole module**

Run: `mvn -o test -Djacoco.skip=true`
Expected: `BUILD SUCCESS`, all tests pass. This is the first point since Task 4 where the entire module compiles again — if anything still fails to compile, it means a call site was missed in Tasks 11-18; find it with `grep -rn "new Cta4jException(" src/main` (should only match `HttpClient.java` at this point, using the new 3-arg constructor) and fix it before proceeding.

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/cta4j/common/internal/http/HttpClient.java
git commit -m "feat: retarget HttpClient to the new Cta4jException(message, endpoint, cause) constructor"
```

---

### Task 20: Update `CHANGELOG.md`

**Files:**
- Modify: `CHANGELOG.md`

**Interfaces:**
- None — documentation only.

- [ ] **Step 1: Correct the stale `final` bullet and add new entries**

```markdown
// before (in the [6.0.0] ### Changed section)
- `Cta4jException` is now `final` and can no longer be subclassed.

// after
- `Cta4jException` is now `sealed`, permitting only `Cta4jBusException` and `Cta4jTrainException` — it can still not
  be subclassed by consumers, but the SDK's own exception hierarchy can now be extended internally.
- `Cta4jException` gains an `endpoint()` accessor, populated on every exception the SDK throws.
- `getMessage()` on every `Cta4jException` now returns CTA's raw reported text verbatim, rather than an
  SDK-constructed "Error response from X: ..." template.
```

Add a new bullet list under `### Added` in the same `[6.0.0]` section:

```markdown
- `Cta4jBusException` — thrown by all CTA Bus Tracker API calls; adds no fields beyond `endpoint()`, since the bus
  API has no numeric error codes.
- `Cta4jTrainException` (sealed) and its per-endpoint subclasses `Cta4jArrivalsException`, `Cta4jFollowException`,
  `Cta4jLocationsException` — each exposes `errorCode()` (a typed enum: `ArrivalsErrorCode`, `FollowErrorCode`,
  `LocationsErrorCode`) and `rawErrorCode()` (the raw CTA integer, preserved even when `errorCode()` is `UNKNOWN`).
```

Add a bullet under `### Changed`:

```markdown
- Removed `train.common.internal.wire.CtaError` and both bus/train `ApiUtils.buildErrorMessage` helpers — message
  construction now lives in the exception constructors themselves.
```

- [ ] **Step 2: Run the full test suite one more time to confirm nothing regressed**

Run: `mvn -o test -Djacoco.skip=true`
Expected: `BUILD SUCCESS`.

- [ ] **Step 3: Commit**

```bash
git add CHANGELOG.md
git commit -m "docs: update changelog for the structured exception hierarchy"
```

---

## Self-Review

**Spec coverage:**
- Sealed `Cta4jException`/`Cta4jBusException`/`Cta4jTrainException` hierarchy — Tasks 4-10. ✓
- Per-endpoint `Cta4jArrivalsException`/`Cta4jFollowException`/`Cta4jLocationsException` with `errorCode()`/`rawErrorCode()` — Tasks 7-9. ✓
- `ArrivalsErrorCode`/`FollowErrorCode`/`LocationsErrorCode` enums verified against `train-api.pdf` — Tasks 1-3. ✓
- Raw, undecorated `getMessage()` — built into every exception constructor from Task 5 onward (no template wrapping anywhere). ✓
- Guiding rule (throw the most specific type available) applied to every call site — Tasks 11-19, including the two previously-undiscovered wrinkles (bus interface `findById` default methods in Task 12, and `Qualifiers`'/`ApiUtils.parseErrCd`'s lack of endpoint context in Tasks 13/14/17, resolved by using the qualifier method name as the endpoint string). ✓
- Deletion of `train.common.internal.wire.CtaError` and both `ApiUtils.buildErrorMessage` methods — Tasks 14 and 18. ✓
- `FollowApiImpl.NOT_FOUND_ERROR_CODE`/`ArrivalsApiImpl.INVALID_MAPID_ERROR_CODE`/`INVALID_STPID_ERROR_CODE` removed in favor of enum comparisons — Task 15. ✓
- No `Cta4jStationsException` — `StationsApiImpl` uses `Cta4jTrainException` directly — Task 15, Step 4. ✓
- Changelog correction — Task 20. ✓

**Placeholder scan:** No "TBD"/"TODO"/"similar to Task N" in any step; every step shows complete, concrete code or an exact bash command.

**Type consistency:** `rawErrorCode()`/`errorCode()` names match across the spec, Tasks 7-9's production code, and Task 16's test assertions. `Cta4jBusException`'s three constructors (`List<? extends CtaError>, String`; `String, String, Throwable`; `String, String`) are used consistently across Tasks 11, 12, 13. `ApiUtils.parseErrCd`'s signature (`String errCd, String endpoint`) is unchanged from its pre-existing form, so no caller in Task 15 needs updating beyond the exception type it now throws.

One sequencing note for whoever executes this: **Tasks 4 through 19 must all complete before the module compiles again.** Task 4 removes the old `Cta4jException` constructors project-wide; nothing compiles project-wide until Task 19 retargets the last remaining call site (`HttpClient`). Each individual task's own test target (`-Dtest=<SpecificTest>`) will still pass in isolation during this window, but do not run a bare `mvn test` (no `-Dtest` filter) and expect success until Task 19, Step 3.