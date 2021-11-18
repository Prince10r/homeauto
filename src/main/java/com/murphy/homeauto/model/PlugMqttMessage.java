package com.murphy.homeauto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PlugMqttMessage {
//   "Time":"2021-11-18T16:41:25",
//           "Uptime":"1T21:28:39",
//           "Vcc":3.451,
//           "SleepMode":"Dynamic",
//           "Sleep":50,
//           "LoadAvg":19,
//           "POWER":"ON",
//           "Wifi":{}
//           "ENERGY":{}

       @JsonProperty("Time")
       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
       private Date time;
       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd'T'hh:mm:ss")
       @JsonProperty("Uptime")
       private Date uptime;
       @JsonProperty("Vcc")
       private Float vcc;
       @JsonProperty("SleepMode")
       private String sleepMode;
       @JsonProperty("Sleep")
       private int sleep;
       @JsonProperty("LoadAvg")
       private int loadAvg;
       @JsonProperty("POWER")
       private PowerState powerState;
       private WifiMqttMessage wifi;
       @JsonProperty("ENERGY")
       private PlugEnergy plugEnergy;

}
