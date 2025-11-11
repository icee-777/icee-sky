package com.icee.service.impl;

import com.icee.dto.DishDTO;
import com.icee.entity.Dish;
import com.icee.entity.DishFlavor;
import com.icee.mapper.DishFlavorMapper;
import com.icee.mapper.DishMapper;
import com.icee.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish= Dish.builder()
                .name(dishDTO.getName())
                .categoryId(dishDTO.getCategoryId())
                .price(dishDTO.getPrice())
                .image(dishDTO.getImage())
                .description(dishDTO.getDescription())
                .status(dishDTO.getStatus())
                .build();
        dishMapper.insert(dish);
        //插入菜品偏好设置
        for(DishFlavor dishFlavor:dishDTO.getFlavors()){
            if(dishFlavor.getName()==null|| dishFlavor.getName().equals("")){
                continue;
            }
            //dish主键回显~Mybatis
            dishFlavor.setDishId(dish.getId());
            dishFlavorMapper.insert(dishFlavor);
        }
    }
}
