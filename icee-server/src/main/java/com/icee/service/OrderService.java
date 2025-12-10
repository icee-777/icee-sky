package com.icee.service;

import com.icee.dto.*;
import com.icee.result.PageResult;
import com.icee.vo.OrderPaymentVO;
import com.icee.vo.OrderStatisticsVO;
import com.icee.vo.OrderSubmitVO;
import com.icee.vo.OrderVO;

public interface OrderService {
    /**
     * 提交订单
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) throws Exception;

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

    /**
     * 获取订单分页数据
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult getOrdersByPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单派送
     * @param id
     */
    void delivery(Long id);

    /**
     * 订单接收
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 订单拒绝
     * @param ordersRejectionDTO
     */
    void reject(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 管理端取消订单
     * @param ordersCancelDTO
     */
    void cancelAdmin(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单完成
     * @param id
     */
    void complete(Long id);

    /**
     * 获取订单统计数据
     * @return
     */
    OrderStatisticsVO getOrderStatistics();
}
