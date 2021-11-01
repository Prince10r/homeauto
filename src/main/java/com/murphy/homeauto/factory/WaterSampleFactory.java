package com.murphy.homeauto.factory;

import com.murphy.homeauto.model.WaterSample;
import com.murphy.homeauto.model.WaterSensorLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WaterSampleFactory {

    public List<WaterSample> fromCommandResultString(String waterSampleCommandString) {

        String[] waterSamplesArr = waterSampleCommandString.split("[\\r\\n]");
        List<WaterSample> waterSamples = new ArrayList<>();
        try {
            Date waterSampleDate = new Date();
            waterSamples.add(
                    getSingleWaterSample(waterSamplesArr[0], waterSampleDate, WaterSensorLocation.HOT_WATER_TANK_TOP));
            waterSamples.add(getSingleWaterSample(waterSamplesArr[1], waterSampleDate,
                    WaterSensorLocation.HOT_WATER_TANK_BOTTOM));
            waterSamples.add(
                    getSingleWaterSample(waterSamplesArr[2], waterSampleDate, WaterSensorLocation.COLD_WATER_TANK));
            waterSamples.add(getSingleWaterSample(waterSamplesArr[3], waterSampleDate,
                    WaterSensorLocation.HOT_WATER_TANK_MIDDLE));
        } catch (Exception e) {
            log.error("Failed to convert water command result string: " + waterSampleCommandString, e);
        }

        return waterSamples;
    }

    private static WaterSample getSingleWaterSample(String waterSampleString, Date date, WaterSensorLocation location) {
        WaterSample waterSample = new WaterSample();
        String[] parts = waterSampleString.split(",");
        float tempCelsius = Float.parseFloat(parts[0].replaceAll("[(]", ""));
        waterSample.setDate(date);
        waterSample.setLocation(location);
        waterSample.setTemperature(tempCelsius);
        return waterSample;
    }
}

