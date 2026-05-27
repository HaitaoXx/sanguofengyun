<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>数据统计 - 社团管理系统</title>
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 20px; background: linear-gradient(135deg, #1a1a1a 0%, #8b0000 100%); min-height: 100vh; }
        .container { max-width: 1400px; margin: 0 auto; }
        h2 { color: #333; margin-bottom: 20px; text-align: center; }
        
        /* 统计卡片 */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            border-radius: 12px;
            padding: 24px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .stat-card .number {
            font-size: 36px;
            font-weight: 700;
            color: #dc143c;
        }
        
        .btn-back {
            display: inline-block;
            padding: 10px 20px;
            background: linear-gradient(135deg, #dc143c 0%, #8b0000 100%);
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 20px;
            text-decoration: none;
            transition: all 0.3s ease;
        }
        
        .btn-back:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(220, 20, 60, 0.3);
        }
        
        .stat-card .label {
            font-size: 14px;
            color: #666;
            margin-top: 8px;
        }
        
        /* 图表区域 */
        .charts-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }
        
        .chart-container {
            background: white;
            border-radius: 12px;
            padding: 24px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .chart-container h3 {
            color: #333;
            margin-bottom: 20px;
            font-size: 16px;
        }
        
        .chart {
            height: 350px;
        }
        
        
    </style>
</head>
<body>
<div class="container">
    <h2>📊 数据统计仪表盘</h2>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="number" id="userCount">0</div>
            <div class="label">用户总数</div>
        </div>
        <div class="stat-card">
            <div class="number" id="clubCount">0</div>
            <div class="label">社团总数</div>
        </div>
        <div class="stat-card">
            <div class="number" id="activityCount">0</div>
            <div class="label">活动总数</div>
        </div>
        <div class="stat-card">
            <div class="number" id="memberCount">0</div>
            <div class="label">成员总数</div>
        </div>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-grid">
        <div class="chart-container">
            <h3>👥 用户角色分布</h3>
            <div class="chart" id="roleChart"></div>
        </div>
        <div class="chart-container">
            <h3>🏢 各社团活动数量</h3>
            <div class="chart" id="clubChart"></div>
        </div>
        <div class="chart-container">
            <h3>📈 月度活动趋势</h3>
            <div class="chart" id="monthChart"></div>
        </div>
        <div class="chart-container">
            <h3>📋 申请统计</h3>
            <div class="chart" id="applyChart"></div>
        </div>
    </div>
    
    <center>
        <a href="<%= request.getContextPath() %>/index" class="btn-back">返回首页</a>
    </center>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // 显示加载状态
    document.getElementById('userCount').textContent = '加载中...';
    document.getElementById('clubCount').textContent = '加载中...';
    document.getElementById('activityCount').textContent = '加载中...';
    document.getElementById('memberCount').textContent = '加载中...';
    
    // 获取统计数据
    fetch('<%= request.getContextPath() %>/statistics/data')
        .then(response => {
            if (!response.ok) {
                throw new Error('网络响应异常: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('获取到的数据:', data);
            
            // 更新统计卡片
            document.getElementById('userCount').textContent = data.userCount || 0;
            document.getElementById('clubCount').textContent = data.clubCount || 0;
            document.getElementById('activityCount').textContent = data.activityCount || 0;
            document.getElementById('memberCount').textContent = data.memberCount || 0;
            
            // 用户角色分布饼图
            var roleChart = echarts.init(document.getElementById('roleChart'));
            var roleData = data.userCountByRole || { admin: 0, leader: 0, student: 0 };
            roleChart.setOption({
                tooltip: { trigger: 'item' },
                legend: { orient: 'vertical', right: 10 },
                series: [{
                    name: '用户角色',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    avoidLabelOverlap: false,
                    itemStyle: { borderRadius: 10 },
                    data: [
                        { value: roleData.admin || 0, name: '管理员' },
                        { value: roleData.leader || 0, name: '社长' },
                        { value: roleData.student || 0, name: '学生' }
                    ],
                    color: ['#dc143c', '#ffc107', '#28a745']
                }]
            });
            
            // 各社团活动数量柱状图
            var clubChart = echarts.init(document.getElementById('clubChart'));
            var clubData = data.activityCountByClub || {};
            var clubNames = Object.keys(clubData);
            var clubCounts = Object.values(clubData);
            
            if (clubNames.length === 0) {
                clubNames = ['暂无数据'];
                clubCounts = [0];
            }
            
            clubChart.setOption({
                tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
                grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
                xAxis: { type: 'category', data: clubNames, axisLabel: { rotate: 30 } },
                yAxis: { type: 'value' },
                series: [{
                    name: '活动数量',
                    type: 'bar',
                    data: clubCounts,
                    itemStyle: { color: '#dc143c', borderRadius: [4, 4, 0, 0] }
                }]
            });
            
            // 月度活动趋势折线图
            var monthChart = echarts.init(document.getElementById('monthChart'));
            var monthData = data.activityCountByMonth || {};
            var months = Object.keys(monthData).sort();
            var monthCounts = months.map(m => monthData[m]);
            
            if (months.length === 0) {
                months = ['暂无数据'];
                monthCounts = [0];
            }
            
            monthChart.setOption({
                tooltip: { trigger: 'axis' },
                grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
                xAxis: { type: 'category', data: months, boundaryGap: false },
                yAxis: { type: 'value' },
                series: [{
                    name: '活动数量',
                    type: 'line',
                    smooth: true,
                    data: monthCounts,
                    areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: 'rgba(220, 20, 60, 0.3)' },
                        { offset: 1, color: 'rgba(220, 20, 60, 0.05)' }
                    ]) },
                    lineStyle: { color: '#dc143c', width: 3 },
                    itemStyle: { color: '#dc143c' }
                }]
            });
            
            // 申请统计饼图
            var applyChart = echarts.init(document.getElementById('applyChart'));
            applyChart.setOption({
                tooltip: { trigger: 'item' },
                legend: { orient: 'vertical', right: 10 },
                series: [{
                    name: '申请统计',
                    type: 'pie',
                    radius: '60%',
                    data: [
                        { value: data.activityApplyCount || 0, name: '活动报名' },
                        { value: data.memberApplyCount || 0, name: '成员申请' }
                    ],
                    color: ['#17a2b8', '#6f42c1']
                }]
            });
            
            // 响应式调整
            window.addEventListener('resize', function() {
                roleChart.resize();
                clubChart.resize();
                monthChart.resize();
                applyChart.resize();
            });
        })
        .catch(error => {
            console.error('获取统计数据失败:', error);
            document.getElementById('userCount').textContent = 'Error';
            document.getElementById('clubCount').textContent = 'Error';
            document.getElementById('activityCount').textContent = 'Error';
            document.getElementById('memberCount').textContent = 'Error';
            alert('获取统计数据失败，请检查控制台错误信息');
        });
});
</script>
</body>
</html>
