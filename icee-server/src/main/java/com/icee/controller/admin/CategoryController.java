package com.icee.controller.admin;

import com.icee.dto.CategoryDTO;
import com.icee.dto.CategoryPageQueryDTO;
import com.icee.entity.Category;
import com.icee.result.PageResult;
import com.icee.result.Result;
import com.icee.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult=categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "启用禁用分类")
    public Result<String> status(@PathVariable Integer status,Long id){
        categoryService.status(status,id);
        return Result.success();
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除分类")
    public Result<String> deleteById(Long id){
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 编辑分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "编辑分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询分类")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
