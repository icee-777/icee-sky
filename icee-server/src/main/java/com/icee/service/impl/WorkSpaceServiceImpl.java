package com.icee.service.impl;

import com.icee.entity.Dish;
import com.icee.entity.Orders;
import com.icee.entity.Setmeal;
import com.icee.mapper.DishMapper;
import com.icee.mapper.OrderMapper;
import com.icee.mapper.SetmealMapper;
import com.icee.mapper.UserMapper;
import com.icee.service.WorkSpaceService;
import com.icee.vo.BusinessDataVO;
import com.icee.vo.DishOverViewVO;
import com.icee.vo.OrderOverViewVO;
import com.icee.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 工作台数据
     * @param today
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDate today) {
        LocalDateTime begin=today.atStartOfDay();
        LocalDateTime end=today.atTime(23,59,59);
        List<Orders> ordersList=orderMapper.getAllOrder(begin,end);
        int validCount=0,total=ordersList.size();
        BigDecimal turnOver=new BigDecimal(0);
        for(Orders orders : ordersList){
            if(orders.getStatus()==5){
                turnOver=turnOver.add(orders.getAmount());
                validCount++;
            }
        }
        double unitPrice=0.0,orderCompletionRate=0.0;
        if(total!=0){
            orderCompletionRate=(double)validCount/total;
        }
        if(validCount!=0){
            unitPrice=turnOver.divide(new BigDecimal(validCount)).doubleValue();
        }

        Integer newUser=userMapper.getNewUserCount(begin,end);

        return BusinessDataVO.builder()
                .turnover(turnOver.doubleValue())
                .validOrderCount(validCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUser)
                .build();
    }

    /**
     * 套餐数据
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        int sold=0,discontinued=0;
        List<Setmeal> setmealList=setmealMapper.getAllSetmeal();
        for(Setmeal setmeal : setmealList){
            if(setmeal.getStatus()==1){
                sold++;
            }else{
                discontinued++;
            }
        }
        return new SetmealOverViewVO().builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 菜品数据
     * @return
     */
    @Override
    public DishOverViewVO getDishOverView() {
        List<Dish> dishList=dishMapper.getAllDish();
        int sold=0,discontinued=0;
        for(Dish dish : dishList){
            if(dish.getStatus()==1){
                sold++;
            }else{
                discontinued++;
            }
        }
        return new DishOverViewVO().builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 订单数据
     * @param today
     * @return
     */
    @Override
    public OrderOverViewVO getOrderData(LocalDate today) {
        LocalDateTime begin=today.atStartOfDay();
        LocalDateTime end=today.atTime(23,59,59);
        List<Orders> ordersList=orderMapper.getAllOrder(begin,end);
        int waitingOrders=0,deliveredOrders=0,completedOrders=0,cancelledOrders=0,allOrders=ordersList.size();
        for(Orders orders : ordersList){
            switch (orders.getStatus()){
                case 2:
                    waitingOrders++;
                    break;
                case 3:
                    deliveredOrders++;
                    break;
                case 5:
                    completedOrders++;
                    break;
                case 6:
                    cancelledOrders++;
                    break;
            }
        }
        return new OrderOverViewVO().builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }
}
