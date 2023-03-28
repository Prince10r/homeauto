package com.murphy.homeauto.controller;

import com.murphy.homeauto.command.CommandManager;
import com.murphy.homeauto.model.LightSwitch;
import com.murphy.homeauto.repository.LightSwitchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/light")
public class LightController {

    @Autowired
    LightSwitchRepository lightSwitchRepository;

    @Autowired
    CommandManager commandManager;

    @GetMapping
    private Iterable<LightSwitch> getAllLightSwitches(){
        return lightSwitchRepository.findAll();
    }

    @PutMapping("/turnOff")
    public void turnOff(@RequestParam("id")Long id){
        LightSwitch lightSwitch = lightSwitchRepository.findById(id).get();
        commandManager.send433Code(lightSwitch.getOffCode());
    }

    @PutMapping("/turnOn")
    public void turnOn(@RequestParam("id")String id){
        LightSwitch lightSwitch = lightSwitchRepository.findById(Long.parseLong(id)).get();
        commandManager.send433Code(lightSwitch.getOnCode());
    }

}
