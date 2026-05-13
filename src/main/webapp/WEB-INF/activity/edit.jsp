<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.Activity" %>
<%@ page import="com.zwj.entity.Club" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
    <title>编辑活动 - 社团管理系统</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            margin: 0;
            padding: 20px;
            background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%);
            min-height: 100vh;
        }
        
        .container {
            max-width: 700px;
            margin: 0 auto;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .page-title {
            font-size: 28px;
            color: #333;
            margin-bottom: 30px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .back-link {
            color: white;
            text-decoration: none;
            margin-bottom: 20px;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 8px 16px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 20px;
            transition: background 0.3s ease;
        }
        
        .back-link:hover {
            background: rgba(255, 255, 255, 0.3);
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #555;
        }
        
        select, input, textarea {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 15px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
            font-family: inherit;
        }
        
        textarea {
            resize: vertical;
            min-height: 120px;
        }
        
        select:focus, input:focus, textarea:focus {
            outline: none;
            border-color: #dc143c;
            box-shadow: 0 0 0 3px rgba(220, 20, 60, 0.1);
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-size: 15px;
            font-weight: 500;
            transition: all 0.3s ease;
            background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(220, 20, 60, 0.3);
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(220, 20, 60, 0.4);
        }
        
        .btn-secondary {
            background: #6c757d;
            margin-left: 10px;
        }
        
        .btn-secondary:hover {
            background: #5a6268;
            box-shadow: 0 6px 20px rgba(108, 117, 125, 0.4);
        }
        
        .button-group {
            text-align: center;
            margin-top: 30px;
        }
        
        .form-card {
            background: white;
            border-radius: 12px;
            padding: 30px;
            border: 1px solid #f0f0f0;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        
        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }
        }
        
        .activity-info {
            background: #f8f9ff;
            border-left: 4px solid #dc143c;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
        }
        
        .activity-info p {
            margin: 5px 0;
            color: #666;
        }
    </style>
</head>
<body>
    <a href="<%= request.getContextPath() %>/activity/list" class="back-link">
        ← 返回活动列表
    </a>
    
    <div class="container">
        <h1 class="page-title">✏️ 编辑活动</h1>
        
        <%
            Activity activity = (Activity) request.getAttribute("activity");
            List<Club> clubs = (List<Club>) request.getAttribute("clubs");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        %>
        
        <div class="form-card">
            <div class="activity-info">
                <p><strong>活动ID:</strong> <%= activity.getId() %></p>
                <p><strong>当前社团:</strong> <%= activity.getClubId() %></p>
                <p><strong>创建时间:</strong> <%= activity.getCreateTime() %></p>
            </div>
            
            <form action="<%= request.getContextPath() %>/activity/edit" method="post">
                <input type="hidden" name="id" value="<%= activity.getId() %>">
                
                <div class="form-group">
                    <label for="clubId">所属社团:</label>
                    <select name="clubId" id="clubId" required>
                        <option value="">请选择社团</option>
                        <%
                            if (clubs != null) {
                                for (Club club : clubs) {
                                    boolean selected = club.getId().equals(activity.getClubId());
                        %>
                                    <option value="<%= club.getId() %>" <%= selected ? "selected" : "" %>>
                                        <%= club.getName() %>
                                    </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="name">活动名称:</label>
                        <input type="text" name="name" id="name" required value="<%= activity.getName() %>" placeholder="请输入活动名称">
                    </div>
                    
                    <div class="form-group">
                        <label for="location">活动地点:</label>
                        <input type="text" name="location" id="location" required value="<%= activity.getLocation() != null ? activity.getLocation() : "" %>" placeholder="请输入活动地点">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="activityTimeStr">活动时间:</label>
                        <input type="datetime-local" name="activityTimeStr" id="activityTimeStr" required 
                               value="<%= activity.getActivityTime() != null ? dateFormat.format(activity.getActivityTime()) : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label for="duration">活动时长(小时):</label>
                        <input type="number" name="duration" id="duration" min="0.5" step="0.5" 
                               value="<%= activity.getDuration() != null ? activity.getDuration() : "" %>" placeholder="2.0">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="description">活动描述:</label>
                    <textarea name="description" id="description" required placeholder="请详细描述活动内容、安排等信息..."><%= activity.getDescription() != null ? activity.getDescription() : "" %></textarea>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="btn">✏️ 保存修改</button>
                    <a href="<%= request.getContextPath() %>/activity/list" class="btn btn-secondary">取消</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>