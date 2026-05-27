<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>活动报名列表 - 社团管理系统</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 20px; }
        .container { max-width: 1000px; margin: 0 auto; }
        h2 { color: #333; margin-bottom: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #dc143c; color: white; }
        tr:hover { background-color: #f9f9f9; }
        .status-pending { color: #ffc107; font-weight: bold; }
        .status-approved { color: #28a745; font-weight: bold; }
        .status-rejected { color: #dc3545; font-weight: bold; }
        .btn { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
        .btn-approve { background-color: #28a745; color: white; }
        .btn-reject { background-color: #dc3545; color: white; }
        .btn-back { background-color: #6c757d; color: white; }
        .btn:hover { opacity: 0.8; }
        .action-cell { display: flex; gap: 8px; }
    </style>
</head>
<body>
<div class="container">
    <h2>活动报名列表</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>用户ID</th>
                <th>报名时间</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${applies}" var="apply">
                <tr>
                    <td>${apply.id}</td>
                    <td>${apply.userId}</td>
                    <td>${apply.applyTime}</td>
                    <td>
                        <span class="status-${apply.status}">
                            <c:if test="${apply.status == 'pending'}">待审核</c:if>
                            <c:if test="${apply.status == 'approved'}">已通过</c:if>
                            <c:if test="${apply.status == 'rejected'}">已拒绝</c:if>
                        </span>
                    </td>
                    <td class="action-cell">
                        <c:if test="${apply.status == 'pending'}">
                            <a href="<%= request.getContextPath() %>/activityApply/approve?id=${apply.id}&activityId=${activityId}">
                                <button class="btn btn-approve">通过</button>
                            </a>
                            <a href="<%= request.getContextPath() %>/activityApply/reject?id=${apply.id}&activityId=${activityId}">
                                <button class="btn btn-reject">拒绝</button>
                            </a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <br>
    <a href="<%= request.getContextPath() %>/activity/list">
        <button class="btn btn-back">返回活动列表</button>
    </a>
</div>
</body>
</html>
