package com.murphy.homeauto.controller;

import com.murphy.homeauto.model.Plug;
import com.murphy.homeauto.model.PlugEnergy;
import com.murphy.homeauto.model.PlugUsageAnalysis;
import com.murphy.homeauto.service.SmartPlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

//@RestController
@RequestMapping("/plug")
public class PlugController {

    SmartPlugService smartPlugService;

    String wired;

    @Autowired
    public PlugController(SmartPlugService smartPlugService){
        this.smartPlugService = smartPlugService;
    }

    @PutMapping("/turn/on")
    public void turnOn(@RequestParam Long plugId){
        smartPlugService.turnOn(plugId);
    }

    @PutMapping("/turn/off")
    public void turnoff(@RequestParam Long plugId) {
        smartPlugService.turnOff(plugId);
    }

    @GetMapping("/state")
    public boolean isOn(@RequestParam  Long id){
        return smartPlugService.isOn(id);
    }

    @GetMapping("/usage/current")
    public List<PlugEnergy> getCurrentUsages(){
        return smartPlugService.getCurrentUsages();
    }

    @GetMapping("/usage/period")
    public PlugUsageAnalysis getUsageAnalysis(@RequestParam Date from, @RequestParam Date to){
        return smartPlugService.getUsageAnalysis(from, to);
    }

    @GetMapping("/all")
    public List<Plug> getAllPlugs(){
        return smartPlugService.getAllPlugs();
    }
}
