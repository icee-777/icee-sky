package com.icee.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.icee.constant.MessageConstant;
import com.icee.context.BaseContext;
import com.icee.dto.OrdersDTO;
import com.icee.dto.OrdersPaymentDTO;
import com.icee.dto.OrdersSubmitDTO;
import com.icee.entity.*;
import com.icee.exception.AddressBookBusinessException;
import com.icee.exception.BaseException;
import com.icee.exception.OrderBusinessException;
import com.icee.exception.ShoppingCartBusinessException;
import com.icee.mapper.*;
import com.icee.result.PageResult;
import com.icee.service.OrderService;
import com.icee.utils.WeChatPayUtil;
import com.icee.vo.OrderPaymentVO;
import com.icee.vo.OrderSubmitVO;
import com.icee.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        AddressBook addressBook=addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList=shoppingCartMapper.getByUserId(userId);
        if(shoppingCartList.isEmpty()){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        List<OrderDetail> orderDetails=new ArrayList<>();
        for(ShoppingCart cart : shoppingCartList){
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetails.add(orderDetail);
        }

        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setUserId(userId);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setTablewareNumber(orderDetails.size());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orderMapper.insert(orders);

        for(OrderDetail orderDetail:orderDetails){
            orderDetail.setOrderId(orders.getId());
        }
        orderDetailMapper.insertBatch(orderDetails);

        shoppingCartMapper.deleteByUserId(userId);

        OrderSubmitVO orderSubmitVO=new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //获取当前订单
        Orders orders = orderMapper.getByNumber(ordersPaymentDTO.getOrderNumber());

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );

        JSONObject jsonObject=new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        orders.setStatus(Orders.CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        //默认立即送出
        orders.setDeliveryStatus(1);
        orderMapper.update(orders);

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 用户端订单详情查询
     * @param id
     * @return
     */
    @Override
    public OrderVO getOrderDetail(Long id) {
        OrderVO orderVO=new OrderVO();
        Orders orders=orderMapper.getById(id);
        BeanUtils.copyProperties(orders,orderVO);
        List<OrderDetail> orderDetails=orderDetailMapper.getByOrderId(id);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    /**
     * 订单取消
     * @param id
     */
    @Override
    public void cancel(Long id) {
        Orders orders=orderMapper.getById(id);
        Integer status=orders.getStatus();
        if(status>2){
            throw new OrderBusinessException("请与店家电话沟通");
        }
        if(status==Orders.PENDING_PAYMENT){
            orders.setStatus(Orders.CANCELLED);
        }else if(status==Orders.TO_BE_CONFIRMED){  //todo 待付款与待接单是两种状态
            orders.setStatus(Orders.CANCELLED);
            orders.setPayStatus(Orders.REFUND);
        }

        //todo 记录取消时间与原因
        orders.setCancelTime(LocalDateTime.now());
        orders.setCancelReason("用户取消");
        orderMapper.update(orders);
    }

    /**
     * 订单历史查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult getHistoryOrders(Integer page, Integer pageSize, Integer status) {
        Long userId = BaseContext.getCurrentId();
        PageHelper.startPage(page,pageSize);
        //todo 给前端应返回OrderVO
        //todo 查询时应额外使用userId
        Page<OrderVO> ordersPage=orderMapper.page(status,userId);
        for(OrderVO orderVO : ordersPage){
            Long currentId=orderVO.getId();
            List<OrderDetail> orderDetailsList=orderDetailMapper.getByOrderId(currentId);
            orderVO.setOrderDetailList(orderDetailsList);
        }
        return new PageResult(ordersPage.getTotal(),ordersPage.getResult());
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void repetition(Long id) {
        //todo 再来一单的逻辑是:将当前订单的菜品再次添加入购物车,由用户决定是否增删改菜品与下单
        Orders orders=orderMapper.getById(id);
        List<OrderDetail> orderDetailList=orderDetailMapper.getByOrderId(id);
        List<ShoppingCart> shoppingCartList=new ArrayList<>();
        for(OrderDetail od : orderDetailList){
            ShoppingCart shoppingCart=new ShoppingCart();
            BeanUtils.copyProperties(orders,shoppingCart);
            BeanUtils.copyProperties(od,shoppingCart);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartList.add(shoppingCart);
        }
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 获取订单的预计剩余时间
     * @param id
     * @return
     */
    @Override
    public Long getTime(Long id) {
        Orders orders = orderMapper.getById(id);
        if(orders==null){
            throw new OrderBusinessException("订单不存在");
        }
        LocalDateTime deliveryTime = orders.getDeliveryTime();
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MINUTES.between(now, deliveryTime);
    }
}
