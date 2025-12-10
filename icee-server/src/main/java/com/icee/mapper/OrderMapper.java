package com.icee.mapper;

import com.github.pagehelper.Page;
import com.icee.dto.OrdersPageQueryDTO;
import com.icee.entity.Orders;
import com.icee.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.function.UnaryOperator;

@Mapper
public interface OrderMapper {

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("INSERT INTO orders (number, status, user_id, address_book_id, order_time, checkout_time, pay_method, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status, pay_status) " +
            "VALUES (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, #{payMethod}, #{amount}, #{remark}, #{phone}, #{address}, #{userName}, #{consignee}, #{cancelReason}, #{rejectionReason}, #{cancelTime}, #{estimatedDeliveryTime}, #{deliveryStatus}, #{deliveryTime}, #{packAmount}, #{tablewareNumber}, #{tablewareStatus},#{payStatus})")
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询订单
     * @param status
     * @param userId
     */
    //todo 按主键降序排列(最近的订单在前)
    Page<OrderVO> page(Integer status,Long userId);

    /**
     * 获取订单详情
     * @param ordersPageQueryDTO
     * @return
     */
    //todo 1:当传入单个参数对象时,mybatis会自动映射对象属性值->直接使用变量名 #{phone} 或 <if test="phone">...</if>
    //todo 2：当传入多个参数对象时
    //todo 2.1:不使用@Param注解 -> #{param1.phone} 使用参数位置定位
    //todo 2.2:使用@Param("a")注解 -> #{a.phone}   使用参数名(a)定位
    //todo <![CDATA[ < ]]>:转义字符,避免解析错误
    //todo 应默认使用id降序(先处理新订单)
    Page<OrderVO> pageDto(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计订单数量
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status=#{status}")
    Integer statusCount(Integer status);
}
