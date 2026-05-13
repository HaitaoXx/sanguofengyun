package com.gxa.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class AppUpdateDTO {
    private String appId;
    private String title;
    private String description;
    private String coverImage;
    private String configJson;
    private List<QuestionDTO> questions;
}
