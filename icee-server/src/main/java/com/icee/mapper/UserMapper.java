package com.icee.mapper;

import com.icee.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {


    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 新增用户
     * @param user
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Select("insert into user (openid, name, phone, sex, id_number, avatar) values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar})")
    void insert(User user);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);
}
