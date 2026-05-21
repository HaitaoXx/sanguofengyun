<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.Member" %>
<%@ page import="com.zwj.entity.User" %>
<%@ page import="com.zwj.entity.Club" %>
<%
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
        * { margin: 0; padding: 0; box-sizing: border-box; }
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
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        
        h2 { 
            color: white; 
            font-size: 28px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3);
        }
        
        .btn {
            display: inline-block;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            text-decoration: none;
            transition: all 0.3s ease;
        }
        
        .btn-success {
            background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%);
            color: white;
            box-shadow: 0 4px 12px rgba(220, 20, 60, 0.3);
        }
        
        .btn-success:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(220, 20, 60, 0.4);
        }
        
        .btn-primary {
            background: rgba(255, 255, 255, 0.15);
            color: white;
            border: 1px solid rgba(255, 255, 255, 0.3);
        }
        
        .btn-primary:hover {
            background: rgba(255, 255, 255, 0.25);
        }
        
        .btn-warning {
            background: linear-gradient(135deg, #ffc107 0%, #ff8f00 100%);
            color: #1a1a1a;
            font-weight: 600;
        }
        
        .btn-warning:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 10px rgba(255, 193, 7, 0.4);
        }
        
        .btn-danger {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
        }
        
        .btn-danger:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 10px rgba(220, 53, 69, 0.4);
        }
        
        .table-container {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 16px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            overflow: hidden;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        th {
            background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%);
            color: white;
            padding: 16px 20px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        td {
            padding: 14px 20px;
            border-bottom: 1px solid #e8e8e8;
            color: #333;
        }
        
        tr:hover td {
            background-color: #fafafa;
        }
        
        tr:last-child td {
            border-bottom: none;
        }
        
        .role-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 500;
        }
        
        .role-leader {
            background-color: #ffeeba;
            color: #856404;
        }
        
        .role-member {
            background-color: #d4edda;
            color: #155724;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        
        .empty-state i {
            font-size: 48px;
            margin-bottom: 16px;
            opacity: 0.5;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>👥 成员管理</h2>
        <div>
            <a href="<%= request.getContextPath() %>/member/add" class="btn btn-success">➕ 添加成员</a>
            <a href="<%= request.getContextPath() %>/index" class="btn btn-primary">🏠 返回首页</a>
        </div>
    </div>
    
    <div class="table-container">
        <table>
            <tr>
                <th>ID</th>
                <th>用户名</th>
                <th>姓名</th>
                <th>社团</th>
                <th>加入时间</th>
                <th>角色</th>
                <th>操作</th>
            </tr>
            <%
                List<Member> members = (List<Member>) request.getAttribute("members");
                List<User> users = (List<User>) request.getAttribute("users");
                List<Club> clubs = (List<Club>) request.getAttribute("clubs");
                if (members != null && !members.isEmpty() && users != null && clubs != null) {
                    for (Member member : members) {
            %>
            <tr>
                <td><%= member.getId() %></td>
                <td>
                    <%
                        for (User user : users) {
                            if (user.getId().equals(member.getUserId())) {
                                out.println(user.getUsername());
                                break;
                            }
                        }
                    %>
                </td>
                <td>
                    <%
                        for (User user : users) {
                            if (user.getId().equals(member.getUserId())) {
                                out.println(user.getName());
                                break;
                            }
                        }
                    %>
                </td>
                <td>
                    <%
                        for (Club club : clubs) {
                            if (club.getId().equals(member.getClubId())) {
                                out.println(club.getName());
                                break;
                            }
                        }
                    %>
                </td>
                <td><%= member.getJoinTime() != null ? member.getJoinTime() : "" %></td>
                <td>
                    <span class="role-badge <%= "leader".equals(member.getRole()) ? "role-leader" : "role-member" %>">
                        <%= "leader".equals(member.getRole()) ? "社长" : "成员" %>
                    </span>
                </td>
                <td>
                    <%
                        if (userInfo != null && ("admin".equals(((com.zwj.entity.User)userInfo).getRole()) || "leader".equals(((com.zwj.entity.User)userInfo).getRole()))) {
                    %>
                        <a href="<%= request.getContextPath() %>/member/edit/<%= member.getId() %>" class="btn btn-warning" style="padding: 6px 12px; font-size: 12px;">✏️ 编辑</a>
                        <a href="<%= request.getContextPath() %>/member/delete/<%= member.getId() %>" 
                           class="btn btn-danger" 
                           style="padding: 6px 12px; font-size: 12px;"
                           onclick="return confirm('确定要删除这个成员吗？')">🗑️ 删除</a>
                    <%
                        } else {
                    %>
                        <span style="color: #999; font-size: 12px;">无操作权限</span>
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
                <td colspan="7" class="empty-state">
                    <i>📭</i><br/>
                    <p>暂无成员数据</p>
                </td>
            </tr>
            <%
                }
            %>
        </table>
    </div>
</div>
</body>
</html>