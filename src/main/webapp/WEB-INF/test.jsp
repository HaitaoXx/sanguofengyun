<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.zwj.entity.User" %>
<%@ page import="com.zwj.service.UserService" %>
<%@ page import="com.zwj.service.impl.UserServiceImpl" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<html>
<head>
    <title>测试用户数据</title>
</head>
<body>
<h2>测试用户数据</h2>
<%
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
    UserService userService = ctx.getBean("userServiceImpl", UserService.class);
    
    // 查询所有用户
    java.util.List<User> users = userService.findAll();
    out.println("<h3>所有用户：</h3>");
    for(User user : users) {
        out.println("ID: " + user.getId() + 
                   ", 用户名: " + user.getUsername() + 
                   ", 密码: " + user.getPassword() + 
                   ", 角色: " + user.getRole() + 
                   ", 姓名: " + user.getName() + "<br>");
    }
    
    // 测试登录
    out.println("<h3>测试登录：</h3>");
    User testUser = userService.login("admin", "123456", "admin");
    if (testUser != null) {
        out.println("admin登录成功：" + testUser.getName());
    } else {
        out.println("admin登录失败");
    }
%>
</body>
</html>