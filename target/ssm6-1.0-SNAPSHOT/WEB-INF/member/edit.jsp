<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zwj.entity.Member" %>
<%@ page import="com.zwj.entity.User" %>
<%@ page import="com.zwj.entity.Club" %>
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
    <title>编辑成员 - 社团管理系统</title>
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
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
        
        .member-info {
            background: #f8f9ff;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
        }
        
        .member-info p {
            margin: 5px 0;
            color: #666;
        }
    </style>
</head>
<body>
    <a href="<%= request.getContextPath() %>/member/list" class="back-link">
        ← 返回成员列表
    </a>
    
    <div class="container">
        <h1 class="page-title">✏️ 编辑成员</h1>
        
        <%
            Member member = (Member) request.getAttribute("member");
            List<User> users = (List<User>) request.getAttribute("users");
            List<Club> clubs = (List<Club>) request.getAttribute("clubs");
        %>
        
        <div class="form-card">
            <div class="member-info">
                <p><strong>成员ID:</strong> <%= member.getId() %></p>
                <p><strong>当前用户:</strong> <%= member.getUserId() %></p>
                <p><strong>当前社团:</strong> <%= member.getClubId() %></p>
            </div>
            
            <form action="<%= request.getContextPath() %>/member/edit" method="post">
                <input type="hidden" name="id" value="<%= member.getId() %>">
                
                <div class="form-group">
                    <label for="userId">选择用户:</label>
                    <select name="userId" id="userId" required>
                        <option value="">请选择用户</option>
                        <%
                            if (users != null) {
                                for (User user : users) {
                                    boolean selected = user.getId().equals(member.getUserId());
                        %>
                                    <option value="<%= user.getId() %>" <%= selected ? "selected" : "" %>>
                                        <%= user.getUsername() %> (<%= user.getRealname() %>)
                                    </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="clubId">选择社团:</label>
                    <select name="clubId" id="clubId" required>
                        <option value="">请选择社团</option>
                        <%
                            if (clubs != null) {
                                for (Club club : clubs) {
                                    boolean selected = club.getId().equals(member.getClubId());
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
                
                <div class="form-group">
                    <label for="role">角色:</label>
                    <select name="role" id="role" required>
                        <option value="">请选择角色</option>
                        <option value="成员" <%= "成员".equals(member.getRole()) ? "selected" : "" %>>成员</option>
                        <option value="社长" <%= "社长".equals(member.getRole()) ? "selected" : "" %>>社长</option>
                        <option value="管理员" <%= "管理员".equals(member.getRole()) ? "selected" : "" %>>管理员</option>
                    </select>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="btn">✏️ 保存修改</button>
                    <a href="<%= request.getContextPath() %>/member/list" class="btn btn-secondary">取消</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>