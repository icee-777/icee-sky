package com.icee.service;

import com.icee.dto.DishDTO;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);
}
