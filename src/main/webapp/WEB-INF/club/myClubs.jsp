<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.Club" %>
<html>
<head>
    <title>我的社团 - 社团管理系统</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            margin: 0;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        h2 {
            font-size: 32px;
            color: #333;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        h2::before {
            content: "🏛️";
            font-size: 28px;
        }
        
        .button-group {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        
        table { 
            border-collapse: collapse; 
            width: 100%; 
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        
        th { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; 
            padding: 15px 12px; 
            text-align: left; 
            font-weight: 600;
            font-size: 14px;
        }
        
        td { 
            padding: 12px; 
            border-bottom: 1px solid #f0f0f0;
            transition: background-color 0.3s ease;
        }
        
        tr:hover td {
            background-color: #f8f9ff;
        }
        
        tr:last-child td {
            border-bottom: none;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        
        .empty-state-icon {
            font-size: 48px;
            margin-bottom: 20px;
            opacity: 0.5;
        }
        
        .empty-state-text {
            font-size: 18px;
            margin-bottom: 20px;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s ease;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #6c757d;
        }
        
        .btn-secondary:hover {
            background: #5a6268;
            box-shadow: 0 5px 15px rgba(108, 117, 125, 0.4);
        }
        
        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .stats-info h3 {
            font-size: 14px;
            opacity: 0.9;
            margin-bottom: 5px;
        }
        
        .stats-number {
            font-size: 28px;
            font-weight: bold;
        }
        
        .back-link {
            color: white;
            text-decoration: none;
            margin-bottom: 20px;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 8px 16px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 20px;
            transition: background 0.3s ease;
        }
        
        .back-link:hover {
            background: rgba(255, 255, 255, 0.3);
        }
    </style>
</head>
<body>
    <a href="<%= request.getContextPath() %>/index" class="back-link">
        ← 返回首页
    </a>
    
    <div class="container">
        <h2>我的社团</h2>
        
        <%
            Object clubsObj = request.getAttribute("clubs");
            if (clubsObj != null) {
                java.util.List<com.zwj.entity.Club> clubs = (java.util.List<com.zwj.entity.Club>) clubsObj;
                if (clubs.isEmpty()) {
        %>
                    <div class="empty-state">
                        <div class="empty-state-icon">📋</div>
                        <div class="empty-state-text">您还没有加入任何社团</div>
                        <a href="<%= request.getContextPath() %>/club/list" class="btn">
                            浏览所有社团
                        </a>
                    </div>
        <%
                } else {
        %>
                    <div class="stats-card">
                        <div class="stats-info">
                            <h3>已加入社团</h3>
                            <div class="stats-number"><%= clubs.size() %></div>
                        </div>
                        <div style="font-size: 48px; opacity: 0.7;">🏛️</div>
                    </div>
                    
                    <div class="button-group">
                        <a href="<%= request.getContextPath() %>/club/list" class="btn btn-secondary">
                            浏览所有社团
                        </a>
                    </div>
                    
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>社团名称</th>
                                <th>社团简介</th>
                                <th>创建时间</th>
                            </tr>
                        </thead>
                        <tbody>
            <%
                            for (com.zwj.entity.Club club : clubs) {
            %>
                            <tr>
                                <td><%= club.getId() %></td>
                                <td><strong><%= club.getName() %></strong></td>
                                <td><%= club.getIntro() != null ? club.getIntro() : "暂无简介" %></td>
                                <td><%= club.getCreateTime() %></td>
                            </tr>
            <%
                            }
            %>
                        </tbody>
                    </table>
        <%
                }
            } else {
        %>
                <div class="empty-state">
                    <div class="empty-state-icon">📋</div>
                    <div class="empty-state-text">您还没有加入任何社团</div>
                    <a href="<%= request.getContextPath() %>/club/list" class="btn">
                        浏览所有社团
                    </a>
                </div>
        <%
            }
        %>
    </div>
</body>
</html>