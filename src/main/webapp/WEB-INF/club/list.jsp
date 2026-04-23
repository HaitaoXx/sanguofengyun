<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.Club" %>
<%
    // 获取用户信息用于权限控制
    Object userInfo = request.getSession().getAttribute("user");
%>
<html>
<head>
    <title>社团管理 - 社团管理系统</title>
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
            background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%);
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
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }
        
        th, td { 
            border: 1px solid #e9ecef; 
            padding: 15px 12px; 
            text-align: left;
        }
        
        th { 
            background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%);
            color: white;
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        
        tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        
        tr:hover {
            background-color: #e3f2fd;
            transition: background-color 0.3s ease;
        }
        
        .btn { 
            padding: 8px 16px; 
            margin: 2px; 
            text-decoration: none; 
            border-radius: 6px;
            font-weight: 500;
            transition: all 0.3s ease;
            display: inline-block;
            border: none;
            cursor: pointer;
        }
        
        .btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }
        
        .btn-primary { 
            background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%);
            color: white; 
        }
        
        .btn-warning { 
            background: linear-gradient(135deg, #ffc107 0%, #ff9800 100%); 
            color: white; 
        }
        
        .btn-danger { 
            background: linear-gradient(135deg, #dc3545 0%, #e74c3c 100%); 
            color: white; 
        }
        
        .btn-success { 
            background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%); 
            color: white; 
        }
        
        .id-cell {
            font-family: 'Courier New', monospace;
            font-weight: bold;
            color: #6c757d;
        }
        
        .name-cell {
            font-weight: 600;
            color: #333;
        }
        
        .description-cell {
            max-width: 300px;
            line-height: 1.4;
        }
        
        .time-cell {
            font-size: 14px;
            color: #6c757d;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #6c757d;
        }
        
        .empty-icon {
            font-size: 64px;
            margin-bottom: 20px;
            opacity: 0.5;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>🏢 社团管理</h2>
    <div class="button-group">
        <%
            if (userInfo != null && "admin".equals(((com.zwj.entity.User)userInfo).getRole())) {
        %>
            <a href="<%= request.getContextPath() %>/club/add" class="btn btn-success">➕ 添加社团</a>
        <%
            }
        %>
        <a href="<%= request.getContextPath() %>/index" class="btn btn-primary">🏠 返回首页</a>
    </div>

    <table>
    <tr>
        <th>🆔 ID</th>
        <th>📛 社团名称</th>
        <th>📝 社团简介</th>
        <th>🕐 创建时间</th>
        <th>⚙️ 操作</th>
    </tr>
    <%
        List<Club> clubs = (List<Club>) request.getAttribute("clubs");
        if (clubs != null && !clubs.isEmpty()) {
            for (Club club : clubs) {
    %>
    <tr>
        <td class="id-cell">#<%= club.getId() %></td>
        <td class="name-cell"><%= club.getName() %></td>
        <td class="description-cell"><%= club.getIntro() != null ? club.getIntro() : "暂无简介" %></td>
        <td class="time-cell"><%= club.getCreateTime() %></td>
        <td>
            <%
                if (userInfo != null && "admin".equals(((com.zwj.entity.User)userInfo).getRole())) {
            %>
                <a href="<%= request.getContextPath() %>/club/edit/<%= club.getId() %>" class="btn btn-warning">✏️ 编辑</a>
                <a href="<%= request.getContextPath() %>/club/delete/<%= club.getId() %>" 
                   class="btn btn-danger" 
                   onclick="return confirm('确定要删除这个社团吗？')">🗑️ 删除</a>
            <%
                } else {
            %>
                <span style="color: #6c757d; font-size: 12px;">无操作权限</span>
            <%
                }
            %>
        </td>
    </tr>
    <%
            }
        } else {
    %>
            <tr>
                <td colspan="5" class="empty-state">
                    <div class="empty-icon">📭</div>
                    <div>暂无社团数据</div>
                    <a href="<%= request.getContextPath() %>/club/add" class="btn btn-success" style="margin-top: 20px;">➕ 创建第一个社团</a>
                </td>
            </tr>
    <%
        }
    %>
</table>
</div>
</body>
</html>