<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.zwj.service.ClubService" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<html>
<head>
    <title>调试页面</title>
</head>
<body>
    <h2>调试信息</h2>
    
    <h3>1. Spring Context 检查</h3>
    <%
        try {
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
            if (context != null) {
                out.println("✅ Spring Context 已加载<br>");
                
                ClubService clubService = (ClubService) context.getBean("clubServiceImpl");
                if (clubService != null) {
                    out.println("✅ ClubService 已注入<br>");
                    
                    try {
                        java.util.List clubs = clubService.findAll();
                        out.println("✅ clubService.findAll() 调用成功<br>");
                        out.println("📊 查询到 " + clubs.size() + " 条社团记录<br>");
                        
                        if (clubs.isEmpty()) {
                            out.println("⚠️ 数据库中没有社团数据，尝试插入测试数据<br>");
                            try {
                                com.zwj.entity.Club testClub = new com.zwj.entity.Club();
                                testClub.setName("测试社团");
                                testClub.setIntro("这是一个测试社团");
                                clubService.save(testClub);
                                out.println("✅ 测试数据插入成功<br>");
                                
                                clubs = clubService.findAll();
                                out.println("📊 插入后查询到 " + clubs.size() + " 条记录<br>");
                            } catch (Exception e) {
                                out.println("❌ 测试数据插入失败: " + e.getMessage() + "<br>");
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        out.println("❌ clubService.findAll() 调用失败: " + e.getMessage() + "<br>");
                        e.printStackTrace();
                    }
                } else {
                    out.println("❌ ClubService 注入失败<br>");
                }
            } else {
                out.println("❌ Spring Context 未加载<br>");
            }
        } catch (Exception e) {
            out.println("❌ 获取 Spring Context 失败: " + e.getMessage() + "<br>");
            e.printStackTrace();
        }
    %>
    
    <h3>2. 数据库连接检查</h3>
    <%
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/club_system?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8",
                "root", "123456");
            if (conn != null && !conn.isClosed()) {
                out.println("✅ 数据库连接成功<br>");
                
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM t_club");
                if (rs.next()) {
                    int count = rs.getInt("count");
                    out.println("📊 t_club 表中有 " + count + " 条记录<br>");
                }
                rs.close();
                stmt.close();
                conn.close();
            } else {
                out.println("❌ 数据库连接失败<br>");
            }
        } catch (Exception e) {
            out.println("❌ 数据库连接异常: " + e.getMessage() + "<br>");
            e.printStackTrace();
        }
    %>
    
    <br>
    <a href="<%= request.getContextPath() %>/index">返回首页</a>
</body>
</html>