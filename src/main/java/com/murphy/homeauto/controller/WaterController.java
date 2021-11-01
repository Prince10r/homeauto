package com.murphy.homeauto.controller;

import com.murphy.homeauto.model.WaterSample;
import com.murphy.homeauto.repository.WaterSampleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/water")
public class WaterController {

    final
    WaterSampleRepository waterSampleRepository;

    public WaterController(WaterSampleRepository waterSampleRepository) {
        this.waterSampleRepository = waterSampleRepository;
    }

    @GetMapping("/temperature/latest")
    public List<WaterSample> getLatestWaterTemps() {
        return waterSampleRepository.getLatestWaterTemps();
    }
}
