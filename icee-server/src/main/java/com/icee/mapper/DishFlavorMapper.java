package com.icee.mapper;

import com.icee.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 删除菜品口味信息
     * @param dishIds
     */
//    @Delete("delete from dish_flavor where dish_id in #{dishIds}")
    public void delete(List<Long> dishIds);

    /**
     * 插入菜品口味数据
     * @param dishFlavor
     */
    @Insert("insert into dish_flavor (dish_id, name, value) values (#{dishId}, #{name}, #{value})")
    public void insert(DishFlavor dishFlavor);

    /**
     * 根据菜品id查询菜品口味数据
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getById(Long dishId);
}
