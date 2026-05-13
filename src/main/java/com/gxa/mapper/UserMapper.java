package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM t_user WHERE username = #{username} AND status = 1")
    User selectByUsername(@Param("username") String username);
    
    @Select("SELECT COUNT(*) FROM t_user WHERE email = #{email} AND id != #{excludeId}")
    Integer countByEmail(@Param("email") String email, @Param("excludeId") Long excludeId);
    
    @Select("SELECT COUNT(*) FROM t_user WHERE phone = #{phone} AND id != #{excludeId}")
    Integer countByPhone(@Param("phone") String phone, @Param("excludeId") Long excludeId);
}
