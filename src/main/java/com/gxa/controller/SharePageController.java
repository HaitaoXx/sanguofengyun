package com.gxa.controller;

import com.gxa.domain.entity.AppShare;
import com.gxa.mapper.AppShareMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Controller
public class SharePageController {
    
    @Autowired
    private AppShareMapper appShareMapper;
    
    @GetMapping("/share/{shareCode}")
    public RedirectView sharePage(@PathVariable String shareCode) {
        AppShare share = appShareMapper.selectByShareCode(shareCode);
        if (share == null) {
            log.error("分享链接不存在: {}", shareCode);
            return new RedirectView("/");
        }
        
        share.setShareCount(share.getShareCount() + 1);
        appShareMapper.updateById(share);
        
        return new RedirectView("/?share=" + shareCode);
    }
}
