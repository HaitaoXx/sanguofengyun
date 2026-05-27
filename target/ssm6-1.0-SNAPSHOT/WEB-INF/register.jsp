<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册 - 社团管理系统</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; }
        .register-container { max-width: 500px; width: 100%; background: rgba(255, 255, 255, 0.95); border-radius: 15px; padding: 40px; box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1); backdrop-filter: blur(10px); }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: 600; color: #555; }
        input[type="text"], input[type="password"], select { width: 100%; padding: 12px 15px; border: 2px solid #e1e5e9; border-radius: 8px; font-size: 15px; transition: border-color 0.3s ease, box-shadow 0.3s ease; background: rgba(255, 255, 255, 0.8); }
        input[type="text"]:focus, input[type="password"]:focus, select:focus { outline: none; border-color: #dc143c; box-shadow: 0 0 0 3px rgba(220, 20, 60, 0.1); }
        .btn { width: 100%; padding: 14px 30px; border: none; border-radius: 25px; cursor: pointer; font-size: 16px; font-weight: 500; transition: all 0.3s ease; background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%); color: white; box-shadow: 0 4px 15px rgba(220, 20, 60, 0.3); }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(220, 20, 60, 0.4); }
        .error { color: #dc3545; margin-bottom: 20px; padding: 12px 15px; border: 1px solid #f5c6cb; background-color: #f8d7da; border-radius: 8px; font-weight: 500; }
        .success { color: #28a745; margin-bottom: 20px; padding: 12px 15px; border: 1px solid #c3e6cb; background-color: #d4edda; border-radius: 8px; font-weight: 500; }
        h2 { text-align: center; color: #333; margin-bottom: 10px; font-size: 28px; font-weight: 700; }
        h4 { text-align: center; color: #666; margin-bottom: 30px; font-size: 16px; font-weight: 400; }
        .logo-icon { text-align: center; margin-bottom: 20px; font-size: 48px; }
        .login-link { text-align: center; margin-top: 20px; color: #666; }
        .login-link a { color: #dc143c; text-decoration: none; font-weight: 500; }
        .login-link a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="register-container">
    <div class="logo-icon">🎓</div>
    <h2>社团管理系统</h2>
    <h4>用户注册</h4>
    <form action="<%= request.getContextPath() %>/register/submit" method="post">
    <%
        String error = (String) request.getAttribute("error");
        if (error != null && !error.isEmpty()) {
    %>
            <div class="error"><%= error %></div>
    <%
        }
    %>
        <div class="form-group">
            <label for="username">用户名:</label>
            <input type="text" id="username" name="username" required placeholder="请输入用户名">
        </div>
        <div class="form-group">
            <label for="password">密码:</label>
            <input type="password" id="password" name="password" required placeholder="请输入密码">
        </div>
        <div class="form-group">
            <label for="confirmPassword">确认密码:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="请再次输入密码">
        </div>
        <div class="form-group">
            <label for="name">姓名:</label>
            <input type="text" id="name" name="name" required placeholder="请输入真实姓名">
        </div>
        <div class="form-group">
            <label for="role">角色:</label>
            <select id="role" name="role" required>
                <option value="student" selected>学生</option>
                <option value="leader">社长</option>
            </select>
        </div>
        <div class="form-group">
            <button type="submit" class="btn">注册</button>
        </div>
        <div class="login-link">
            已有账号？<a href="<%= request.getContextPath() %>/login">立即登录</a>
        </div>
    </form>
</div>
</body>
</html>