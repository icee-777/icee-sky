package com.icee.controller.user;

import com.icee.result.Result;
import com.icee.service.DishService;
import com.icee.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/dish")
@Tag(name = "C端-用户端-菜品接口")
@Slf4j
public class UserDishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类查询
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "根据分类查询")
    public Result<List<DishVO>> list(Long categoryId){
        //查询redis是否存在数据
        //TODO 放入redis为什么类型,则取到时候也用此类型
        List<DishVO> redisVOList = (List<DishVO>) redisTemplate.opsForValue().get("dish_" + categoryId);
        if(redisVOList!=null&&!redisVOList.isEmpty()){
            return Result.success(redisVOList);
        }
        List<DishVO> dishVOList=dishService.dishVOList(categoryId);
        redisTemplate.opsForValue().set("dish_"+categoryId,dishVOList);
        return Result.success(dishVOList);
    }
}
