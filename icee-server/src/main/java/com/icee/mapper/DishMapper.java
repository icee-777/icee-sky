package com.icee.mapper;

import com.icee.annotation.AutoFill;
import com.icee.entity.Dish;
import com.icee.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(*) from dish where category_id=#{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * @param dish
     */
    @Options(useGeneratedKeys = true,keyProperty = "id")  //TODO 主键回显设置
    @Insert("insert into dish (name,category_id,price,image,description,status,create_time,update_time,create_user,update_user) values (#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


}
