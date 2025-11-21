package com.icee.mapper;

import com.icee.entity.SetmealDish;
import com.icee.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{setmealId}")
    List<SetmealDish> getById(Long setmealId);

    /**
     * 根据套餐id删除
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 批量删除
     * @param setmealIds
     */
    void delete(List<Long> setmealIds);

    /**
     * 根据套餐id查询菜品id
     * @param setmealId
     * @return
     */
    @Select("select dish_id from setmeal_dish where setmeal_id=#{setmealId}")
    List<Long> getDishIds(Long setmealId);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select sd.copies,d.name,d.description,d.image from dish d left join setmeal_dish sd on d.id = sd.dish_id where sd.setmeal_id=#{setmealId}")
    List<DishItemVO> getMealDish(Long setmealId);
}
