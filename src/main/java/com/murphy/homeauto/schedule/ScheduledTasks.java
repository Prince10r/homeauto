package com.murphy.homeauto.schedule;

import com.murphy.homeauto.command.CommandManager;
import com.murphy.homeauto.factory.WaterSampleFactory;
import com.murphy.homeauto.model.WaterSample;
import com.murphy.homeauto.repository.WaterSampleRepository;
import com.murphy.homeauto.service.FrontDoorImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    CommandManager commandManager;
    @Autowired
    WaterSampleFactory waterSampleFactory;
    @Autowired
    WaterSampleRepository waterSampleRepository;
    @Autowired
    FrontDoorImageService frontDoorImageService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final long WATER_SAMPLE_SCHEDULE = 5 * 60 * 1000;
    private static final long ANIMATABLE_IMAGE_SCHEDULE = 6 * 60 * 1000;
    private static final long STARTUP_DELAY = 20 * 1000;

    @Scheduled(initialDelay = STARTUP_DELAY, fixedRate = WATER_SAMPLE_SCHEDULE)
    public void readWaterTemperature() {
        String result = commandManager.readWaterSample();
        List<WaterSample> waterSamples = waterSampleFactory.fromCommandResultString(result);
        waterSampleRepository.saveAll(waterSamples);
    }

    @Scheduled(initialDelay = STARTUP_DELAY, fixedRate = ANIMATABLE_IMAGE_SCHEDULE)
    public void generateAnimatableFrontDoorImages() {
        frontDoorImageService.createAnimatableImages();
    }
}
