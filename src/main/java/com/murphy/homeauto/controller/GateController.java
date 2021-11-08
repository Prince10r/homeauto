package com.murphy.homeauto.controller;

import com.murphy.homeauto.command.CommandManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate")
public class GateController {

    @Resource
    CommandManager commandManager;

    @PostMapping("/toggle")
    public void toggleGate(){
        commandManager.toggleGate();
    }
}