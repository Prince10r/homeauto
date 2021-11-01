package com.murphy.homeauto.model;

public enum WaterSensorLocation {
    COLD_WATER_TANK(1), HOT_WATER_TANK_TOP(2), HOT_WATER_TANK_MIDDLE(3), HOT_WATER_TANK_BOTTOM(4);

    private final int location;

    WaterSensorLocation(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }

    public static WaterSensorLocation fromLocation(int location) {
        switch (location) {
            case 1:
                return WaterSensorLocation.COLD_WATER_TANK;

            case 2:
                return WaterSensorLocation.HOT_WATER_TANK_TOP;

            case 3:
                return WaterSensorLocation.HOT_WATER_TANK_MIDDLE;

            case 4:
                return WaterSensorLocation.HOT_WATER_TANK_BOTTOM;

            default:
                throw new IllegalArgumentException("Location [" + location
                        + "] not supported.");
        }
    }
}
