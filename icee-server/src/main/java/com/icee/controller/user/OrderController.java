package com.icee.controller.user;

import com.icee.dto.OrdersPaymentDTO;
import com.icee.dto.OrdersSubmitDTO;
import com.icee.result.PageResult;
import com.icee.result.Result;
import com.icee.service.OrderService;
import com.icee.vo.OrderPaymentVO;
import com.icee.vo.OrderSubmitVO;
import com.icee.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")  //在spring容器中的别名,与商家端的OrderController区分
@RequestMapping("/user/order")
@Tag(name = "用户订单接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @Operation(summary = "用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) throws Exception {
        OrderSubmitVO orderSubmitVO=orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @Operation(summary = "订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @Operation(summary = "获取订单详情")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id){
        OrderVO orderVo=orderService.getOrderDetail(id);
        return Result.success(orderVo);
    }

    /**
     * 用户取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @Operation(summary = "用户取消订单")
    public Result<String> cancel(@PathVariable Long id){
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * 查询历史订单
     * @param page
     * @param pageSize
     * //todo 枚举参数状态
     * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @return
     */
    @GetMapping("/historyOrders")
    @Operation(summary = "查询历史订单")
    public Result<PageResult> historyOrders(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) Integer status){
        PageResult pageResult=orderService.getHistoryOrders(page,pageSize,status);
        return Result.success(pageResult);
    }

    /**
     * 查询历史订单
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @Operation(summary = "再来一单")
    public Result<String> repetition(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 订单催单
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @Operation(summary = "订单催单")
    public Result<String> reminder(@PathVariable Long id){
        orderService.reminder(id);
        return Result.success();
    }
}
