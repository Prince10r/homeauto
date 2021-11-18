package com.murphy.homeauto.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
// {
//        "TotalStartTime":"2019-05-09T19:54:21",
//        "Total":130.791,
//        "Yesterday":0.000,
//        "Today":0.000,
//        "Period":0,
//        "Power":0,
//        "ApparentPower":0,
//        "ReactivePower":0,
//        "Factor":0.00,
//        "Voltage":234,
//        "Current":0.000
//        }

@Data
public class PlugEnergy {
    @Id
    private Long id;
    private Date sampleTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    @JsonProperty("TotalStartTime")
    Date totalStartTime;
    @JsonProperty("Total")
    Float total;
    @JsonProperty("Yesterday")
    Float yesterday;
    @JsonProperty("Period")
    Integer period;
    @JsonProperty("Power")
    Integer power;
    @JsonProperty("ApparentPower")
    Integer apparentPower;
    @JsonProperty("ReactivePower")
    Integer reactivePower;
    @JsonProperty("Factor")
    Float factor;
    @JsonProperty("Voltage")
    Integer voltage;
    @JsonProperty("Current")
    Float current;

    private Long plug;
}
