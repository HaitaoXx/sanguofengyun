package com.gxa.service;

import com.gxa.domain.dto.StatisticsQueryDTO;
import com.gxa.domain.vo.BehaviorAnalysisVO;
import com.gxa.domain.vo.DifficultyAnalysisVO;
import com.gxa.domain.vo.KnowledgeAnalysisVO;
import com.gxa.domain.vo.StatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExportService {
    
    @Autowired
    private StatisticsService statisticsService;
    
    public byte[] exportStatisticsToExcel(StatisticsQueryDTO dto) throws IOException {
        StatisticsVO statistics = statisticsService.getStatistics(dto);
        BehaviorAnalysisVO behavior = statisticsService.getBehaviorAnalysis(dto.getAppId());
        DifficultyAnalysisVO difficulty = statisticsService.getDifficultyAnalysis(dto.getAppId());
        KnowledgeAnalysisVO knowledge = statisticsService.getKnowledgeAnalysis(dto.getAppId());
        
        try (Workbook workbook = new XSSFWorkbook()) {
            createSummarySheet(workbook, statistics, dto);
            createTrendSheet(workbook, statistics.getTrendData());
            createDimensionSheet(workbook, statistics.getDimensionData());
            createBehaviorSheet(workbook, behavior);
            createDifficultySheet(workbook, difficulty);
            createKnowledgeSheet(workbook, knowledge);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    
    private void createSummarySheet(Workbook workbook, StatisticsVO statistics, StatisticsQueryDTO dto) {
        Sheet sheet = workbook.createSheet("统计概览");
        
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("AI智能答题系统 - 统计分析报告");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        
        rowNum++;
        
        Row dateRow = sheet.createRow(rowNum++);
        dateRow.createCell(0).setCellValue("统计时间范围：");
        dateRow.createCell(1).setCellValue(dto.getStartDate() + " 至 " + dto.getEndDate());
        
        Row currentDateRow = sheet.createRow(rowNum++);
        currentDateRow.createCell(0).setCellValue("生成时间：");
        currentDateRow.createCell(1).setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        rowNum++;
        
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("指标");
        headerRow.createCell(1).setCellValue("数值");
        headerRow.getCell(0).setCellStyle(headerStyle);
        headerRow.getCell(1).setCellStyle(headerStyle);
        
        createDataRow(sheet, rowNum++, "浏览次数", String.valueOf(statistics.getViewCount()), dataStyle);
        createDataRow(sheet, rowNum++, "答题次数", String.valueOf(statistics.getAnswerCount()), dataStyle);
        createDataRow(sheet, rowNum++, "分享次数", String.valueOf(statistics.getShareCount()), dataStyle);
        createDataRow(sheet, rowNum++, "平均得分", String.format("%.2f", statistics.getAvgScore()), dataStyle);
        
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
    }
    
    private void createTrendSheet(Workbook workbook, List<Map<String, Object>> trendData) {
        Sheet sheet = workbook.createSheet("趋势分析");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"日期", "浏览次数", "答题次数", "分享次数"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        for (Map<String, Object> data : trendData) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, data.get("date"), dataStyle);
            createCell(row, 1, data.get("viewCount"), dataStyle);
            createCell(row, 2, data.get("answerCount"), dataStyle);
            createCell(row, 3, data.get("shareCount"), dataStyle);
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 4000);
        }
    }
    
    private void createDimensionSheet(Workbook workbook, List<Map<String, Object>> dimensionData) {
        Sheet sheet = workbook.createSheet("题型分析");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"题型", "正确数", "错误数", "正确率(%)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        for (Map<String, Object> data : dimensionData) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, data.get("name"), dataStyle);
            createCell(row, 1, data.get("correct"), dataStyle);
            createCell(row, 2, data.get("wrong"), dataStyle);
            createCell(row, 3, data.get("rate"), dataStyle);
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 4000);
        }
    }
    
    private void createBehaviorSheet(Workbook workbook, BehaviorAnalysisVO behavior) {
        Sheet sheet = workbook.createSheet("用户行为分析");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        Row headerRow1 = sheet.createRow(rowNum++);
        headerRow1.createCell(0).setCellValue("用户统计");
        headerRow1.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        createDataRow(sheet, rowNum++, "总用户数", String.valueOf(behavior.getTotalUsers()), dataStyle);
        createDataRow(sheet, rowNum++, "活跃用户数(30天)", String.valueOf(behavior.getActiveUsers()), dataStyle);
        createDataRow(sheet, rowNum++, "新增用户数(7天)", String.valueOf(behavior.getNewUsers()), dataStyle);
        createDataRow(sheet, rowNum++, "平均答题时长(秒)", String.format("%.2f", behavior.getAvgAnswerTime()), dataStyle);
        createDataRow(sheet, rowNum++, "平均每次答题数", String.format("%.2f", behavior.getAvgQuestionsPerSession()), dataStyle);
        
        rowNum++;
        
        Row headerRow2 = sheet.createRow(rowNum++);
        headerRow2.createCell(0).setCellValue("小时分布");
        headerRow2.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        Row hourHeaderRow = sheet.createRow(rowNum++);
        hourHeaderRow.createCell(0).setCellValue("时段");
        hourHeaderRow.createCell(1).setCellValue("答题次数");
        hourHeaderRow.getCell(0).setCellStyle(headerStyle);
        hourHeaderRow.getCell(1).setCellStyle(headerStyle);
        
        for (BehaviorAnalysisVO.HourlyData data : behavior.getHourlyDistribution()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getHour() + ":00");
            row.createCell(1).setCellValue(data.getAnswerCount());
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        rowNum++;
        
        Row headerRow3 = sheet.createRow(rowNum++);
        headerRow3.createCell(0).setCellValue("星期分布");
        headerRow3.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        Row weekHeaderRow = sheet.createRow(rowNum++);
        weekHeaderRow.createCell(0).setCellValue("星期");
        weekHeaderRow.createCell(1).setCellValue("答题次数");
        weekHeaderRow.getCell(0).setCellStyle(headerStyle);
        weekHeaderRow.getCell(1).setCellStyle(headerStyle);
        
        for (BehaviorAnalysisVO.WeeklyData data : behavior.getWeeklyDistribution()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getDayOfWeek());
            row.createCell(1).setCellValue(data.getAnswerCount());
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
    }
    
    private void createDifficultySheet(Workbook workbook, DifficultyAnalysisVO difficulty) {
        Sheet sheet = workbook.createSheet("难度分析");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        Row headerRow1 = sheet.createRow(rowNum++);
        headerRow1.createCell(0).setCellValue("难度分布");
        headerRow1.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        Row distHeaderRow = sheet.createRow(rowNum++);
        String[] distHeaders = {"难度等级", "题目数量", "平均正确率(%)"};
        for (int i = 0; i < distHeaders.length; i++) {
            Cell cell = distHeaderRow.createCell(i);
            cell.setCellValue(distHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
        
        for (DifficultyAnalysisVO.DifficultyData data : difficulty.getDifficultyDistribution()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getDifficulty());
            row.createCell(1).setCellValue(data.getCount());
            row.createCell(2).setCellValue(data.getAvgAccuracy());
            for (int i = 0; i < 3; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }
        
        rowNum++;
        
        Row headerRow2 = sheet.createRow(rowNum++);
        headerRow2.createCell(0).setCellValue("题目详情");
        headerRow2.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        Row detailHeaderRow = sheet.createRow(rowNum++);
        String[] detailHeaders = {"题目ID", "题目内容", "题型", "难度", "正确率(%)"};
        for (int i = 0; i < detailHeaders.length; i++) {
            Cell cell = detailHeaderRow.createCell(i);
            cell.setCellValue(detailHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
        
        for (DifficultyAnalysisVO.QuestionDifficulty data : difficulty.getQuestionDetails()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getQuestionId());
            row.createCell(1).setCellValue(data.getQuestionText());
            row.createCell(2).setCellValue(data.getQuestionType());
            row.createCell(3).setCellValue(data.getDifficulty());
            row.createCell(4).setCellValue(data.getAccuracyRate());
            for (int i = 0; i < 5; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }
        
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 4000);
    }
    
    private void createKnowledgeSheet(Workbook workbook, KnowledgeAnalysisVO knowledge) {
        Sheet sheet = workbook.createSheet("知识点分析");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        Row headerRow1 = sheet.createRow(rowNum++);
        headerRow1.createCell(0).setCellValue("知识点分布");
        headerRow1.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        Row distHeaderRow = sheet.createRow(rowNum++);
        String[] distHeaders = {"知识点", "题目数", "答题数", "正确数", "正确率(%)"};
        for (int i = 0; i < distHeaders.length; i++) {
            Cell cell = distHeaderRow.createCell(i);
            cell.setCellValue(distHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
        
        for (KnowledgeAnalysisVO.KnowledgeData data : knowledge.getKnowledgeDistribution()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getKnowledge());
            row.createCell(1).setCellValue(data.getQuestionCount());
            row.createCell(2).setCellValue(data.getAnswerCount());
            row.createCell(3).setCellValue(data.getCorrectCount());
            row.createCell(4).setCellValue(data.getAccuracyRate());
            for (int i = 0; i < 5; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }
        
        rowNum++;
        
        Row headerRow2 = sheet.createRow(rowNum++);
        headerRow2.createCell(0).setCellValue("薄弱知识点(正确率<60%)");
        headerRow2.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        Row weakHeaderRow = sheet.createRow(rowNum++);
        weakHeaderRow.createCell(0).setCellValue("知识点");
        weakHeaderRow.createCell(1).setCellValue("正确率(%)");
        weakHeaderRow.createCell(2).setCellValue("题目数");
        weakHeaderRow.getCell(0).setCellStyle(headerStyle);
        weakHeaderRow.getCell(1).setCellStyle(headerStyle);
        weakHeaderRow.getCell(2).setCellStyle(headerStyle);
        
        for (KnowledgeAnalysisVO.WeakPoint data : knowledge.getWeakPoints()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getKnowledge());
            row.createCell(1).setCellValue(data.getAccuracyRate());
            row.createCell(2).setCellValue(data.getQuestionCount());
            for (int i = 0; i < 3; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }
        
        rowNum++;
        
        Row headerRow3 = sheet.createRow(rowNum++);
        headerRow3.createCell(0).setCellValue("优势知识点(正确率>=85%)");
        headerRow3.getCell(0).setCellStyle(createTitleStyle(workbook));
        
        rowNum++;
        
        Row strongHeaderRow = sheet.createRow(rowNum++);
        strongHeaderRow.createCell(0).setCellValue("知识点");
        strongHeaderRow.createCell(1).setCellValue("正确率(%)");
        strongHeaderRow.createCell(2).setCellValue("题目数");
        strongHeaderRow.getCell(0).setCellStyle(headerStyle);
        strongHeaderRow.getCell(1).setCellStyle(headerStyle);
        strongHeaderRow.getCell(2).setCellStyle(headerStyle);
        
        for (KnowledgeAnalysisVO.StrongPoint data : knowledge.getStrongPoints()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getKnowledge());
            row.createCell(1).setCellValue(data.getAccuracyRate());
            row.createCell(2).setCellValue(data.getQuestionCount());
            for (int i = 0; i < 3; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }
        
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 3000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 4000);
    }
    
    private void createDataRow(Sheet sheet, int rowNum, String label, String value, CellStyle style) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(style);
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(style);
    }
    
    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }
    
    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}