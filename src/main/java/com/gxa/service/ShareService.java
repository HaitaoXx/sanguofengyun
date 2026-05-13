package com.gxa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxa.domain.entity.AppShare;
import com.gxa.domain.entity.App;
import com.gxa.domain.vo.ShareVO;
import com.gxa.mapper.AppShareMapper;
import com.gxa.mapper.AppMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class ShareService {
    
    @Autowired
    private AppShareMapper appShareMapper;
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private AppService appService;
    
    @Transactional
    public ShareVO createShare(String appId, Long userId) {
        App app = appMapper.selectByAppId(appId);
        if (app == null) {
            throw new RuntimeException("应用不存在");
        }
        
        String shareCode = generateShareCode();
        String shareUrl = buildShareUrl(shareCode);
        
        AppShare share = new AppShare();
        share.setAppId(appId);
        share.setShareCode(shareCode);
        share.setShareUrl(shareUrl);
        share.setUserId(userId);
        share.setShareCount(0);
        appShareMapper.insert(share);
        
        appService.incrementShareCount(appId);
        
        ShareVO vo = new ShareVO();
        vo.setShareCode(shareCode);
        vo.setShareUrl(shareUrl);
        vo.setQrCode(generateQrCodeUrl(shareUrl));
        vo.setCreateTime(share.getCreateTime());
        
        return vo;
    }
    
    public String getAppIdByShareCode(String shareCode) {
        AppShare share = appShareMapper.selectByShareCode(shareCode);
        if (share == null) {
            throw new RuntimeException("分享链接不存在");
        }
        
        share.setShareCount(share.getShareCount() + 1);
        appShareMapper.updateById(share);
        
        return share.getAppId();
    }
    
    private String generateShareCode() {
        return "SHARE" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
    
    private String buildShareUrl(String shareCode) {
        return "/share/" + shareCode;
    }
    
    private String generateQrCodeUrl(String shareUrl) {
        return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + shareUrl;
    }
}
