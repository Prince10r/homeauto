package com.murphy.homeauto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlugAP {
    @JsonProperty("SSId")
    private String ssid;
    @JsonProperty("BSSId")
    private String bssid;
    @JsonProperty("Channel")
    private Integer channel;
    @JsonProperty("RSSI")
    private Integer rssi;
}
