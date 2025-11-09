package com.icee.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.icee.constant.StatusConstant;
import com.icee.context.BaseContext;
import com.icee.dto.CategoryDTO;
import com.icee.dto.CategoryPageQueryDTO;
import com.icee.entity.Category;
import com.icee.mapper.CategoryMapper;
import com.icee.mapper.DishMapper;
import com.icee.mapper.SetmealMapper;
import com.icee.result.PageResult;
import com.icee.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        //TODO pageHelper(2.0.0)～SpringBoot(3.x.x)   版本过低则无法正确分页
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page=categoryMapper.page(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 分类状态变更
     * @param status
     * @param id
     */
    @Override
    public void status(Integer status, Long id) {
        Category category=Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update( category);
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category=Category.builder()
                .type(categoryDTO.getType())
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .status(StatusConstant.DISABLE)
                .build();
        categoryMapper.insert(category);
    }

    /**
     * 删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        Integer count = dishMapper.countByCategoryId(id);

        //TODO 只要分类下关联了菜品或者套餐，不能删除
        if(count>0){
            throw new RuntimeException("当前分类下关联了菜品，不能删除");
        }
        count = setmealMapper.countByCategoryId(id);
        if(count>0){
            throw new RuntimeException("当前分类下关联了套餐，不能删除");
        }

        categoryMapper.deleteById(id);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category=Category.builder()
                .id(categoryDTO.getId())
//                .type(categoryDTO.getType())
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
        List<Category> list=categoryMapper.list(type);
        return list;
    }
}
