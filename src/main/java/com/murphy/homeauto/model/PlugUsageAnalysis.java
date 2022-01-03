package com.murphy.homeauto.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlugUsageAnalysis {
    private Date fromDate;
    private Date toDate;
    private Float totalPowerConsumption;
    private Float peakPowerConsumption;
    private Float peakHourlyPowerConsumption;
    private Float averageHourlyConsumption;
    private Float averageDailyConsumption;
    private Float averageMinuteConsumption;
    private Float averageWeeklyConsumption;
    private Float averageMonthlyConsumption;

    private Integer peakVoltage;
    private Integer minVoltage;
    private Integer averageVoltage;

    private List<PlugEnergy> dataPoints;

}
