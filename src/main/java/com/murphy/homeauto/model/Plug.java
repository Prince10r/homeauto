package com.murphy.homeauto.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Plug {

    @Id
    private Long id;
    private String name;
    private String location;
    private String description;
}
