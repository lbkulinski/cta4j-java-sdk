package com.cta4j.bus.model;

import java.math.BigDecimal;
import java.util.List;

public record Stop(
    String id,

    String name,

    BigDecimal latitude,

    BigDecimal longitude,

    List<Integer> detoursAdded,

    List<Integer> detoursRemoved,

    Integer gtfsSequence,

    Boolean adaAccessible
) {
}
