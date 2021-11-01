package com.murphy.homeauto.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class WaterSample {
    @Id
    private long id;

    private float temperature;
    private WaterSensorLocation location;
    private Date date;
}
