package com.cta4j.external.bus.detour;

import java.util.List;

public record CtaDetour(
    String id,

    String ver,

    String st,

    String desc,

    List<CtaDetoursRouteDirection> rtdirs,

    String startdt,

    String enddt
) {
}
