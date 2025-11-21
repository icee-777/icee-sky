package com.icee.mapper;

import com.github.pagehelper.Page;
import com.icee.annotation.AutoFill;
import com.icee.dto.SetmealPageQueryDTO;
import com.icee.entity.Setmeal;
import com.icee.entity.SetmealDish;
import com.icee.enumeration.OperationType;
import com.icee.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param categoryId
     * @return
     */
    @Select("select count(*) from setmeal where category_id=#{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 插入套餐数据
     * @param setMeal
     */
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into setmeal (category_id, name, price, status,description, create_time, update_time, create_user, update_user,  image) VALUES (#{categoryId}, #{name}, #{price}, #{status},#{description}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{image})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setMeal);


    /**
     * 修改套餐数据
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据id查询套餐数据
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除套餐
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据ids和status查询套餐数据
     * @param ids
     * @param status
     * @return
     */
    List<Setmeal> getByIdsAndStatus(List<Long> ids, Integer status);

    /**
     * 根据分类id查询套餐数据
     * @param categoryId
     * @return
     */
    @Select("select * from setmeal where category_id=#{categoryId} and status=1")
    List<Setmeal> list(Long categoryId);
}
