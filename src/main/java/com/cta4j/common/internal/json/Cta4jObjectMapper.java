package com.cta4j.common.internal.json;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.ObjectMapper;

@NullMarked
@ApiStatus.Internal
public final class Cta4jObjectMapper {
    private static final ObjectMapper INSTANCE = new ObjectMapper();

    private Cta4jObjectMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ObjectMapper instance() {
        return INSTANCE;
    }
}
