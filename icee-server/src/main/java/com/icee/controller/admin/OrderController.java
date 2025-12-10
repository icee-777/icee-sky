package com.icee.controller.admin;

import com.icee.dto.OrdersCancelDTO;
import com.icee.dto.OrdersConfirmDTO;
import com.icee.dto.OrdersPageQueryDTO;
import com.icee.dto.OrdersRejectionDTO;
import com.icee.result.PageResult;
import com.icee.result.Result;
import com.icee.service.OrderService;
import com.icee.vo.OrderStatisticsVO;
import com.icee.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Tag(name = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 条件查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @Operation(summary = "条件查询")
    public Result<PageResult> getOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult result=orderService.getOrdersByPage(ordersPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 订单派送
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @Operation(summary = "订单派送")
    public Result<String> delivery(@PathVariable Long id){
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @Operation(summary = "订单详情")
    public Result<OrderVO> getOrderDetails(@PathVariable Long id){
        OrderVO orderVO=orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 接收订单
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    @Operation(summary = "接收订单")
    //todo Put/Post请求需使用DTO接收前端数据
    public Result<String> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒绝订单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @Operation(summary = "拒绝订单")
    public Result<String> reject(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        orderService.reject(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @Operation(summary = "取消订单")
    public Result<String> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO){
        orderService.cancelAdmin(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @Operation(summary = "完成订单")
    public Result<String> complete(@PathVariable Long id){
        orderService.complete(id);
        return Result.success();
    }

    /**
     * 订单统计
     * @return
     */
    @GetMapping("/statistics")
    @Operation(summary = "订单统计")
    public Result<OrderStatisticsVO> getOrderStatistics(){
        OrderStatisticsVO orderStatisticsVO=orderService.getOrderStatistics();
        return Result.success(orderStatisticsVO);
    }


}
