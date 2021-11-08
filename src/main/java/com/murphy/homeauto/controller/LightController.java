package com.murphy.homeauto.controller;

import com.murphy.homeauto.command.CommandManager;
import com.murphy.homeauto.model.LightSwitch;
import com.murphy.homeauto.repository.LightSwitchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("light")
public class LightController {

    @Resource
    LightSwitchRepository lightSwitchRepository;

    @Resource
    CommandManager commandManager;

    @GetMapping
    private Iterable<LightSwitch> getAllLightSwitches(){
        return lightSwitchRepository.findAll();
    }

    @GetMapping("/turnOff")
    public void turnOff(@RequestParam("id")Long id){
        LightSwitch lightSwitch = lightSwitchRepository.findById(id).get();
        commandManager.send433Code(lightSwitch.getOffCode());
    }

    @GetMapping("/turnOn")
    public void turnOn(@RequestParam("id")Long id){
        LightSwitch lightSwitch = lightSwitchRepository.findById(id).get();
        commandManager.send433Code(lightSwitch.getOnCode());
    }

}
