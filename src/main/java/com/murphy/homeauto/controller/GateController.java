package com.murphy.homeauto.controller;

import com.murphy.homeauto.command.CommandManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/gate")
public class GateController {

    @Autowired
    CommandManager commandManager;

    @PostMapping("/toggle")
    public void toggleGate(){
        commandManager.toggleGate();
    }
}