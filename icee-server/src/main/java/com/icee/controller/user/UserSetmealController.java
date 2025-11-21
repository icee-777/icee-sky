package com.icee.controller.user;

import com.icee.entity.Dish;
import com.icee.entity.Setmeal;
import com.icee.result.Result;
import com.icee.service.SetMealService;
import com.icee.vo.DishItemVO;
import com.icee.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/setmeal")
@Slf4j
@Tag(name = "用户端套餐接口")
public class UserSetmealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 查询套餐
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询套餐")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")
    public Result<List<Setmeal>> list(Long categoryId){
        List<Setmeal> setmealList=setMealService.list(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 查询套餐中的菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @Operation(summary = "查询套餐中的菜品")
    public Result<List<DishItemVO>> dishList(@PathVariable Long id){
        List<DishItemVO> dishList=setMealService.dishList(id);
        return Result.success(dishList);
    }
}
