package com.icee.controller.admin;

import com.icee.dto.DishDTO;
import com.icee.dto.DishPageQueryDTO;
import com.icee.entity.Dish;
import com.icee.result.PageResult;
import com.icee.result.Result;
import com.icee.service.DishService;
import com.icee.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Tag(name = "菜品管理")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO){
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * 菜品查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "菜品查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult=dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "启用禁用菜品")
    public Result<String> status(@PathVariable Integer status,Long id){
        dishService.status(status,id);
        return Result.success();
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "修改菜品")
    public Result<String> update(@RequestBody DishDTO dishDTO){
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 获取菜品列表
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "获取菜品列表")
    public Result<List<Dish>> list(Integer categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
    	DishVO dishVO = dishService.getById(id);
    	return Result.success(dishVO);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除菜品")
    public Result<String> delete(@RequestParam List<Long> ids){  //TODO 若不使用@RequestParam(SpringMVC),则无法自动封装List集合,只能通过Long[]接收参数(Spring自带)
        dishService.delete(ids);
        return Result.success();
    }
}
