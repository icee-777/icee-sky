package com.icee.task;

import com.icee.entity.Orders;
import com.icee.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class TestTask {

    @Autowired
    private OrderMapper orderMapper;


    /**
     * 测试定时任务
     */
    //todo 需在启动类中添加 @EnableScheduling
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void executeTask(){
//        log.info("执行定时任务:{}", LocalDateTime.now());
//    }


    /**
     * 每分钟检测一次订单支付超时
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    //todo 更新原子化
    @Transactional(rollbackFor = Exception.class)
    public void payTimeOut(){
        //todo 获取超时订单的id列表
        List<Long> payTimeOutList=orderMapper.getPayTimeOutOrders();
        if(payTimeOutList.isEmpty()){
            return;
        }
        log.info("超时订单:{}",payTimeOutList);
        //todo 通过id列表批量更新 ->  where id in (id1,id2,id3)
        //todo update更新字段间使用,分割
        orderMapper.updatePayTimeOut(payTimeOutList);
//        for(Long id : payTimeOutList){
//            Orders orders=new Orders();
//            orders.setId(id);
//            orders.setStatus(Orders.CANCELLED);
//            orders.setCancelTime(LocalDateTime.now());
//            orders.setCancelReason("超时未支付");
//            orderMapper.update(orders);
//        }
    }

    /**
     * 定时任务,每天凌晨一点将派送中的订单转变为完成状态
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void updateDeliveryStatus(){
        List<Long> deliveryList=orderMapper.getDeliveryOrders();
        if(deliveryList.isEmpty()){
            return;
        }
        log.info("需更新的派送中订单{}:",deliveryList);
        orderMapper.updateDelivery(deliveryList);
    }
}
