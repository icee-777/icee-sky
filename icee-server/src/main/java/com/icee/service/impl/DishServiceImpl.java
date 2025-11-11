package com.icee.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.icee.constant.MessageConstant;
import com.icee.constant.StatusConstant;
import com.icee.dto.DishDTO;
import com.icee.dto.DishPageQueryDTO;
import com.icee.entity.Dish;
import com.icee.entity.DishFlavor;
import com.icee.exception.DeletionNotAllowedException;
import com.icee.mapper.DishFlavorMapper;
import com.icee.mapper.DishMapper;
import com.icee.mapper.SetMealDishMapper;
import com.icee.result.PageResult;
import com.icee.service.DishService;
import com.icee.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage=dishMapper.page(dishPageQueryDTO);
        return new PageResult(dishPage.getTotal(),dishPage.getResult());
    }

    /**
     * 菜品状态修改
     * @param status
     * @param id
     */
    @Override
    public void status(Integer status, Long id) {
        Dish dish=Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     */
    @Override
    public List<Dish> list(Integer categoryId) {
        List<Dish> dishList=dishMapper.list(categoryId);
        return dishList;
    }

    /**
     * 根据id查询菜品
     * @param id
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish=dishMapper.getById(id);
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setCategoryName(dishMapper.getCategoryNameById(dishVO.getCategoryId()));
        if(dishVO!=null){
            dishVO.setFlavors(dishFlavorMapper.getById(id));
        }
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish=Dish.builder()
                .id(dishDTO.getId())
                .name(dishDTO.getName())
                .categoryId(dishDTO.getCategoryId())
                .price(dishDTO.getPrice())
                .image(dishDTO.getImage())
                .description(dishDTO.getDescription())
                .status(dishDTO.getStatus())
                .build();
        dishMapper.update(dish);
        dishFlavorMapper.delete(List.of(dishDTO.getId()));
        List<DishFlavor> dishFlavors=dishDTO.getFlavors();
        for(DishFlavor dishFlavor:dishFlavors){
            if(dishFlavor.getName()==null|| dishFlavor.getName().equals("")){
                continue;
            }
            dishFlavor.setDishId(dish.getId());
            dishFlavorMapper.insert(dishFlavor);
        }
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
        for(Long id : ids){
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus()==StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setMealIds = setMealDishMapper.getSetMealIdsByDishIds(ids);
        if(setMealIds!=null && setMealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.delete(ids);
        dishFlavorMapper.delete(ids);
    }
}
