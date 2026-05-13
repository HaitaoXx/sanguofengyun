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
    <title>成员管理 - 社团管理系统</title>
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
        
        .btn-danger { 
            background: linear-gradient(135deg, #dc3545 0%, #e74c3c 100%); 
            color: white; 
        }
        
        table { 
            border-collapse: collapse; 
            width: 100%; 
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            margin-top: 20px;
        }
        
        th, td { 
            border: 1px solid #e9ecef; 
            padding: 15px 12px; 
            text-align: left;
        }
        
        th { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
        
        .id-cell {
            font-family: 'Courier New', monospace;
            font-weight: bold;
            color: #6c757d;
        }
        
        .username-cell {
            font-family: 'Courier New', monospace;
            color: #495057;
        }
        
        .name-cell {
            font-weight: 600;
            color: #333;
        }
        
        .role-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
        }
        
        .role-leader {
            background: linear-gradient(135deg, #ffc107 0%, #ff9800 100%);
            color: white;
        }
        
        .role-member {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
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
            color: #667eea;
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
        <h1 class="page-title">👥 成员管理</h1>
        <div class="button-group">
            <a href="<%= request.getContextPath() %>/member/add?clubId=<%= request.getAttribute("clubId") %>" class="btn btn-success">➕ 添加成员</a>
            <a href="<%= request.getContextPath() %>/index" class="btn btn-primary">🏠 返回首页</a>
        </div>
    </div>
    
    <%
        Object membersObj = request.getAttribute("members");
        Object usersObj = request.getAttribute("users");
        Object clubIdObj = request.getAttribute("clubId");
        
        if (membersObj != null && usersObj != null) {
            java.util.List<com.zwj.entity.Member> members = (java.util.List<com.zwj.entity.Member>) membersObj;
            java.util.List<com.zwj.entity.User> users = (java.util.List<com.zwj.entity.User>) usersObj;
            Integer clubId = (Integer) clubIdObj;
            
            int leaderCount = 0;
            int memberCount = 0;
            
            for (com.zwj.entity.Member member : members) {
                if ("社长".equals(member.getRole())) {
                    leaderCount++;
                } else {
                    memberCount++;
                }
            }
    %>
            <div class="stats-cards">
                <div class="stat-card">
                    <div class="stat-number"><%= members.size() %></div>
                    <div class="stat-label">总成员数</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= leaderCount %></div>
                    <div class="stat-label">社长</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= memberCount %></div>
                    <div class="stat-label">普通成员</div>
                </div>
            </div>
            
            <%
                if (members.isEmpty()) {
            %>
                    <div class="empty-state">
                        <div class="empty-icon">👥</div>
                        <div>该社团暂无成员</div>
                        <a href="<%= request.getContextPath() %>/member/add?clubId=<%= clubId %>" class="btn btn-success" style="margin-top: 20px;">➕ 添加第一个成员</a>
                    </div>
            <%
                } else {
            %>
                    <table>
                        <tr>
                            <th>🆔 ID</th>
                            <th>👤 用户名</th>
                            <th>📝 姓名</th>
                            <th>👑 角色</th>
                            <th>🕐 加入时间</th>
                            <th>⚙️ 操作</th>
                        </tr>
            <%
                    for (com.zwj.entity.Member member : members) {
                        String userName = "";
                        String realName = "";
                        for (com.zwj.entity.User user : users) {
                            if (user.getId().equals(member.getUserId())) {
                                userName = user.getUsername();
                                realName = user.getName();
                                break;
                            }
                        }
                        
                        String roleText = member.getRole() != null ? member.getRole() : "成员";
                        String roleClass = "社长".equals(roleText) ? "role-leader" : "role-member";
            %>
                        <tr>
                            <td class="id-cell">#<%= member.getId() %></td>
                            <td class="username-cell">@<%= userName %></td>
                            <td class="name-cell"><%= realName %></td>
                            <td>
                                <span class="role-badge <%= roleClass %>">
                                    <%= "社长".equals(roleText) ? "👑 " + roleText : "👤 " + roleText %>
                                </span>
                            </td>
                            <td class="time-cell"><%= member.getJoinTime() %></td>
                            <td>
                                <%
                                    if (userInfo != null && ("admin".equals(((com.zwj.entity.User)userInfo).getRole()) || "leader".equals(((com.zwj.entity.User)userInfo).getRole()))) {
                                %>
                                    <a href="<%= request.getContextPath() %>/member/delete/<%= member.getId() %>" 
                                       class="btn btn-danger" 
                                       style="padding: 6px 12px; font-size: 12px;"
                                       onclick="return confirm('确定要移除成员 <%= realName %> 吗？')">🗑️ 移除</a>
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
            %>
                    </table>
            <%
                }
            } else {
            %>
                    <div class="empty-state">
                        <div class="empty-icon">⚠️</div>
                        <div>无法加载成员数据</div>
                    </div>
            <%
            }
            %>
</div>
</body>
</html>