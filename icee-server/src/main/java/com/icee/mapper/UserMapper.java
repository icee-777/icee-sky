package com.icee.mapper;

import com.icee.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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
    @Select("insert into user (openid, name, phone, sex, id_number, avatar, create_time) values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    void insert(User user);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * 查询所有用户
     * @return
     */
    @Select("select * from user where create_time between #{startTime} and #{endTime} order by id asc")
    List<User> getAllUser(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取某个日期之前的用户总量
     * @param startTime
     * @return
     */
    @Select("select count(*) from user where create_time < #{startTime}")
    //todo  <![CDATA[<]]>是xml特有的CDATA标签，用于防止SQL注入,不能在注解方式中使用
    Long getStartUserCount(LocalDateTime startTime);

    /**
     * 获取某个时间段内新增的用户数量
     * @param begin
     * @param end
     * @return
     */
    @Select("select count(*) from user where create_time between #{begin} and #{end}")
    Integer getNewUserCount(LocalDateTime begin, LocalDateTime end);
}
