package com.icee.service;

import com.icee.dto.DishDTO;
import com.icee.dto.DishPageQueryDTO;
import com.icee.entity.Dish;
import com.icee.result.PageResult;
import com.icee.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param id
     * @param status
     */
    void status(Integer status, Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Integer categoryId);

    /**
     * 根据id查询菜品和对应的口味
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 删除菜品
     * @param ids
     */
    void delete(List<Long> ids);
}
