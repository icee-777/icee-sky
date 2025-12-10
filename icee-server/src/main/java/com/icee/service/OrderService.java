package com.icee.service;

import com.icee.dto.OrdersDTO;
import com.icee.dto.OrdersPaymentDTO;
import com.icee.dto.OrdersSubmitDTO;
import com.icee.result.PageResult;
import com.icee.vo.OrderPaymentVO;
import com.icee.vo.OrderSubmitVO;
import com.icee.vo.OrderVO;

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

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    OrderVO getOrderDetail(Long id);

    /**
     * 订单取消
     * @param id
     */
    void cancel(Long id);

    /**
     * 获取历史订单信息
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult getHistoryOrders(Integer page, Integer pageSize, Integer status);

    /**
     * 订单重复点餐
     * @param id
     */
    void repetition(Long id);

    /**
     * 获取订单的预计剩余时间
     * @param id
     * @return
     */
    Long getTime(Long id);
}
