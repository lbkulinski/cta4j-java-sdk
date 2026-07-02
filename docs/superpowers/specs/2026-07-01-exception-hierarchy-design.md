# Structured Exception Hierarchy Design

## Motivation

`Cta4jException` currently carries nothing but a free-text `message` and an optional `cause`. Callers who want to
programmatically distinguish "invalid API key" from "rate limited" from "resource not found" have to parse the
message string, which is fragile. This design gives `Cta4jException` structured, type-safe error information while
keeping the "catch one type to get everything the SDK can throw" ergonomic that most well-regarded Java SDKs (e.g.
`java.sql.SQLException` and its subclasses, AWS SDK v2's `SdkException` hierarchy) rely on.

This directly follows the v6.0.0 review, which surfaced (and fixed) three ad hoc, inconsistent not-found detection
mechanisms across the train module (`FollowApiImpl`'s magic `501`, the `103`/`108` codes just added to
`ArrivalsApiImpl`, and no equivalent at all in `LocationsApiImpl`). This design replaces those magic numbers with
real, documented enums.

## Current state

- `Cta4jException` is a single, flat `final` `RuntimeException` subclass with a message and optional cause. (It was
  made `final` earlier in this same v6.0.0 cycle to prevent external subclassing; see Changelog notes below — this
  design supersedes that decision with a `sealed` hierarchy that achieves the same goal while allowing internal
  extension.)
- Bus and train APIs are asymmetric in what CTA actually returns:
  - **Bus** (`com.cta4j.bus.common.internal.wire.CtaError`): only a free-text `msg()`. No numeric error code exists
    in the CTA Bus Tracker API at all. Each feature's typed error record (`CtaStopError`, `CtaDetourError`, etc.)
    additionally carries resource-identifying fields (`rt`, `stpid`, `dir`, etc.) used internally to distinguish
    not-found from fatal, but these are not surfaced publicly today.
  - **Train** (`com.cta4j.train.common.internal.wire.CtaError`): a `{code: int, message: String}` record. CTA's
    Train Tracker API documents real, closed-set numeric error codes per endpoint (see Appendix E of
    `train-api.pdf`), but the codes and their meanings are *not* consistent across endpoints — codes 100-102 mean
    the same thing everywhere, but 103-112/900 only apply to arrivals, 501-503 only to follow, and 106-107 only to
    locations.
- Message formatting is duplicated: `bus.common.internal.util.ApiUtils.buildErrorMessage` and
  `train.common.internal.util.ApiUtils.buildErrorMessage` each independently format an "Error response from X: ..."
  string from data that (after this design) the exception itself will already carry as structured fields.

## Goals

- Let callers catch a specific, typed exception (e.g. `Cta4jFollowException`) and read a structured `errorCode()`
  without parsing message text.
- Preserve the ability to catch `Cta4jException` (or `Cta4jBusException`/`Cta4jTrainException`) to mean "anything
  the SDK threw" / "anything this transit type's calls threw."
- Prevent external consumers from extending the hierarchy themselves (the original motivation for `final`), while
  still allowing this codebase to extend it internally.
- Be faithful to what each CTA API actually returns — no synthesized/heuristic classification for bus, since it has
  no numeric codes to classify.
- Replace the ad hoc magic-number not-found checks introduced this session with real enums.

## Non-goals

- No shared classification enum across bus and train, or across train endpoints (rejected in favor of per-endpoint
  enums — see Decisions below).
- No decorating/rewriting of what CTA actually reported. `getMessage()` returns CTA's raw text verbatim wherever
  possible, rather than a template we invented — see "Message content" below.
- No `Cta4jStationsException` — `StationsApiImpl` has no CTA error-code scheme (it reads from a Socrata JSON feed,
  not the Train Tracker API), so it uses `Cta4jTrainException` directly rather than an empty marker subclass.

## Design

### Class hierarchy

```
Cta4jException (sealed, permits Cta4jBusException, Cta4jTrainException)
├── Cta4jBusException (final)                — endpoint + message/cause only, no code
└── Cta4jTrainException (sealed, permits ...) — directly instantiable (StationsApiImpl, train Qualifiers)
    ├── Cta4jArrivalsException (final)  — rawErrorCode(): Integer, errorCode(): ArrivalsErrorCode
    ├── Cta4jFollowException (final)    — rawErrorCode(): Integer, errorCode(): FollowErrorCode
    └── Cta4jLocationsException (final) — rawErrorCode(): Integer, errorCode(): LocationsErrorCode
```

`sealed` (rather than reverting to a plain open class) is the key decision here: it gives external consumers the
exact same guarantee `final` gave them (they cannot extend any exception in this hierarchy), while still letting
this codebase build the richer internal hierarchy. It also enables exhaustive `switch` pattern matching over the
exception types, consistent with this codebase's existing use of switch expressions (e.g. `TrainDirection.fromCode`).

`Cta4jException` gains an `endpoint()` accessor, populated at every throw site across the entire hierarchy —
including `HttpClient`, which always has either the raw URL or a parsed path available at each of its throw sites.
This means `endpoint()` is a required (non-`@Nullable`), always-populated field for every exception the SDK can
throw, not just the bus/train-specific ones. Its two existing constructors gain an `endpoint` parameter accordingly:

```java
public sealed class Cta4jException extends RuntimeException permits Cta4jBusException, Cta4jTrainException {
    private final String endpoint;

    public Cta4jException(String message, String endpoint) {
        super(message);
        this.endpoint = Objects.requireNonNull(endpoint);
    }

    public Cta4jException(String message, String endpoint, Throwable cause) {
        super(message, cause);
        this.endpoint = Objects.requireNonNull(endpoint);
    }

    public String endpoint() { return this.endpoint; }
}
```

All subclass constructors delegate to one of these two via `super(...)`.

### Message content

`getMessage()` returns CTA's raw reported text unmodified — no `"Error response from %s: ..."` template wrapping
it. Now that `endpoint()`, `rawErrorCode()`, and `errorCode()` (see below) exist as separate structured accessors,
baking that same information into the message string too would be redundant, and it means the SDK is no longer
editing/rephrasing what CTA actually said. This also loses less than it would have under the old flat design:
`Throwable.toString()` already prefixes the message with the exception's class name (e.g.
`Cta4jFollowException: No trains with runnumber 123 were found`), and since each exception class is now
endpoint-specific, the class name alone conveys context our template used to add manually.

The one place raw text still needs *some* combining is `Cta4jBusException`, since a single bus response can carry
multiple error entries. Those are joined with `"; "` and nothing else — no endpoint prefix, no added template.

### Cta4jBusException

Bus has no numeric code, so per the decision made during design, `Cta4jBusException` exposes only what's uniformly
available: `endpoint()` (inherited) and the message/cause. No generic identifying-field map or raw-error-list
accessor — CTA's bus error payload genuinely doesn't have more structure than a message plus per-feature fields that
don't generalize.

It takes over the message-joining responsibility that `bus.common.internal.util.ApiUtils.buildErrorMessage` used to
have:

```java
public final class Cta4jBusException extends Cta4jException {
    public Cta4jBusException(List<? extends CtaError> errors, String endpoint) {
        super(joinMessages(errors), endpoint);
    }

    public Cta4jBusException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);
    }

    private static String joinMessages(List<? extends CtaError> errors) {
        return errors.stream()
                     .map(CtaError::msg)
                     .reduce("%s; %s"::formatted)
                     .orElse("Unknown error");
    }
}
```

### Cta4jTrainException and per-endpoint subclasses

`Cta4jTrainException` is concrete and `sealed`, permitting only the three leaf subclasses. It's used directly (not
through a leaf subclass) by `StationsApiImpl` and by train `Qualifiers`, both of which lack per-endpoint context.

Each leaf subclass (`Cta4jArrivalsException`, `Cta4jFollowException`, `Cta4jLocationsException`) has two
constructors, mirroring the split between a genuine CTA-reported error and a parse/transport failure:

```java
public final class Cta4jFollowException extends Cta4jTrainException {
    private final @Nullable Integer rawErrorCode;
    private final @Nullable FollowErrorCode errorCode;

    public Cta4jFollowException(String message, String endpoint, int rawErrorCode, FollowErrorCode errorCode) {
        super(message, endpoint);
        this.rawErrorCode = rawErrorCode;
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    public Cta4jFollowException(String message, String endpoint, Throwable cause) {
        super(message, endpoint, cause);
        this.rawErrorCode = null;
        this.errorCode = null;
    }

    public @Nullable Integer rawErrorCode() { return this.rawErrorCode; }
    public @Nullable FollowErrorCode errorCode() { return this.errorCode; }
}
```

`errorCode()` is the primary, enum-shaped accessor for pattern-matching. `rawErrorCode()` exists specifically as a
fidelity fallback: since `FollowErrorCode.UNKNOWN` is a fixed singleton, it can't dynamically carry whatever
arbitrary number CTA actually sent — if CTA introduces a new code this SDK doesn't recognize yet, `errorCode()`
correctly reports `UNKNOWN`, but `rawErrorCode()` still preserves the real number so no information is lost. Both
are `null` for the parse/transport-failure constructor, since there is no CTA-reported error to attach in that
case — this is an intentional, documented distinction, not an oversight.

This eliminates the need for `train.common.internal.wire.CtaError` and
`train.common.internal.util.ApiUtils.buildErrorMessage(String, CtaError)` — both are deleted. A representative
call-site simplification:

```java
// before
CtaError error = new CtaError(errCd, followResponse.errNm());
String message = ApiUtils.buildErrorMessage(FOLLOW_ENDPOINT, error);
throw new Cta4jException(message);

// after
throw new Cta4jFollowException(followResponse.errNm(), FOLLOW_ENDPOINT, errCd, errorCode);
```

### Error code enums

Verified against `train-api.pdf` Appendix E (pp. 36-38). Each enum follows this codebase's existing
`TrainDirection`/`CardinalDirection` pattern (`getCode()` accessor, static `fromCode(int)` factory) with one
deviation: `fromCode` returns an `UNKNOWN` constant for unrecognized codes rather than throwing, so a future/
undocumented CTA code never crashes the caller — the `rawErrorCode()` int on the exception is always available as
a fallback regardless.

**`ArrivalsErrorCode`** (`com.cta4j.train.arrival.model`):
`MISSING_PARAMETER(100)`, `INVALID_API_KEY(101)`, `DAILY_LIMIT_EXCEEDED(102)`, `INVALID_MAPID(103)`,
`MAPID_NOT_INTEGER(104)`, `TOO_MANY_MAPIDS(105)`, `INVALID_ROUTE(106)`, `TOO_MANY_ROUTES(107)`,
`INVALID_STPID(108)`, `TOO_MANY_STPIDS(109)`, `INVALID_MAX(110)`, `MAX_NOT_POSITIVE(111)`,
`STPID_NOT_INTEGER(112)`, `INVALID_PARAMETER(500)`, `SERVER_ERROR(900)`, `UNKNOWN(-1)`

**`FollowErrorCode`** (`com.cta4j.train.follow.model`):
`MISSING_PARAMETER(100)`, `INVALID_API_KEY(101)`, `DAILY_LIMIT_EXCEEDED(102)`, `INVALID_PARAMETER(500)`,
`RUN_NOT_FOUND(501)`, `UNABLE_TO_DETERMINE_STOPS(502)`, `UNABLE_TO_FIND_PREDICTIONS(503)`, `UNKNOWN(-1)`

**`LocationsErrorCode`** (`com.cta4j.train.location.model`):
`MISSING_PARAMETER(100)`, `INVALID_API_KEY(101)`, `DAILY_LIMIT_EXCEEDED(102)`, `INVALID_ROUTE(106)`,
`TOO_MANY_ROUTES(107)`, `INVALID_PARAMETER(500)`, `UNKNOWN(-1)`

`106`/`107` (`INVALID_ROUTE`/`TOO_MANY_ROUTES`) intentionally appear in both `ArrivalsErrorCode` and
`LocationsErrorCode` with the same meaning — accepted duplication, the tradeoff of choosing per-endpoint enums over
one shared enum with inapplicable entries.

`FollowApiImpl.NOT_FOUND_ERROR_CODE` (`501`) and `ArrivalsApiImpl.INVALID_MAPID_ERROR_CODE`/
`INVALID_STPID_ERROR_CODE` (`103`/`108`) — the magic-number constants added earlier this session — are removed and
replaced by `FollowErrorCode.RUN_NOT_FOUND`/`ArrivalsErrorCode.INVALID_MAPID`/`ArrivalsErrorCode.INVALID_STPID`
comparisons.

## Guiding rule for call sites

Throw the most specific exception type for which context is available:

| Code location                                             | Exception thrown                   |
|-----------------------------------------------------------|------------------------------------|
| `HttpClient` (shared bus+train infra, no transit context) | `Cta4jException` (root, unchanged) |
| bus `Qualifiers` (shared across bus features)             | `Cta4jBusException`                |
| train `Qualifiers` (shared across train features)         | `Cta4jTrainException`              |
| any bus `*ApiImpl`                                        | `Cta4jBusException`                |
| `ArrivalsApiImpl`                                         | `Cta4jArrivalsException`           |
| `FollowApiImpl`                                           | `Cta4jFollowException`             |
| `LocationsApiImpl`                                        | `Cta4jLocationsException`          |
| `StationsApiImpl` (no code scheme)                        | `Cta4jTrainException`              |

## File inventory

**New:**
- `com.cta4j.bus.exception.Cta4jBusException`
- `com.cta4j.train.exception.Cta4jTrainException` (sealed)
- `com.cta4j.train.exception.Cta4jArrivalsException`
- `com.cta4j.train.exception.Cta4jFollowException`
- `com.cta4j.train.exception.Cta4jLocationsException`
- `com.cta4j.train.arrival.model.ArrivalsErrorCode`
- `com.cta4j.train.follow.model.FollowErrorCode`
- `com.cta4j.train.location.model.LocationsErrorCode`

**Modified:**
- `com.cta4j.exception.Cta4jException` — `sealed` (was `final`), gains `endpoint()`
- Every bus `*ApiImpl` + bus `Qualifiers` — retarget throw sites to `Cta4jBusException`
- Every train `*ApiImpl` + train `Qualifiers` — retarget throw sites to the appropriate tier
- Every public `*Api` interface — `@throws Cta4jException` javadoc updated to the specific type actually thrown
- `CHANGELOG.md` — correct the stale "`Cta4jException` is now `final`..." bullet from finding #8; add bullets for
  the new hierarchy, enums, and removals below

**Deleted:**
- `train.common.internal.wire.CtaError` + `CtaErrorTest.java`
- `train.common.internal.util.ApiUtils.buildErrorMessage(String, CtaError)` + its tests
- `bus.common.internal.util.ApiUtils.buildErrorMessage(String, List<? extends CtaError>)` + its tests
- `FollowApiImpl.NOT_FOUND_ERROR_CODE`, `ArrivalsApiImpl.INVALID_MAPID_ERROR_CODE`/`INVALID_STPID_ERROR_CODE`

## Testing strategy

- New unit tests per enum: `fromCode`/`getCode`, and that an unrecognized code maps to `UNKNOWN` (mirrors
  `TrainDirectionTest`/`CardinalDirectionTest` style).
- New unit tests per exception class: both constructors, `endpoint()`/`rawErrorCode()`/`errorCode()` accessors,
  message content, and that the parse-failure constructor leaves `rawErrorCode()`/`errorCode()` `null`.
- Existing `*ApiImplTest` error-path assertions upgrade from `.isInstanceOf(Cta4jException.class)` to the specific
  subclass, plus a new assertion on `errorCode()` for at least one error case per impl.
- `HttpClientTest` reviewed/updated for the `endpoint` constructor argument.
- Remove `CtaErrorTest.java` (train) and the now-deleted `buildErrorMessage` test cases in both `ApiUtilsTest`
  classes.

## Compatibility notes

This is additive from a compile-time standpoint for existing consumers: `catch (Cta4jException e)` code continues
to compile and behave the same way. The one meaningful behavior change from earlier this session is reverted:
`Cta4jException` is `sealed`, not `final` — the changelog bullet added for finding #8 needs correcting to reflect
that consumers still cannot subclass it (sealed enforces that), but the class is no longer literally `final`.
