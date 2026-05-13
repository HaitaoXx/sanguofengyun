<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 获取用户信息用于权限控制
    Object userInfo = request.getSession().getAttribute("user");
%>
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
            max-width: 800px;
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
            content: "👑";
            font-size: 28px;
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
        
        .club-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            border: 2px solid #f0f0f0;
            position: relative;
            overflow: hidden;
        }
        
        .club-card::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 5px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .club-header {
            margin-bottom: 25px;
            padding-bottom: 20px;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .club-title {
            font-size: 28px;
            color: #333;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .club-id {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
        }
        
        .club-info {
            display: grid;
            gap: 20px;
        }
        
        .info-row {
            display: flex;
            align-items: flex-start;
            gap: 15px;
        }
        
        .info-label {
            font-weight: 600;
            color: #666;
            min-width: 100px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .info-value {
            flex: 1;
            color: #333;
            font-size: 16px;
            line-height: 1.6;
        }
        
        .action-buttons {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin-top: 30px;
        }
        
        .btn {
            padding: 12px 20px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            font-size: 15px;
            font-weight: 500;
            transition: all 0.3s ease;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }
        
        .btn-members {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
        }
        
        .btn-members:hover {
            box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
        }
        
        .btn-activities {
            background: linear-gradient(135deg, #fd7e14 0%, #e83e8c 100%);
        }
        
        .btn-activities:hover {
            box-shadow: 0 6px 20px rgba(253, 126, 20, 0.4);
        }
        
        .btn-edit {
            background: linear-gradient(135deg, #17a2b8 0%, #6f42c1 100%);
        }
        
        .btn-edit:hover {
            box-shadow: 0 6px 20px rgba(23, 162, 184, 0.4);
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        
        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
            opacity: 0.5;
        }
        
        .empty-state-text {
            font-size: 18px;
            margin-bottom: 10px;
        }
        
        .role-badge {
            background: linear-gradient(135deg, #ffc107 0%, #ff6b6b 100%);
            color: white;
            padding: 3px 10px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <a href="<%= request.getContextPath() %>/index" class="back-link">
        ← 返回首页
    </a>
    
    <div class="container">
        <h2>我的社团 <span class="role-badge">社长</span></h2>
        
        <%
            Object clubObj = request.getAttribute("club");
            if (clubObj != null) {
                com.zwj.entity.Club club = (com.zwj.entity.Club) clubObj;
        %>
            <div class="club-card">
                <div class="club-header">
                    <div class="club-title">
                        <span>🏛️</span>
                        <%= club.getName() %>
                        <span class="club-id">ID: <%= club.getId() %></span>
                    </div>
                </div>
                
                <div class="club-info">
                    <div class="info-row">
                        <div class="info-label">
                            <span>📝</span> 社团简介
                        </div>
                        <div class="info-value">
                            <%= club.getIntro() != null ? club.getIntro() : "暂无简介" %>
                        </div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">
                            <span>📅</span> 创建时间
                        </div>
                        <div class="info-value">
                            <%= club.getCreateTime() %>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="action-buttons">
                <a href="<%= request.getContextPath() %>/member/clubMembers" class="btn btn-members">
                    <span>👥</span> 管理成员
                </a>
                <a href="<%= request.getContextPath() %>/activity/clubActivities" class="btn btn-activities">
                    <span>🎯</span> 管理活动
                </a>
                <%
                    if (userInfo != null && "admin".equals(((com.zwj.entity.User)userInfo).getRole())) {
                %>
                    <a href="<%= request.getContextPath() %>/club/edit/<%= club.getId() %>" class="btn btn-edit">
                        <span>✏️</span> 编辑社团
                    </a>
                <%
                    }
                %>
            </div>
        <%
        } else {
        %>
            <div class="empty-state">
                <div class="empty-state-icon">👑</div>
                <div class="empty-state-text">您还没有管理的社团</div>
                <div style="color: #999; font-size: 14px;">请联系管理员为您分配社团管理权限</div>
            </div>
        <%
        }
        %>
    </div>
</body>
</html>