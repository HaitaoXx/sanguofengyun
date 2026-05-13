package com.gxa.domain.dto;

import lombok.Data;

@Data
public class AppCreateDTO {
    private String title;
    private String description;
    private String type;
    private String coverImage;
    private String configJson;
}
