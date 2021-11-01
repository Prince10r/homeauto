package com.murphy.homeauto.controller;

import com.murphy.homeauto.model.FrontDoorImage;
import com.murphy.homeauto.model.FrontDoorImageFile;
import com.murphy.homeauto.service.FrontDoorCameraService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("frontdoor")
public class FrontDoorController {

    final FrontDoorCameraService frontDoorFileService;

    public FrontDoorController(FrontDoorCameraService frontDoorFileService) {
        this.frontDoorFileService = frontDoorFileService;
    }

    @GetMapping("/todayImages")
    public List<FrontDoorImage> getTodayImages() {
        return frontDoorFileService.getFrontDoorImagesToday();
    }

    @GetMapping("/yesterdayImages")
    public List<FrontDoorImage> getYesterdayImages() {
        return frontDoorFileService.getFrontDoorImagesYesterday();
    }

    @GetMapping("/images")
    public List<FrontDoorImage> getImages(@RequestParam("date") String date) {
        return frontDoorFileService.getFrontDoorImagesByDate(date);
    }


    @GetMapping("/gifs")
    public List<FrontDoorImageFile> getGifs(@RequestParam("date") String date) {
        return frontDoorFileService.getFrontDoorGifsByDate(date);
    }

    @GetMapping("/webp")
    public List<FrontDoorImageFile> getWebPs(@RequestParam("date") String date) {
        return frontDoorFileService.getFrontDoorWebPsByDate(date);
    }
}
