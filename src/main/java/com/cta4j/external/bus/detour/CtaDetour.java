package com.cta4j.external.bus.detour;

import java.util.List;

public record CtaDetour(
    String id,

    int ver,

    int st,

    String desc,

    List<CtaDetoursRouteDirection> rtdirs,

    String startdt,

    String enddt
) {
}
