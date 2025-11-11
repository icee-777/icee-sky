package com.icee.service;

import com.icee.dto.DishDTO;
import com.icee.dto.DishPageQueryDTO;
import com.icee.result.PageResult;

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
}
