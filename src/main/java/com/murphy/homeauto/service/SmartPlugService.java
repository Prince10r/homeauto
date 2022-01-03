package com.murphy.homeauto.service;

import com.murphy.homeauto.model.Plug;
import com.murphy.homeauto.model.PlugEnergy;
import com.murphy.homeauto.model.PlugUsageAnalysis;
import com.murphy.homeauto.repository.PlugEnergyRepository;
import com.murphy.homeauto.repository.PlugRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class SmartPlugService {
    @Resource
    PlugRepository plugRepository;

    @Resource
    PlugEnergyRepository plugEnergyRepository;

    public void updatePlugDetails(String topic, String ipAddress, boolean state){
        Plug plug = plugRepository.findByTopic(topic);
        boolean hasChanged = false;
        if(plug.getIpAddress() == null ||
                (ipAddress != null && !plug.getIpAddress().equals(ipAddress))){
            hasChanged = true;
            plug.setIpAddress(ipAddress);
        }
        if(plug.getState() == null || (plug.getState() != state)){
            hasChanged = true;
            plug.setState(state);
        }
        if(hasChanged){
            plugRepository.save(plug);
        }
    }

    public void saveEnergyReading(String topic, PlugEnergy plugEnergy){
        Plug plug = plugRepository.findByTopic(topic);
        plugEnergy.setPlug(plug.getId());
        plugEnergyRepository.save(plugEnergy);
    }

    public void turnOn(Long plugId) {
    }

    public void turnOff(Long plugId) {
    }

    public List<PlugEnergy> getCurrentUsages() {
        return plugEnergyRepository.findLatestPlugEnergies();
    }

    public boolean isOn(Long id) {
        return plugRepository.findById(id).get().getState();
    }

    public PlugUsageAnalysis getUsageAnalysis(Date from, Date to) {
        return null;
    }

    public List<Plug> getAllPlugs() {
        Iterable<Plug> plugs = plugRepository.findAll();
        return StreamSupport.stream(plugs.spliterator(), false)
                .collect(Collectors.toList());
    }
}
