package com.gxa.controller;

import com.gxa.domain.dto.StatisticsQueryDTO;
import com.gxa.service.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/export")
@CrossOrigin
public class ExportController {
    
    @Autowired
    private ExportService exportService;
    
    @PostMapping("/statistics/excel")
    public ResponseEntity<byte[]> exportStatistics(@RequestBody StatisticsQueryDTO dto) throws IOException {
        byte[] excelData = exportService.exportStatisticsToExcel(dto);
        
        String filename = "统计报告_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", encodedFilename);
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelData);
    }
}