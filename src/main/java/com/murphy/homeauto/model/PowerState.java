package com.murphy.homeauto.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum PowerState {
    ON("ON"),
    OFF("OFF");

    public final String label;

    private static final Map<String, PowerState> ENUM_MAP;

    private PowerState(String label) {
        this.label = label;
    }

    static {
        Map<String,PowerState> map = new ConcurrentHashMap<String, PowerState>();
        for (PowerState instance : PowerState.values()) {
            map.put(instance.label.toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    @JsonCreator
    public PowerState fromString(String label){
        return ENUM_MAP.get(label.toLowerCase());
    }

}
