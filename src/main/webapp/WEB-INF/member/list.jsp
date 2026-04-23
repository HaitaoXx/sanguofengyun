<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.Member" %>
<%@ page import="com.zwj.entity.User" %>
<%@ page import="com.zwj.entity.Club" %>
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
    <title>成员管理</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 8px 16px; margin: 5px; text-decoration: none; }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-warning { background-color: #ffc107; color: black; }
        .btn-danger { background-color: #dc3545; color: white; }
        .btn-success { background-color: #28a745; color: white; }
    </style>
</head>
<body>
<h2>成员管理</h2>
<a href="<%= request.getContextPath() %>/member/add" class="btn btn-success">添加成员</a>
<a href="<%= request.getContextPath() %>/index" class="btn btn-primary">返回首页</a>

<table>
    <tr>
        <th>ID</th>
        <th>用户ID</th>
        <th>用户名</th>
        <th>社团</th>
        <th>加入时间</th>
        <th>角色</th>
        <th>操作</th>
    </tr>
    <%
        List<Member> members = (List<Member>) request.getAttribute("members");
        List<User> users = (List<User>) request.getAttribute("users");
        List<Club> clubs = (List<Club>) request.getAttribute("clubs");
        if (members != null && users != null && clubs != null) {
            for (Member member : members) {
    %>
    <tr>
        <td><%= member.getId() %></td>
        <td><%= member.getUserId() %></td>
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
        <td><%= member.getJoinTime() %></td>
        <td><%= member.getRole() %></td>
        <td>
            <%
                if (userInfo != null && ("admin".equals(((com.zwj.entity.User)userInfo).getRole()) || "leader".equals(((com.zwj.entity.User)userInfo).getRole()))) {
            %>
                <a href="<%= request.getContextPath() %>/member/edit/<%= member.getId() %>" class="btn btn-warning">编辑</a>
                <a href="<%= request.getContextPath() %>/member/delete/<%= member.getId() %>" 
                   class="btn btn-danger" 
                   onclick="return confirm('确定要删除这个成员吗？')">删除</a>
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
        }
    %>
</table>
</body>
</html>