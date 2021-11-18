package com.murphy.homeauto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WifiMqttMessage {

//       "Wifi":{
//        "AP":1,
//                "SSId":"CooleyHouse2G",
//                "BSSId":"C4:04:15:42:90:41",
//                "Channel":13,
//                "RSSI":84
//    }

    @JsonProperty("AP")
    private PlugAP ap;

}
