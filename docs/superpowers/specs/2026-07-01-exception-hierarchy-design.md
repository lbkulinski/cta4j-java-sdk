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
  design reconsiders that decision. `final` cannot coexist with the subclass hierarchy this design needs, so
  `Cta4jException` reverts to a plain, non-`final` class. See "Why not `sealed`" below for why this is an acceptable
  tradeoff rather than a compromise.)
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

- Let callers catch a specific, typed exception (e.g. `Cta4jFollowException`) and read a structured `getErrorCode()`
  without parsing message text.
- Preserve the ability to catch `Cta4jException` (or `Cta4jBusException`/`Cta4jTrainException`) to mean "anything
  the SDK threw" / "anything this transit type's calls threw."
- Allow this codebase to build a rich internal exception hierarchy without fighting Java's sealed-class rules (see
  "Why not `sealed`" below). External subclassing is left open, same as `java.sql.SQLException`.
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
Cta4jException (plain, non-final)
├── Cta4jBusException (final)          — endpoint + message/cause only, no code
└── Cta4jTrainException (non-final)    — directly instantiable (StationsApiImpl, train Qualifiers)
    ├── Cta4jArrivalsException (final)  — getRawErrorCode(): Integer, getErrorCode(): ArrivalsErrorCode
    ├── Cta4jFollowException (final)    — getRawErrorCode(): Integer, getErrorCode(): FollowErrorCode
    └── Cta4jLocationsException (final) — getRawErrorCode(): Integer, getErrorCode(): LocationsErrorCode
```

**Why not `sealed`:** `sealed` was the original plan, since it would give external consumers the same
"cannot-extend-this" guarantee `final` gave them, while still letting this codebase build the richer internal
hierarchy. It doesn't work here, though: Java requires a sealed class's permitted subclasses to either live in the
same package as the sealed class, or share a proper JPMS module (`module-info.java`) with it. This project has
neither — `Cta4jBusException`/`Cta4jTrainException`/the three leaf classes are deliberately spread across
feature-specific packages (see File inventory), and introducing `module-info.java` just to satisfy `sealed` would be
a much bigger, unrelated change to the whole build. Given the new `getErrorCode()`/`getRawErrorCode()`/`getEndpoint()`
accessors already remove the main practical reason a consumer would want to subclass one of these exceptions (to
attach custom classification), the closed-hierarchy guarantee `sealed` would add is a marginal benefit not worth
either constraint — so this hierarchy stays open, same as `java.sql.SQLException`.

`Cta4jException` gains an `getEndpoint()` accessor, populated at every throw site across the entire hierarchy —
including `HttpClient`, which always has either the raw URL or a parsed path available at each of its throw sites.
This means `getEndpoint()` is a required (non-`@Nullable`), always-populated field for every exception the SDK can
throw, not just the bus/train-specific ones. Its two existing constructors gain an `endpoint` parameter accordingly:

```java
public class Cta4jException extends RuntimeException {
    private final String endpoint;

    public Cta4jException(String message, String endpoint) {
        super(message);
        this.endpoint = Objects.requireNonNull(endpoint);
    }

    public Cta4jException(String message, String endpoint, Throwable cause) {
        super(message, cause);
        this.endpoint = Objects.requireNonNull(endpoint);
    }

    public String getEndpoint() { return this.endpoint; }
}
```

All subclass constructors delegate to one of these two via `super(...)`.

### Message content

`getMessage()` returns CTA's raw reported text unmodified — no `"Error response from %s: ..."` template wrapping
it. Now that `getEndpoint()`, `getRawErrorCode()`, and `getErrorCode()` (see below) exist as separate structured accessors,
baking that same information into the message string too would be redundant, and it means the SDK is no longer
editing/rephrasing what CTA actually said. This also loses less than it would have under the old flat design:
`Throwable.toString()` already prefixes the message with the exception's class name (e.g.
`Cta4jFollowException: Invalid API key`), and since each exception class is now endpoint-specific, the class name
alone conveys context our template used to add manually.

The one place raw text still needs *some* combining is `Cta4jBusException`, since a single bus response can carry
multiple error entries. Those are joined with `"; "` and nothing else — no endpoint prefix, no added template.

### Cta4jBusException

Bus has no numeric code, so per the decision made during design, `Cta4jBusException` exposes only what's uniformly
available: `getEndpoint()` (inherited) and the message/cause. No generic identifying-field map or raw-error-list
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

`Cta4jTrainException` is concrete and non-`final` (not `sealed` — see "Why not `sealed`" above), extended by the
three leaf subclasses below. It's also used directly (not through a leaf subclass) by `StationsApiImpl` and by
train `Qualifiers`, both of which lack per-endpoint context.

`getRawErrorCode()` lives on `Cta4jTrainException` itself (see above) rather than being redeclared on each leaf —
its type (`@Nullable Integer`) is identical across all three leaves, so only the enum-typed `getErrorCode()` needs
to be leaf-specific. Each leaf subclass has two constructors: a parse/transport failure, and a genuine CTA-reported
error, which takes the raw `int` from `ApiUtils.parseErrCd` and derives its own `getErrorCode()` internally via
`fromCode(...)`. Both delegate the endpoint to `TrainApiConstants` (see below) instead of taking it as a parameter —
a `Cta4jFollowException` can only ever come from the follow endpoint, so requiring the caller to pass that same
fixed value every time would just invite a copy-paste bug (e.g. passing the arrivals endpoint into a follow
exception by mistake):

```java
public final class Cta4jFollowException extends Cta4jTrainException {
    private final @Nullable FollowErrorCode errorCode;

    public Cta4jFollowException(String message, Throwable cause) {
        super(message, TrainApiConstants.FOLLOW_ENDPOINT, cause);
        this.errorCode = null;
    }

    public Cta4jFollowException(String message, int rawErrorCode) {
        super(message, TrainApiConstants.FOLLOW_ENDPOINT, rawErrorCode);
        this.errorCode = FollowErrorCode.fromCode(rawErrorCode);
    }

    public @Nullable FollowErrorCode getErrorCode() { return this.errorCode; }
}
```

There's deliberately no `(String message, FollowErrorCode errorCode)` constructor taking the enum directly. It
looks like a reasonable convenience overload, but it has a real data-loss trap: `FollowErrorCode.UNKNOWN` maps to
the sentinel `-1`, not the actual code CTA sent, so an enum-first constructor would compute `errorCode.getCode()`
and store `-1` as `rawErrorCode` whenever `errorCode` is `UNKNOWN` — silently discarding the real, unrecognized
code and defeating the entire reason `getRawErrorCode()` exists. Since both real call sites (`ArrivalsApiImpl`/
`FollowApiImpl`/`LocationsApiImpl`) always start from the raw `int` anyway, and nothing else in this SDK constructs
these exceptions from an already-resolved enum, the overload has no legitimate caller and only exists as a footgun
— so it's omitted rather than kept and documented around.

`getErrorCode()` is the primary, enum-shaped accessor for pattern-matching. `getRawErrorCode()` (inherited) exists
specifically as a fidelity fallback: since `FollowErrorCode.UNKNOWN` is a fixed singleton, it can't dynamically
carry whatever arbitrary number CTA actually sent — if CTA introduces a new code this SDK doesn't recognize yet,
`getErrorCode()` correctly reports `UNKNOWN`, but `getRawErrorCode()` still preserves the real number so no
information is lost. Both are `null` for the parse/transport-failure constructor, since there is no CTA-reported
error to attach in that case — this is an intentional, documented distinction, not an oversight.

This eliminates the need for `train.common.internal.wire.CtaError` and
`train.common.internal.util.ApiUtils.buildErrorMessage(String, CtaError)` — both are deleted. A representative
call-site simplification:

```java
// before
CtaError error = new CtaError(errCd, followResponse.errNm());
String message = ApiUtils.buildErrorMessage(FOLLOW_ENDPOINT, error);
throw new Cta4jException(message);

// after
throw new Cta4jFollowException(followResponse.errNm(), errCd);
```

### TrainApiConstants

`ArrivalsApiImpl`, `FollowApiImpl`, and `LocationsApiImpl` each already had a private `*_ENDPOINT` constant for
building request URLs; the new leaf exception classes need the exact same value. Rather than have the exception
class own it (which the impl would then import from) or duplicate the literal in both places, both sides reference
a new shared, public constants holder:

```java
package com.cta4j.train.common;

public final class TrainApiConstants {
    public static final String SCHEME = "https";
    public static final String DEFAULT_HOST = "lapi.transitchicago.com";
    public static final String DEFAULT_STATIONS_URL = "https://data.cityofchicago.org/resource/8pix-ypme.json";

    private static final String API_PREFIX = "/api/1.0";

    public static final String ARRIVALS_ENDPOINT = "%s/ttarrivals.aspx".formatted(API_PREFIX);
    public static final String FOLLOW_ENDPOINT = "%s/ttfollow.aspx".formatted(API_PREFIX);
    public static final String POSITIONS_ENDPOINT = "%s/ttpositions.aspx".formatted(API_PREFIX);

    private TrainApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
```

This absorbs `SCHEME`/`DEFAULT_HOST`/`DEFAULT_STATIONS_URL`/`API_PREFIX`, which previously lived in
`train.common.internal.util.ApiUtils` alongside the unrelated `parseErrCd` method. Moving them out leaves
`ApiUtils` purely behavioral (`parseErrCd` only) and `TrainApiConstants` purely literal values — every constant
related to the CTA Train Tracker API (both the "which server" values `TrainApiImpl.BuilderImpl` uses as defaults,
and the "which path" values each `*ApiImpl` and leaf exception uses) in one place. `TrainApiConstants` is public
(unlike `ApiUtils`, which is `@ApiStatus.Internal`) since the public leaf exception classes need to reference it
directly.

Only the three leaf exceptions bake in a fixed endpoint this way. `Cta4jTrainException` itself keeps taking
`endpoint` as a real constructor parameter, since neither of its direct users has one fixed value: `StationsApiImpl`
uses a runtime-configurable `stationsUrl()` (not a constant), and `Qualifiers` uses a different literal per method
(see the "Qualifiers" call sites in the Guiding rule table below).

### Error code enums

Verified against `train-api.pdf` Appendix E (pp. 36-38). Each enum follows this codebase's existing
`TrainDirection`/`CardinalDirection` pattern (`getCode()` accessor, static `fromCode(int)` factory) with one
deviation: `fromCode` returns an `UNKNOWN` constant for unrecognized codes rather than throwing, so a future/
undocumented CTA code never crashes the caller — the `getRawErrorCode()` int on the exception is always available as
a fallback regardless. Each also includes an `OK(0)` constant not present in the original design: `fromCode` gets
called on the success path too (before the `errCd != 0` check), so representing `0` explicitly is more accurate
than letting it fall into `UNKNOWN`.

**`ArrivalsErrorCode`** (`com.cta4j.train.arrival.exception`):
`OK(0)`, `MISSING_PARAMETER(100)`, `INVALID_API_KEY(101)`, `DAILY_LIMIT_EXCEEDED(102)`, `INVALID_MAPID(103)`,
`MAPID_NOT_INTEGER(104)`, `TOO_MANY_MAPIDS(105)`, `INVALID_ROUTE(106)`, `TOO_MANY_ROUTES(107)`,
`INVALID_STPID(108)`, `TOO_MANY_STPIDS(109)`, `INVALID_MAX(110)`, `MAX_NOT_POSITIVE(111)`,
`STPID_NOT_INTEGER(112)`, `INVALID_PARAMETER(500)`, `SERVER_ERROR(900)`, `UNKNOWN(-1)`

**`FollowErrorCode`** (`com.cta4j.train.follow.exception`):
`OK(0)`, `MISSING_PARAMETER(100)`, `INVALID_API_KEY(101)`, `DAILY_LIMIT_EXCEEDED(102)`, `INVALID_PARAMETER(500)`,
`RUN_NOT_FOUND(501)`, `UNABLE_TO_DETERMINE_STOPS(502)`, `UNABLE_TO_FIND_PREDICTIONS(503)`, `UNKNOWN(-1)`

**`LocationsErrorCode`** (`com.cta4j.train.location.exception`):
`OK(0)`, `MISSING_PARAMETER(100)`, `INVALID_API_KEY(101)`, `DAILY_LIMIT_EXCEEDED(102)`, `INVALID_ROUTE(106)`,
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

Package placement follows CLAUDE.md's existing convention: only the `*Api` interface sits bare at a feature's
package root (it's the feature's single primary entry point); every other supporting public type — domain models,
query objects — gets its own categorized subpackage (`model/Route.java`, `query/StopsPredictionsQuery.java`).
Exception types are supporting types in that same sense, not entry points, so each gets an `exception/` subpackage
too. `Cta4jArrivalsException`, `Cta4jFollowException`, and `Cta4jLocationsException` are exactly as feature-specific
as `ArrivalsApi`/`FollowApi`/`LocationsApi`, so each lives under its feature's own `exception/` subpackage.
`Cta4jTrainException` and `Cta4jBusException` are shared across every feature of their transit type, so each lives
under `train.common.exception`/`bus.common.exception`, alongside where `TrainApiConfig`/`BusApiConfig` live in
`train.common`/`bus.common`. The three error-code enums (`ArrivalsErrorCode`/`FollowErrorCode`/`LocationsErrorCode`)
are colocated with their respective exception in the same `exception/` subpackage, not in `model/` — each enum
exists solely to be that exception's `getErrorCode()` return type, not as domain data describing an arrival/train/etc.,
so it belongs with the exception rather than with `Arrival`/`FollowTrain`/`TrainLocations`.

**New:**
- `com.cta4j.bus.common.exception.Cta4jBusException` (shared across every bus feature)
- `com.cta4j.train.common.exception.Cta4jTrainException` (shared across every train feature)
- `com.cta4j.train.common.internal.util.TrainApiConstants` — absorbs `SCHEME`/`DEFAULT_HOST`/`DEFAULT_STATIONS_URL`/`API_PREFIX`
  from `ApiUtils`, plus the new `ARRIVALS_ENDPOINT`/`FOLLOW_ENDPOINT`/`POSITIONS_ENDPOINT` constants (see
  "TrainApiConstants" above)
- `com.cta4j.train.arrival.exception.Cta4jArrivalsException` (feature-specific)
- `com.cta4j.train.follow.exception.Cta4jFollowException` (feature-specific)
- `com.cta4j.train.location.exception.Cta4jLocationsException` (feature-specific)
- `com.cta4j.train.arrival.exception.ArrivalsErrorCode` (colocated with `Cta4jArrivalsException` — it exists solely
  to be that exception's `getErrorCode()` return type, not as domain data, so it lives beside the exception rather
  than in `model/`)
- `com.cta4j.train.follow.exception.FollowErrorCode` (same reasoning)
- `com.cta4j.train.location.exception.LocationsErrorCode` (same reasoning)

**Modified:**
- `com.cta4j.common.exception.Cta4jException` — non-`final` (was `final`), gains `getEndpoint()`
- `train.common.internal.util.ApiUtils` — loses `SCHEME`/`DEFAULT_HOST`/`DEFAULT_STATIONS_URL`/`API_PREFIX` to
  `TrainApiConstants`; keeps only `parseErrCd`
- `TrainApiImpl.BuilderImpl` — switches from `ApiUtils.SCHEME`/`DEFAULT_HOST`/`DEFAULT_STATIONS_URL` to the
  `TrainApiConstants` equivalents
- Every bus `*ApiImpl` + bus `Qualifiers` — retarget throw sites to `Cta4jBusException`
- `ArrivalsApiImpl`/`FollowApiImpl`/`LocationsApiImpl` — drop their private `*_ENDPOINT` constants in favor of
  `TrainApiConstants.ARRIVALS_ENDPOINT`/`FOLLOW_ENDPOINT`/`POSITIONS_ENDPOINT`; retarget throw sites to their
  respective leaf exception
- `StationsApiImpl` + train `Qualifiers` — retarget throw sites to `Cta4jTrainException` directly (still pass
  their own `endpoint` value — see "TrainApiConstants" above for why they can't use the same fixed-constant
  shortcut)
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
- New unit tests per exception class: both constructors, `getEndpoint()`/`getRawErrorCode()`/`getErrorCode()` accessors,
  message content, and that the parse-failure constructor leaves `getRawErrorCode()`/`getErrorCode()` `null`.
- Existing `*ApiImplTest` error-path assertions upgrade from `.isInstanceOf(Cta4jException.class)` to the specific
  subclass, plus a new assertion on `getErrorCode()` for at least one error case per impl.
- `HttpClientTest` reviewed/updated for the `endpoint` constructor argument.
- Remove `CtaErrorTest.java` (train) and the now-deleted `buildErrorMessage` test cases in both `ApiUtilsTest`
  classes.
- `TrainApiImplTest` assertions that reference `ApiUtils.DEFAULT_HOST`/`DEFAULT_STATIONS_URL` update to
  `TrainApiConstants` equivalents. No dedicated `TrainApiConstantsTest` is needed beyond that — it's a pure
  constants holder with a private constructor, already exercised indirectly through the impls that use it.

## Compatibility notes

This is additive from a compile-time standpoint for existing consumers: `catch (Cta4jException e)` code continues
to compile and behave the same way. The one meaningful behavior change from earlier this session is fully reverted:
`Cta4jException` goes back to being a plain non-`final` class, exactly as it was before finding #8's `final` change.
The changelog bullet added for finding #8 ("`Cta4jException` is now `final` and can no longer be subclassed.") needs
to be removed/corrected — that decision no longer holds, for the reasons in "Why not `sealed`" above. Consumers can
once again subclass `Cta4jException` directly if they choose to, though the new `getErrorCode()`/`getRawErrorCode()`/
`getEndpoint()` accessors should make that unnecessary for the classification use case that motivated `final` in the
first place.
