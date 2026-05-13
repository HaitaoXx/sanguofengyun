package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxa.domain.entity.App;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AppMapper extends BaseMapper<App> {
    
    @Select("SELECT a.*, u.username, u.nickname FROM t_app a " +
            "LEFT JOIN t_user u ON a.user_id = u.id " +
            "WHERE a.status = #{status} " +
            "AND (a.title LIKE CONCAT('%', #{keyword}, '%') OR a.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY a.create_time DESC")
    IPage<App> selectPublishedApps(Page<App> page, @Param("status") Integer status, @Param("keyword") String keyword);
    
    @Select("SELECT * FROM t_app WHERE app_id = #{appId}")
    App selectByAppId(@Param("appId") String appId);
    
    @Insert("INSERT INTO t_app (app_id, user_id, title, description, type, cover_image, config_json, status, view_count, answer_count, share_count, is_ai_generated, create_time, update_time) " +
            "VALUES (#{appId, jdbcType=VARCHAR}, #{userId, jdbcType=BIGINT}, #{title, jdbcType=VARCHAR}, #{description, jdbcType=VARCHAR}, #{type, jdbcType=VARCHAR}, #{coverImage, jdbcType=VARCHAR}, #{configJson, jdbcType=VARCHAR}, #{status, jdbcType=INTEGER}, #{viewCount, jdbcType=INTEGER}, #{answerCount, jdbcType=INTEGER}, #{shareCount, jdbcType=INTEGER}, #{isAiGenerated, jdbcType=INTEGER}, #{createTime, jdbcType=TIMESTAMP}, #{updateTime, jdbcType=TIMESTAMP})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertApp(App app);
}
