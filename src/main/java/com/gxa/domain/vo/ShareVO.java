package com.gxa.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShareVO {
    private String shareCode;
    private String shareUrl;
    private String qrCode;
    private LocalDateTime createTime;
}
