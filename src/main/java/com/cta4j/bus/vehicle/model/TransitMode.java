package com.cta4j.bus.vehicle.model;

/**
 * Represents the mode of transit for a vehicle.
 */
public enum TransitMode {
    /**
     * Indicates no specific transit mode.
     */
    NONE(0),

    /**
     * Indicates a bus transit mode.
     */
    BUS(1),

    /**
     * Indicates a ferry transit mode.
     */
    FERRY(2),

    /**
     * Indicates a rail transit mode.
     */
    RAIL(3),

    /**
     * Indicates a people mover transit mode.
     */
    PEOPLE_MOVER(4);

    private final int code;

    TransitMode(int code) {
        this.code = code;
    }

    /**
     * Gets the code associated with this transit mode.
     *
     * @return the transit mode code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns the {@code TransitMode} corresponding to the given code.
     *
     * @param code the transit mode code
     * @return the corresponding {@code TransitMode}
     * @throws IllegalArgumentException if the code does not correspond to any known transit mode
     */
    public static TransitMode fromCode(int code) {
        return switch (code) {
            case 0 -> NONE;
            case 1 -> BUS;
            case 2 -> FERRY;
            case 3 -> RAIL;
            case 4 -> PEOPLE_MOVER;
            default -> {
                String message = String.format("Unknown transit mode code: %d", code);

                throw new IllegalArgumentException(message);
            }
        };
    }
}
