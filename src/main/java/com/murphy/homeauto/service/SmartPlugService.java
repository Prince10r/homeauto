package com.murphy.homeauto.service;

import com.murphy.homeauto.model.Plug;
import com.murphy.homeauto.model.PlugEnergy;
import com.murphy.homeauto.repository.PlugEnergyRepository;
import com.murphy.homeauto.repository.PlugRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class SmartPlugService {
    @Resource
    PlugRepository plugRepository;

    @Resource
    PlugEnergyRepository plugEnergyRepository;

    public void saveEnergyReading(String plugName, PlugEnergy plugEnergy){
        Plug plug = plugRepository.findByName(plugName);
        plugEnergy.setPlug(plug.getId());
        plugEnergyRepository.save(plugEnergy);
    }
}
