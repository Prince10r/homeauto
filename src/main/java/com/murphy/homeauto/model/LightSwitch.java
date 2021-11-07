package com.murphy.homeauto.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class LightSwitch {
    @Id
    private long id;

    private String roomName;
    private long floor;
    private long onCode;
    private long offCode;
    private long toggleCode;


}
