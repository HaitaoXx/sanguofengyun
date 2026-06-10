<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>错误</title>
</head>
<body>
    <h2>系统发生错误</h2>
    <p>${error}</p>
    <a href="${pageContext.request.contextPath}/index">返回首页</a>
</body>
</html>
