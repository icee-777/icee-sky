package com.icee.mapper;

import com.icee.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单详情数据
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id=#{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);

    /**
     * 查询订单的Top10商品
     * @param orderIdList
     * @return
     */
    //todo 分组查询中只能查询划分元素或聚合函数
    List<OrderDetail> getTop10ByOrderIds(List<Long> orderIdList);
}
