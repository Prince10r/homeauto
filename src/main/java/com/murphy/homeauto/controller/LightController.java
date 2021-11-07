package com.murphy.homeauto.controller;

import com.murphy.homeauto.model.LightSwitch;
import com.murphy.homeauto.repository.LightSwitchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("light")
public class LightController {

    @Resource
    LightSwitchRepository lightSwitchRepository;

    @GetMapping
    private Iterable<LightSwitch> getAllLightSwitches(){
        return lightSwitchRepository.findAll();
    }

}
