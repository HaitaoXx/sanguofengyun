<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.Activity" %>
<%@ page import="com.zwj.entity.Club" %>
<html>
<head>
    <title>活动管理</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            margin: 20px;
            background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%);
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
        
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            flex-wrap: wrap;
            gap: 15px;
        }
        
        .page-title {
            font-size: 28px;
            color: #333;
            font-weight: 700;
        }
        
        .btn-group {
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
            border: none;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }
        
        .btn-primary { background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%); color: white; }
        .btn-warning { background: #ffc107; color: black; }
        .btn-danger { background: #dc3545; color: white; }
        .btn-success { background: #28a745; color: white; }
        .btn-search { background: #17a2b8; color: white; }
        .btn-reset { background: #6c757d; color: white; }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }
        
        .search-section {
            background: white;
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 30px;
            border: 1px solid #e1e5e9;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }
        
        .search-form {
            display: grid;
            grid-template-columns: 1fr 200px auto;
            gap: 15px;
            align-items: end;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
        }
        
        .form-group label {
            margin-bottom: 8px;
            font-weight: 600;
            color: #555;
        }
        
        .form-group input, .form-group select {
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 15px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        
        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #dc143c;
            box-shadow: 0 0 0 3px rgba(220, 20, 60, 0.1);
        }
        
        .table-container {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }
        
        table { 
            border-collapse: collapse; 
            width: 100%; 
            border-spacing: 0;
        }
        
        th, td { 
            padding: 12px 15px; 
            text-align: left; 
            border-bottom: 1px solid #f0f0f0;
        }
        
        th { 
            background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%);
            color: white;
            font-weight: 600;
        }
        
        tr:hover {
            background-color: #f8f9fa;
        }
        
        .no-permission {
            color: #6c757d; 
            font-size: 12px;
        }
        
        .search-info {
            margin-top: 10px;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="page-header">
        <h1 class="page-title">🎯 活动管理</h1>
        <div class="btn-group">
<%
    Object userInfo = request.getSession().getAttribute("user");
    if (userInfo != null && ("admin".equals(((com.zwj.entity.User)userInfo).getRole()) || "leader".equals(((com.zwj.entity.User)userInfo).getRole()))) {
%>
            <a href="<%= request.getContextPath() %>/activity/add" class="btn btn-success">➕ 添加活动</a>
<%
    }
%>
            <a href="<%= request.getContextPath() %>/index" class="btn btn-primary">🏠 返回首页</a>
        </div>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-section">
        <form action="<%= request.getContextPath() %>/activity/list" method="get" class="search-form">
            <div class="form-group">
                <label for="keyword">搜索关键词:</label>
                <input type="text" id="keyword" name="keyword" 
                       value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>" 
                       placeholder="请输入活动标题、内容或地点">
            </div>
            
            <div class="form-group">
                <label for="clubId">筛选社团:</label>
                <select id="clubId" name="clubId">
                    <option value="">全部社团</option>
                    <%
                        List<Club> clubs = (List<Club>) request.getAttribute("clubs");
                        Integer selectedClubId = (Integer) request.getAttribute("selectedClubId");
                        if (clubs != null) {
                            for (Club club : clubs) {
                                boolean selected = selectedClubId != null && club.getId().equals(selectedClubId);
                    %>
                                <option value="<%= club.getId() %>" <%= selected ? "selected" : "" %>><%= club.getName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn btn-search">🔍 搜索</button>
                <a href="<%= request.getContextPath() %>/activity/list" class="btn btn-reset">🔄 重置</a>
            </div>
        </form>
        <%
            String keyword = (String) request.getAttribute("keyword");
            if (keyword != null && !keyword.trim().isEmpty()) {
        %>
            <div class="search-info">搜索关键词: "<%= keyword %>"</div>
        <%
            }
        %>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>社团</th>
                    <th>标题</th>
                    <th>内容</th>
                    <th>活动时间</th>
                    <th>地点</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
    <%
        List<Activity> activities = (List<Activity>) request.getAttribute("activities");
        clubs = (List<Club>) request.getAttribute("clubs");
        if (activities != null && clubs != null) {
            for (Activity activity : activities) {
    %>
                <tr>
                    <td><%= activity.getId() %></td>
                    <td>
                        <%
                            for (Club club : clubs) {
                                if (club.getId().equals(activity.getClubId())) {
                                    out.println(club.getName());
                                    break;
                                }
                            }
                        %>
                    </td>
                    <td><%= activity.getTitle() %></td>
                    <td>
                        <%= activity.getContent() != null && activity.getContent().length() > 50 ? 
                           activity.getContent().substring(0, 50) + "..." : 
                           (activity.getContent() != null ? activity.getContent() : "") %>
                    </td>
                    <td><%= activity.getActivityTime() %></td>
                    <td><%= activity.getLocation() != null ? activity.getLocation() : "" %></td>
                    <td><%= activity.getCreateTime() %></td>
                    <td>
                        <div class="btn-group">
                <%
                            if (userInfo != null && ("admin".equals(((com.zwj.entity.User)userInfo).getRole()) || "leader".equals(((com.zwj.entity.User)userInfo).getRole()))) {
                %>
                                <a href="<%= request.getContextPath() %>/activity/edit/<%= activity.getId() %>" class="btn btn-warning">✏️ 编辑</a>
                                <a href="<%= request.getContextPath() %>/activity/delete/<%= activity.getId() %>" 
                                   class="btn btn-danger" 
                                   onclick="return confirm('确定要删除这个活动吗？')">🗑️ 删除</a>
                <%
                            } else {
                %>
                                <span class="no-permission">无操作权限</span>
                <%
                            }
                %>
                        </div>
                    </td>
                </tr>
    <%
            }
        } else {
    %>
                <tr>
                    <td colspan="8" style="text-align: center; padding: 40px; color: #666;">
                        <%= (keyword != null && !keyword.trim().isEmpty()) ? "未找到匹配的活动" : "暂无活动数据" %>
                    </td>
                </tr>
    <%
        }
    %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>