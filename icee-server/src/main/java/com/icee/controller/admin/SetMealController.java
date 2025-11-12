package com.icee.controller.admin;

import com.icee.dto.SetmealDTO;
import com.icee.dto.SetmealPageQueryDTO;
import com.icee.result.PageResult;
import com.icee.result.Result;
import com.icee.service.SetMealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Tag(name = "套餐管理")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setMealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增套餐")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO){
        setMealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * 启用禁用套餐
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "启用禁用套餐")
    public Result<String> status(@PathVariable Integer status,Long id){
        setMealService.status(status,id);
        return Result.success();
    }

    /**
     * 编辑套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "编辑套餐")
    public Result<String> update(@RequestBody SetmealDTO setmealDTO){
        setMealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询套餐")
    public Result<SetmealDTO> getById(@PathVariable Long id){
        SetmealDTO setmealDTO=setMealService.getById(id);
        return Result.success(setmealDTO);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除套餐")
    public Result<String> delete(@RequestParam List<Long> ids){
        setMealService.delete(ids);
        return Result.success();
    }
}
