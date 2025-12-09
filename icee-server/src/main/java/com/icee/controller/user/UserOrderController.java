package com.icee.controller.user;

import com.icee.dto.OrdersDTO;
import com.icee.dto.OrdersPaymentDTO;
import com.icee.dto.OrdersSubmitDTO;
import com.icee.result.Result;
import com.icee.service.OrderService;
import com.icee.vo.OrderPaymentVO;
import com.icee.vo.OrderSubmitVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Tag(name = "用户订单接口")
@Slf4j
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @Operation(summary = "用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
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
//        OrderPaymentVO orderPaymentVO=new OrderPaymentVO("icee", "prepay_id=wx1234567890", "2023-05-01 12:00:00", "MD5", "2023-05-01 12:00:00");
        return Result.success(orderPaymentVO);
    }
}
