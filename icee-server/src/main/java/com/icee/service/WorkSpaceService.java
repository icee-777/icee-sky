package com.icee.service;

import com.icee.vo.BusinessDataVO;
import com.icee.vo.DishOverViewVO;
import com.icee.vo.OrderOverViewVO;
import com.icee.vo.SetmealOverViewVO;

import java.time.LocalDate;


public interface WorkSpaceService {

    /**
     * 获取营业数据
     * @param today
     * @return
     */
    BusinessDataVO getBusinessData(LocalDate today);

    /**
     * 套餐数据
     * @return
     */
    SetmealOverViewVO getSetmealOverView();

    /**
     * 菜品数据
     * @return
     */
    DishOverViewVO getDishOverView();

    /**
     * 订单数据
     * @param today
     * @return
     */
    OrderOverViewVO getOrderData(LocalDate today);
}
