package com.icee.service;

import com.icee.dto.CategoryDTO;
import com.icee.dto.CategoryPageQueryDTO;
import com.icee.entity.Category;
import com.icee.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    void status(Integer status, Long id);

    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改分类
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
