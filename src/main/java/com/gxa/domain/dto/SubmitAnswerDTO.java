package com.gxa.domain.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SubmitAnswerDTO {
    private String appId;
    private List<AnswerItemDTO> answers;
    private Integer duration;

    @Data
    public static class AnswerItemDTO {
        private Long questionId;
        private String userAnswer;
    }
}
