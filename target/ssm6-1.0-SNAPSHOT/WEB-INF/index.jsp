<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>社团管理系统 - 首页</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        
        .header {
            background: rgba(255, 255, 255, 0.95);
            padding: 20px 0;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .header-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .logo {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        
        .welcome-text {
            color: #666;
            font-size: 16px;
        }
        
        .logout-btn {
            padding: 8px 20px;
            background-color: #dc3545;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        
        .logout-btn:hover {
            background-color: #c82333;
        }
        
        .main-container {
            flex: 1;
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
            width: 100%;
        }
        
        .welcome-section {
            background: rgba(255, 255, 255, 0.95);
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
            margin-bottom: 40px;
        }
        
        .welcome-title {
            font-size: 36px;
            color: #333;
            margin-bottom: 10px;
        }
        
        .welcome-subtitle {
            font-size: 18px;
            color: #666;
            margin-bottom: 30px;
        }
        
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }
        
        .menu-card {
            background: white;
            padding: 30px;
            border-radius: 12px;
            text-align: center;
            text-decoration: none;
            color: #333;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            border: 2px solid transparent;
        }
        
        .menu-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
            border-color: #dc143c;
        }
        
        .menu-icon {
            font-size: 48px;
            margin-bottom: 15px;
        }
        
        .menu-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 8px;
            color: #333;
        }
        
        .menu-description {
            font-size: 14px;
            color: #666;
            line-height: 1.4;
        }
        
        .admin-card { border-left: 4px solid #28a745; }
        .leader-card { border-left: 4px solid #007bff; }
        .student-card { border-left: 4px solid #ffc107; }
        
        .guest-section {
            text-align: center;
            background: rgba(255, 255, 255, 0.95);
            padding: 60px 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .guest-title {
            font-size: 42px;
            color: #333;
            margin-bottom: 20px;
        }
        
        .guest-description {
            font-size: 18px;
            color: #666;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .login-btn {
            display: inline-block;
            padding: 15px 40px;
            background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%);
            color: white;
            text-decoration: none;
            border-radius: 50px;
            font-size: 18px;
            font-weight: bold;
            transition: all 0.3s ease;
        }
        
        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(220, 20, 60, 0.4);
        }
        
        .role-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: bold;
            margin-left: 10px;
        }
        
        .admin-badge {
            background-color: #28a745;
            color: white;
        }
        
        .leader-badge {
            background-color: #007bff;
            color: white;
        }
        
        .student-badge {
            background-color: #ffc107;
            color: #333;
        }
    </style>
</head>
<body>
<%
    Object user = session.getAttribute("user");
    if (user != null) {
        com.zwj.entity.User userInfo = (com.zwj.entity.User) user;
        String roleText = "";
        String roleClass = "";
        String roleIcon = "";
        
        if ("admin".equals(userInfo.getRole())) {
            roleText = "管理员";
            roleClass = "admin-badge";
            roleIcon = "👨‍💼";
        } else if ("leader".equals(userInfo.getRole())) {
            roleText = "社长";
            roleClass = "leader-badge";
            roleIcon = "👑";
        } else if ("student".equals(userInfo.getRole())) {
            roleText = "学生";
            roleClass = "student-badge";
            roleIcon = "🎓";
        }
%>
        <header class="header">
            <div class="header-content">
                <div class="logo">🏛️ 社团管理系统</div>
                <div class="user-info">
                    <div>
                        <span class="welcome-text">
                            <%= roleIcon %> 欢迎回来，<%= userInfo.getName() %>
                            <span class="role-badge <%= roleClass %>"><%= roleText %></span>
                        </span>
                    </div>
                    <a href="<%= request.getContextPath() %>/login/logout" class="logout-btn">退出登录</a>
                </div>
            </div>
        </header>
        
        <main class="main-container">
            <div class="welcome-section">
                <h1 class="welcome-title">欢迎使用社团管理系统</h1>
                <p class="welcome-subtitle">高效管理社团活动，丰富校园文化生活</p>
                
                <div class="menu-grid">
<%
        if ("admin".equals(userInfo.getRole())) {
%>
                    <a href="<%= request.getContextPath() %>/club/list" class="menu-card admin-card">
                        <div class="menu-icon">🏢</div>
                        <div class="menu-title">社团管理</div>
                        <div class="menu-description">创建、编辑和管理所有社团信息</div>
                    </a>
                    <a href="<%= request.getContextPath() %>/member/list" class="menu-card admin-card">
                        <div class="menu-icon">👥</div>
                        <div class="menu-title">成员管理</div>
                        <div class="menu-description">查看和管理所有社团成员</div>
                    </a>
                    <a href="<%= request.getContextPath() %>/activity/list" class="menu-card admin-card">
                        <div class="menu-icon">📅</div>
                        <div class="menu-title">活动管理</div>
                        <div class="menu-description">管理所有社团的活动安排</div>
                    </a>
                    <a href="<%= request.getContextPath() %>/user/list" class="menu-card admin-card">
                        <div class="menu-icon">👤</div>
                        <div class="menu-title">用户管理</div>
                        <div class="menu-description">管理系统用户和权限分配</div>
                    </a>
<%      } else if ("leader".equals(userInfo.getRole())) {
%>
                    <a href="<%= request.getContextPath() %>/club/myClub" class="menu-card leader-card">
                        <div class="menu-icon">🏆</div>
                        <div class="menu-title">我的社团</div>
                        <div class="menu-description">查看和管理社团基本信息</div>
                    </a>
                    <a href="<%= request.getContextPath() %>/member/clubMembers" class="menu-card leader-card">
                        <div class="menu-icon">👥</div>
                        <div class="menu-title">成员管理</div>
                        <div class="menu-description">管理社团成员和权限设置</div>
                    </a>
                    <a href="<%= request.getContextPath() %>/activity/clubActivities" class="menu-card leader-card">
                        <div class="menu-icon">📅</div>
                        <div class="menu-title">活动管理</div>
                        <div class="menu-description">创建和管理社团活动</div>
                    </a>
<%      } else if ("student".equals(userInfo.getRole())) {
%>
                    <a href="<%= request.getContextPath() %>/club/list" class="menu-card student-card">
                        <div class="menu-icon">🔍</div>
                        <div class="menu-title">社团列表</div>
                        <div class="menu-description">浏览所有社团信息</div>
                    </a>
                    <a href="<%= request.getContextPath() %>/club/myClubs" class="menu-card student-card">
                        <div class="menu-icon">🎯</div>
                        <div class="menu-title">我的社团</div>
                        <div class="menu-description">查看已加入的社团</div>
                    </a>
                    <a href="<%= request.getContextPath() %>/activity/list" class="menu-card student-card">
                        <div class="menu-icon">🎉</div>
                        <div class="menu-title">活动列表</div>
                        <div class="menu-description">查看社团活动和报名参加</div>
                    </a>
<%      }
%>
                </div>
            </div>
        </main>
<%  } else {
%>
        <main class="main-container">
            <div class="guest-section">
                <h1 class="guest-title">🏛️ 社团管理系统</h1>
                <p class="guest-description">
                    欢迎来到大学社团管理系统！<br>
                    这里是管理社团活动、组织校园文化活动的重要平台<br>
                    加入我们，让大学生活更加丰富多彩！
                </p>
                <a href="<%= request.getContextPath() %>/login" class="login-btn">立即登录</a>
            </div>
        </main>
<%  }
%>
</body>
</html>