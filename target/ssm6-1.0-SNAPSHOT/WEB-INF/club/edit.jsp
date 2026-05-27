<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>编辑社团 - 社团管理系统</title>
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
            max-width: 800px;
            margin: 0 auto;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .header {
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e9ecef;
        }
        
        .page-title {
            font-size: 32px;
            color: #333;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            padding: 10px 20px;
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .back-link:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(40, 167, 69, 0.4);
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
            font-size: 16px;
        }
        
        .form-control {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s ease;
            font-family: inherit;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        textarea.form-control {
            min-height: 120px;
            resize: vertical;
        }
        
        .btn-submit {
            width: 100%;
            padding: 14px 28px;
            background: linear-gradient(135deg, #ffc107 0%, #ff9800 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(255, 193, 7, 0.3);
        }
        
        .btn-submit:active {
            transform: translateY(0);
        }
        
        .required {
            color: #dc3545;
        }
        
        .help-text {
            font-size: 14px;
            color: #6c757d;
            margin-top: 5px;
        }
        
        .club-id {
            background: #f8f9fa;
            padding: 10px 15px;
            border-radius: 6px;
            font-family: 'Courier New', monospace;
            font-weight: bold;
            color: #6c757d;
            display: inline-block;
        }
        
        .button-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        
        .btn-cancel {
            padding: 14px 28px;
            background: #6c757d;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            text-align: center;
            display: inline-block;
        }
        
        .btn-cancel:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1 class="page-title">✏️ 编辑社团</h1>
    </div>
    
    <a href="<%= request.getContextPath() %>/club/list" class="back-link">🏠 返回社团列表</a>
    
    <%
        Object clubObj = request.getAttribute("club");
        if (clubObj != null) {
            com.zwj.entity.Club club = (com.zwj.entity.Club) clubObj;
    %>
    
    <div style="margin: 20px 0;">
        <span style="font-weight: bold;">社团 ID: </span>
        <span class="club-id">#<%= club.getId() %></span>
    </div>
    
    <form action="<%= request.getContextPath() %>/club/edit" method="post" style="margin-top: 30px;">
        <input type="hidden" name="id" value="<%= club.getId() %>">
        
        <div class="form-group">
            <label for="name">社团名称 <span class="required">*</span></label>
            <input type="text" id="name" name="name" class="form-control" required 
                   value="<%= club.getName() %>" maxlength="100">
            <div class="help-text">社团名称将在系统中显示，建议简洁明了</div>
        </div>
        
        <div class="form-group">
            <label for="intro">社团简介</label>
            <textarea id="intro" name="intro" class="form-control" maxlength="500"><%= club.getIntro() != null ? club.getIntro() : "" %></textarea>
            <div class="help-text">详细介绍社团的宗旨、活动内容等，有助于吸引成员加入</div>
        </div>
        
        <div class="button-group">
            <button type="submit" class="btn-submit">💾 保存修改</button>
            <a href="<%= request.getContextPath() %>/club/list" class="btn-cancel">❌ 取消</a>
        </div>
    </form>
    
    <%
        } else {
    %>
        <div style="text-align: center; padding: 40px; color: #6c757d;">
            <div style="font-size: 64px; margin-bottom: 20px;">⚠️</div>
            <div style="font-size: 18px;">无法加载社团信息</div>
            <a href="<%= request.getContextPath() %>/club/list" class="back-link" style="margin-top: 20px; display: inline-block;">返回社团列表</a>
        </div>
    <%
        }
    %>
</div>

<script>
    // 表单验证
    document.querySelector('form').addEventListener('submit', function(e) {
        const name = document.getElementById('name').value.trim();
        
        if (name === '') {
            e.preventDefault();
            alert('请输入社团名称');
            document.getElementById('name').focus();
            return false;
        }
        
        if (name.length < 2) {
            e.preventDefault();
            alert('社团名称至少需要2个字符');
            document.getElementById('name').focus();
            return false;
        }
        
        if (name.length > 100) {
            e.preventDefault();
            alert('社团名称不能超过100个字符');
            document.getElementById('name').focus();
            return false;
        }
        
        return true;
    });
    
    // 自动调整文本框高度
    const textarea = document.getElementById('intro');
    textarea.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = this.scrollHeight + 'px';
    });
    
    // 初始化文本框高度
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
</script>
</body>
</html>