<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>错误页面 - 社团管理系统</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 20px; }
        .error-container { background: rgba(255, 255, 255, 0.95); border-radius: 16px; padding: 60px 40px; text-align: center; box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2); max-width: 500px; width: 100%; }
        .error-icon { font-size: 72px; margin-bottom: 20px; }
        h2 { color: #dc143c; font-size: 28px; margin-bottom: 15px; }
        .error-message { color: #666; font-size: 16px; margin-bottom: 10px; }
        .error-detail { color: #999; font-size: 14px; margin-bottom: 30px; }
        .btn { display: inline-block; padding: 12px 30px; background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%); color: white; text-decoration: none; border-radius: 25px; font-weight: 500; transition: all 0.3s ease; }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(220, 20, 60, 0.4); }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">❌</div>
        <h2>出现错误</h2>
        <p class="error-message">${error}</p>
        <p class="error-detail">${detail}</p>
        <a href="${pageContext.request.contextPath}/index" class="btn">返回首页</a>
    </div>
</body>
</html>