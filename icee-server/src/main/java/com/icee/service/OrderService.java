package com.icee.service;

import com.icee.dto.OrdersDTO;
import com.icee.dto.OrdersPaymentDTO;
import com.icee.dto.OrdersSubmitDTO;
import com.icee.vo.OrderPaymentVO;
import com.icee.vo.OrderSubmitVO;

public interface OrderService {
    /**
     * 提交订单
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);
}
