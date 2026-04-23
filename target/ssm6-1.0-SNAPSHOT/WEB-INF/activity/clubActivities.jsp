<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 权限检查：只有管理员和社长能访问此页面
    Object userInfo = request.getSession().getAttribute("user");
    if (userInfo == null || (!"admin".equals(((com.zwj.entity.User)userInfo).getRole()) && !"leader".equals(((com.zwj.entity.User)userInfo).getRole()))) {
        response.sendRedirect(request.getContextPath() + "/index");
        return;
    }
%>
<html>
<head>
    <title>活动管理 - 社团管理系统</title>
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
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e9ecef;
        }
        
        .page-title {
            font-size: 32px;
            color: #333;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .button-group {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        
        .btn { 
            padding: 10px 20px; 
            text-decoration: none; 
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.3s ease;
            display: inline-block;
            border: none;
            cursor: pointer;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
        }
        
        .btn-primary { 
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white; 
        }
        
        .btn-success { 
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%); 
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
        
        .activities-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }
        
        .activity-card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
            transition: all 0.3s ease;
            border: 1px solid #e9ecef;
            position: relative;
            overflow: hidden;
        }
        
        .activity-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffc107, #ff9800);
        }
        
        .activity-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.12);
        }
        
        .activity-header {
            display: flex;
            align-items: flex-start;
            gap: 15px;
            margin-bottom: 15px;
        }
        
        .activity-icon {
            font-size: 32px;
            color: #ffc107;
            flex-shrink: 0;
        }
        
        .activity-title-section {
            flex: 1;
        }
        
        .activity-title {
            font-size: 20px;
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
            line-height: 1.3;
        }
        
        .activity-id {
            font-size: 14px;
            color: #999;
        }
        
        .activity-club {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 15px;
            display: inline-block;
        }
        
        .activity-details {
            margin-bottom: 20px;
        }
        
        .detail-item {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 10px;
            color: #666;
            font-size: 14px;
        }
        
        .detail-icon {
            color: #ffc107;
            width: 20px;
            text-align: center;
        }
        
        .activity-description {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #ffc107;
        }
        
        .description-title {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
        }
        
        .description-content {
            color: #666;
            line-height: 1.5;
        }
        
        .activity-actions {
            display: flex;
            gap: 10px;
            padding-top: 15px;
            border-top: 1px solid #e9ecef;
        }
        
        .btn-sm {
            padding: 8px 16px;
            font-size: 14px;
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
        
        .stats-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            text-align: center;
        }
        
        .stat-number {
            font-size: 32px;
            font-weight: bold;
            color: #ffc107;
        }
        
        .stat-label {
            color: #6c757d;
            font-size: 14px;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1 class="page-title">📅 活动管理</h1>
        <div class="button-group">
            <a href="<%= request.getContextPath() %>/activity/add?clubId=<%= request.getAttribute("clubId") %>" class="btn btn-primary">➕ 添加活动</a>
            <a href="<%= request.getContextPath() %>/index" class="btn btn-success">🏠 返回首页</a>
        </div>
    </div>
    
    <%
        Object activitiesObj = request.getAttribute("activities");
        Object clubsObj = request.getAttribute("clubs");
        Object clubIdObj = request.getAttribute("clubId");
        
        if (activitiesObj != null && clubsObj != null) {
            java.util.List<com.zwj.entity.Activity> activities = (java.util.List<com.zwj.entity.Activity>) activitiesObj;
            java.util.List<com.zwj.entity.Club> clubs = (java.util.List<com.zwj.entity.Club>) clubsObj;
            Integer clubId = (Integer) clubIdObj;
            
            int upcomingCount = 0;
            int totalActivities = activities.size();
    %>
            <div class="stats-cards">
                <div class="stat-card">
                    <div class="stat-number"><%= totalActivities %></div>
                    <div class="stat-label">总活动数</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= upcomingCount %></div>
                    <div class="stat-label">即将进行</div>
                </div>
            </div>
            
            <%
                if (activities.isEmpty()) {
            %>
                    <div class="empty-state">
                        <div class="empty-icon">📅</div>
                        <div>该社团暂无活动</div>
                        <a href="<%= request.getContextPath() %>/activity/add?clubId=<%= clubId %>" class="btn btn-primary" style="margin-top: 20px;">➕ 创建第一个活动</a>
                    </div>
            <%
                } else {
            %>
                    <div class="activities-grid">
            <%
                        for (com.zwj.entity.Activity activity : activities) {
                            String clubName = "";
                            for (com.zwj.entity.Club club : clubs) {
                                if (club.getId().equals(activity.getClubId())) {
                                    clubName = club.getName();
                                    break;
                                }
                            }
            %>
                            <div class="activity-card">
                                <div class="activity-header">
                                    <div class="activity-icon">📅</div>
                                    <div class="activity-title-section">
                                        <div class="activity-title"><%= activity.getName() %></div>
                                        <div class="activity-id">ID: #<%= activity.getId() %></div>
                                    </div>
                                </div>
                                
                                <div class="activity-club">
                                    🏢 <%= clubName %>
                                </div>
                                
                                <div class="activity-details">
                                    <div class="detail-item">
                                        <span class="detail-icon">🕐</span>
                                        <span><strong>活动时间:</strong> <%= activity.getActivityTime() %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-icon">📍</span>
                                        <span><strong>活动地点:</strong> <%= activity.getLocation() != null ? activity.getLocation() : "待定" %></span>
                                    </div>
                                </div>
                                
                                <div class="activity-description">
                                    <div class="description-title">📝 活动描述</div>
                                    <div class="description-content">
                                        <%= activity.getDescription() != null && !activity.getDescription().isEmpty() ? 
                                            activity.getDescription() : "暂无活动描述" %>
                                    </div>
                                </div>
                                
                                <div class="activity-actions">
                                    <%
                                        if (userInfo != null && ("admin".equals(((com.zwj.entity.User)userInfo).getRole()) || "leader".equals(((com.zwj.entity.User)userInfo).getRole()))) {
                                    %>
                                        <a href="<%= request.getContextPath() %>/activity/edit/<%= activity.getId() %>" 
                                           class="btn btn-warning btn-sm">✏️ 编辑</a>
                                        <a href="<%= request.getContextPath() %>/activity/delete/<%= activity.getId() %>" 
                                           class="btn btn-danger btn-sm" 
                                           onclick="return confirm('确定要删除活动「<%= activity.getName() %>」吗？')">🗑️ 删除</a>
                                    <%
                                        } else {
                                    %>
                                        <span style="color: #6c757d; font-size: 12px;">无操作权限</span>
                                    <%
                                        }
                                    %>
                                </div>
                            </div>
            <%
                        }
            %>
                    </div>
            <%
                }
            } else {
            %>
                    <div class="empty-state">
                        <div class="empty-icon">⚠️</div>
                        <div>无法加载活动数据</div>
                    </div>
            <%
            }
            %>
</div>
</body>
</html>