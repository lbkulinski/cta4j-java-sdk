package com.cta4j.bus.api.detour.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDetour(
    String id,

    int ver,

    int st,

    String desc,

    List<CtaDetoursRouteDirection> rtdirs,

    String startdt,

    String enddt,

    @Nullable
    String rtpidatafeed
) {
    public CtaDetour {
        Objects.requireNonNull(id);
        Objects.requireNonNull(desc);
        Objects.requireNonNull(rtdirs);
        Objects.requireNonNull(startdt);
        Objects.requireNonNull(enddt);

        rtdirs.forEach(Objects::requireNonNull);
    }
}
