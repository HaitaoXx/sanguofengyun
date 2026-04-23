<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.User" %>
<%
    // 权限检查：只有管理员能访问此页面
    Object userInfo = request.getSession().getAttribute("user");
    if (userInfo == null || !"admin".equals(((com.zwj.entity.User)userInfo).getRole())) {
        response.sendRedirect(request.getContextPath() + "/index");
        return;
    }
%>
<html>
<head>
    <title>用户管理</title>
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
<h2>用户管理</h2>
<a href="<%= request.getContextPath() %>/user/add" class="btn btn-success">添加用户</a>
<a href="<%= request.getContextPath() %>/index" class="btn btn-primary">返回首页</a>

<table>
    <tr>
        <th>ID</th>
        <th>用户名</th>
        <th>密码</th>
        <th>角色</th>
        <th>姓名</th>
        <th>社团ID</th>
        <th>创建时间</th>
        <th>操作</th>
    </tr>
    <%
        List<User> users = (List<User>) request.getAttribute("users");
        if (users != null) {
            for (User user : users) {
    %>
    <tr>
        <td><%= user.getId() %></td>
        <td><%= user.getUsername() %></td>
        <td><%= user.getPassword() %></td>
        <td><%= user.getRole() %></td>
        <td><%= user.getName() %></td>
        <td><%= user.getClubId() != null ? user.getClubId() : "" %></td>
        <td><%= user.getCreateTime() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/user/edit/<%= user.getId() %>" class="btn btn-warning">编辑</a>
            <a href="<%= request.getContextPath() %>/user/delete/<%= user.getId() %>" 
               class="btn btn-danger" 
               onclick="return confirm('确定要删除这个用户吗？')">删除</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>