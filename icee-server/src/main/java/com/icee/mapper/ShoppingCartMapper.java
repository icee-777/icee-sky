package com.icee.mapper;

import com.icee.dto.ShoppingCartDTO;
import com.icee.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据DTO查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> getByCart(ShoppingCart shoppingCart);

    /**
     * 添加购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name,user_id,dish_id,setmeal_id,dish_flavor,number,amount,image,create_time) values(#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 修改购物车
     * @param shoppingCart
     */
    @Insert("update shopping_cart set number=#{number} where id=#{id}")
    void update(ShoppingCart shoppingCart);

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id=#{userId}")
    List<ShoppingCart> list(Long userId);

    /**
     * 根据菜品id查询购物车
     * @param dishId
     * @param dishFlavor
     * @param userId
     * @return
     */
//    @Select("select * from shopping_cart where user_id=#{userId} and dish_id=#{dishId} and dish_flavor=#{dishFlavor}")
    ShoppingCart getByDish(Long dishId, String dishFlavor, Long userId);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void deleteAll(Long userId);

    /**
     * 删除购物车
     * @param id
     */
    @Delete("delete from shopping_cart where id= #{id}")
    void deleteById(Long id);
}
