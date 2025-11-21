package com.icee.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.icee.context.BaseContext;
import com.icee.dto.ShoppingCartDTO;
import com.icee.entity.Dish;
import com.icee.entity.Setmeal;
import com.icee.entity.ShoppingCart;
import com.icee.mapper.DishMapper;
import com.icee.mapper.SetmealMapper;
import com.icee.mapper.ShoppingCartMapper;
import com.icee.service.SetMealService;
import com.icee.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void save(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart cart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,cart);
        cart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList=shoppingCartMapper.getByCart(cart);
        if(shoppingCartList==null||shoppingCartList.isEmpty()){ //不存在
            ShoppingCart shoppingCart=ShoppingCart.builder()
                    .userId(BaseContext.getCurrentId())
                    .dishId(shoppingCartDTO.getDishId())
                    .dishFlavor(shoppingCartDTO.getDishFlavor())
                    .setmealId(shoppingCartDTO.getSetmealId())
                    .number(1)
                    .createTime(LocalDateTime.now())
                    .build();
            if(shoppingCartDTO.getDishId()==null){  //是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }else{  //是菜品
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }
            shoppingCartMapper.insert(shoppingCart);
        }else{
            ShoppingCart shoppingCart=shoppingCartList.get(0);
            Integer number = shoppingCart.getNumber()+1;
            shoppingCart.setNumber(number);
            shoppingCartMapper.update(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId=BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList=shoppingCartMapper.list(userId);
        return shoppingCartList;
    }

    /**
     * 删除购物车
     * @param shoppingCartDTO
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        Long userId = BaseContext.getCurrentId();
        if(dishId==null){
            ShoppingCart shoppingCart=setmealMapper.getBySetmeal(setmealId,userId);
            Integer number = shoppingCart.getNumber()-1;
            if(number<=0){
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else{
                shoppingCart.setNumber(number);
                shoppingCartMapper.update(shoppingCart);
            }
        }else{
            String dishFlavor = shoppingCartDTO.getDishFlavor();
            ShoppingCart shoppingCart=shoppingCartMapper.getByDish(dishId,dishFlavor,userId);
            Integer number = shoppingCart.getNumber()-1;
            if(number<=0){
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else{
                shoppingCart.setNumber(number);
                shoppingCartMapper.update(shoppingCart);
            }
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void deleteAll() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteAll(userId);
    }
}
