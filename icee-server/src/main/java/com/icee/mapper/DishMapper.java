package com.icee.mapper;

import com.github.pagehelper.Page;
import com.icee.annotation.AutoFill;
import com.icee.dto.DishPageQueryDTO;
import com.icee.entity.Dish;
import com.icee.enumeration.OperationType;
import com.icee.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(*) from dish where category_id=#{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * @param dish
     */
    @Options(useGeneratedKeys = true,keyProperty = "id")  //TODO 主键回显设置
    @Insert("insert into dish (name,category_id,price,image,description,status,create_time,update_time,create_user,update_user) values (#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 更新菜品
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> list(Integer categoryId);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
//    @Select("select d.* , c.name as categoryName from dish d left join category c on d.category_id=c.id where d.id=#{id}")
    @Select("select * from dish where id= #{id}")   //TODO mapper返回的应是实体对象Dish,而不是DishVO
    Dish getById(Long id);

    /**
     * 删除菜品
     * @param ids
     */
//    @Delete("delete from dish where id in #{ids}")
    void delete(List<Long> ids);   //TODO 遍历数组元素应在xml文件中使用<foreach>标签

    /**
     * 根据id查询菜品分类名称
     * @param id
     * @return
     */
    @Select("select name as categoryName from category where id= #{id}")
    String getCategoryNameById(Long id);

    /**
     * 根据id批量查询菜品
     * @param dishIds
     * @return
     */
    List<Dish> getByIds(List<Long> dishIds);
}
