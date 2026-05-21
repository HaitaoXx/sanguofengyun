<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>我的社团申请 - 社团管理系统</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; }
        h2 { color: #333; margin-bottom: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #dc143c; color: white; }
        tr:hover { background-color: #f9f9f9; }
        .status-pending { color: #ffc107; font-weight: bold; }
        .status-approved { color: #28a745; font-weight: bold; }
        .status-rejected { color: #dc3545; font-weight: bold; }
        .btn { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
        .btn-back { background-color: #6c757d; color: white; }
        .btn:hover { opacity: 0.8; }
    </style>
</head>
<body>
<div class="container">
    <h2>我的社团申请</h2>
    <table>
        <thead>
            <tr>
                <th>社团ID</th>
                <th>申请时间</th>
                <th>状态</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${applies}" var="apply">
                <tr>
                    <td>${apply.clubId}</td>
                    <td>${apply.applyTime}</td>
                    <td>
                        <span class="status-${apply.status}">
                            <c:if test="${apply.status == 'pending'}">待审核</c:if>
                            <c:if test="${apply.status == 'approved'}">已通过</c:if>
                            <c:if test="${apply.status == 'rejected'}">已拒绝</c:if>
                        </span>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <br>
    <a href="<%= request.getContextPath() %>/club/list">
        <button class="btn btn-back">返回社团列表</button>
    </a>
</div>
</body>
</html>
