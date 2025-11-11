package com.icee.mapper;

import com.icee.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入菜品口味数据
     * @param dishFlavor
     */
    @Insert("insert into dish_flavor (dish_id, name, value) values (#{dishId}, #{name}, #{value})")
    public void insert(DishFlavor dishFlavor);
}
