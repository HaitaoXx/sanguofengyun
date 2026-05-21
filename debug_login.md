# 登录问题排查步骤

## 1. 检查数据库连接
- 确认MySQL服务正在运行
- 确认数据库名称 `club_system` 存在
- 确认用户名密码正确：root/123456

## 2. 检查表结构
执行以下SQL确认表存在：
```sql
USE club_system;
SHOW TABLES;
DESC t_user;
```

## 3. 检查用户数据
执行以下SQL查看用户数据：
```sql
SELECT * FROM t_user;
```

## 4. 手动测试登录SQL
```sql
SELECT * FROM t_user WHERE username = 'admin' AND password = '123456' AND role = 'admin';
```

## 5. 常见问题
1. **数据库名称错误** - 确认数据库名是 `club_system`
2. **表不存在** - 执行 `create_tables.sql` 创建表
3. **用户数据未插入** - 执行 `init_data.sql` 插入测试数据
4. **密码大小写** - 确认密码是 `123456`（小写）
5. **角色选择** - 确认选择的角色是 `admin`

## 6. 测试页面
访问 `http://localhost:8080/ssm6/test` 查看调试信息

## 7. 控制台日志
查看Tomcat控制台输出，看是否有SQL错误或异常