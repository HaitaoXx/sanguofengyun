<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>编辑用户 - 社团管理系统</title>
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
            max-width: 600px;
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
        
        select, input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 15px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        
        select:focus, input:focus {
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
        
        .user-info {
            background: #f8f9ff;
            border-left: 4px solid #dc143c;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
        }
        
        .user-info p {
            margin: 5px 0;
            color: #666;
        }
        
        .password-hint {
            font-size: 12px;
            color: #999;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <a href="<%= request.getContextPath() %>/user/list" class="back-link">
        ← 返回用户列表
    </a>
    
    <div class="container">
        <h1 class="page-title">✏️ 编辑用户</h1>
        
        <%
            User user = (User) request.getAttribute("user");
        %>
        
        <div class="form-card">
            <div class="user-info">
                <p><strong>用户ID:</strong> <%= user.getId() %></p>
                <p><strong>创建时间:</strong> <%= user.getCreateTime() %></p>
            </div>
            
            <form action="<%= request.getContextPath() %>/user/edit" method="post">
                <input type="hidden" name="id" value="<%= user.getId() %>">
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="username">用户名:</label>
                        <input type="text" name="username" id="username" required value="<%= user.getUsername() %>" placeholder="请输入用户名">
                    </div>
                    
                    <div class="form-group">
                        <label for="password">密码:</label>
                        <input type="password" name="password" id="password" placeholder="留空则不修改密码">
                        <div class="password-hint">留空则不修改密码</div>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="name">真实姓名:</label>
                        <input type="text" name="name" id="name" required value="<%= user.getName() %>" placeholder="请输入真实姓名">
                    </div>
                    
                    <div class="form-group">
                        <label for="role">角色:</label>
                        <select name="role" id="role" required>
                            <option value="">请选择角色</option>
                            <option value="admin" <%= "admin".equals(user.getRole()) ? "selected" : "" %>>管理员</option>
                            <option value="leader" <%= "leader".equals(user.getRole()) ? "selected" : "" %>>社长</option>
                            <option value="student" <%= "student".equals(user.getRole()) ? "selected" : "" %>>学生</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="clubId">所属社团:</label>
                    <select name="clubId" id="clubId">
                        <option value="">暂不分配社团</option>
                        <option value="1" <%= user.getClubId() != null && user.getClubId().equals(1) ? "selected" : "" %>>示例社团</option>
                        <!-- 可以动态加载社团列表 -->
                    </select>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="btn">✏️ 保存修改</button>
                    <a href="<%= request.getContextPath() %>/user/list" class="btn btn-secondary">取消</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>