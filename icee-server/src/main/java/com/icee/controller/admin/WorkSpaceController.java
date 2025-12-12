package com.icee.controller.admin;

import com.icee.result.Result;
import com.icee.service.WorkSpaceService;
import com.icee.vo.BusinessDataVO;
import com.icee.vo.DishOverViewVO;
import com.icee.vo.OrderOverViewVO;
import com.icee.vo.SetmealOverViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Tag(name = "WorkSpaceController", description = "工作空间管理")
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 获取营业数据
     * @return
     */
    @RequestMapping("/businessData")
    @Operation(summary = "获取营业数据")
    public Result<BusinessDataVO> getBusinessData(){
        LocalDate today=LocalDate.now();
        BusinessDataVO businessDataVO=workSpaceService.getBusinessData(today);
        return Result.success(businessDataVO);
    }

    /**
     * 获取订单数据
     * @return
     */
    @RequestMapping("/overviewOrders")
    @Operation(summary = "获取订单数据")
    public Result<OrderOverViewVO> getOrderData(){
        LocalDate today=LocalDate.now();
        OrderOverViewVO orderOverViewVO=workSpaceService.getOrderData(today);
        return Result.success(orderOverViewVO);
    }

    /**
     * 套餐数据
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @Operation(summary = "套餐数据")
    public Result<SetmealOverViewVO> getSetmealOverView(){
        SetmealOverViewVO setmealOverViewVO=workSpaceService.getSetmealOverView();
        return Result.success(setmealOverViewVO);
    }

    /**
     * 菜品数据
     * @return
     */
    @GetMapping("/overviewDishes")
    @Operation(summary = "菜品数据")
    public Result<DishOverViewVO> getDishOverView(){
        DishOverViewVO dishOverViewVO=workSpaceService.getDishOverView();
        return Result.success(dishOverViewVO);
    }
}
