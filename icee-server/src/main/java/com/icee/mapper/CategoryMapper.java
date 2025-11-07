package com.icee.mapper;

import com.github.pagehelper.Page;
import com.icee.dto.CategoryPageQueryDTO;
import com.icee.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 插入数据
     * @param categoryPageQueryDTO
     */
    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改状态
     * @param status
     * @param id
     */
    @Update("update category set status=#{status} where id=#{id}")
    void status(Integer status, Long id);

    /**
     * 新增分类
     * @param category
     */
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) VALUES" +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Category category);

    /**
     * 删除分类
     * @param id
     */
    @Delete("delete from category where id=#{id}")
    void deleteById(Long id);

    /**
     * 修改分类
     * @param category
     */
//    @Update("update category set name=#{name},sort=#{sort},update_time=#{updateTime},update_user=#{updateUser} where id=#{id}")
    void update(Category category);

    /**
     * 查询
     * @param type
     * @return
     */
//TODO status为1才显示    @Select("select * from category where type=#{type}")
    List<Category> list(Integer type);
}
