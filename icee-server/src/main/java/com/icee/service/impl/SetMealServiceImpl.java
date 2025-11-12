package com.icee.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.icee.constant.MessageConstant;
import com.icee.constant.StatusConstant;
import com.icee.dto.SetmealDTO;
import com.icee.dto.SetmealPageQueryDTO;
import com.icee.entity.Dish;
import com.icee.entity.Setmeal;
import com.icee.entity.SetmealDish;
import com.icee.exception.BaseException;
import com.icee.exception.DeletionNotAllowedException;
import com.icee.mapper.DishMapper;
import com.icee.mapper.SetMealDishMapper;
import com.icee.mapper.SetmealMapper;
import com.icee.result.PageResult;
import com.icee.service.SetMealService;
import com.icee.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);
        for(SetmealVO setmealVO : page){
            setmealVO.setSetmealDishes(setMealDishMapper.getBySetmealId(setmealVO.getId()));
        }
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setMeal= Setmeal.builder()
                .categoryId(setmealDTO.getCategoryId())
                .name(setmealDTO.getName())
                .price(setmealDTO.getPrice())
                .status(setmealDTO.getStatus())
                .description(setmealDTO.getDescription())
                .image(setmealDTO.getImage())
                .build();
        setmealMapper.insert(setMeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setMeal.getId());
            }
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐起售停售
     * @param status
     * @param id
     */
    @Override
    public void status(Integer status, Long id) {
        if(status==StatusConstant.ENABLE){
            // 检查套餐内是否有停售的菜品
            //TODO 数据库N+1查询问题 -> 直接查询套餐内全部停售菜品
            List<Dish> notSaleList = dishMapper.getBySetmealIdAndStatus(id, StatusConstant.DISABLE);
            if (notSaleList!=null&&!notSaleList.isEmpty()) {
                throw new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        Setmeal setmeal=Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        if(setmealDTO==null){
            throw new BaseException(MessageConstant.DATA_NOT_FOUND);
        }
        Setmeal setmeal=Setmeal.builder()
                .id(setmealDTO.getId())
                .categoryId(setmealDTO.getCategoryId())
                .name(setmealDTO.getName())
                .price(setmealDTO.getPrice())
                .status(setmealDTO.getStatus())
                .description(setmealDTO.getDescription())
                .image(setmealDTO.getImage())
                .build();
        setmealMapper.update(setmeal);
        setMealDishMapper.deleteBySetmealId(setmealDTO.getId());
        List<SetmealDish> dishList = setmealDTO.getSetmealDishes();
        if(dishList!=null&&!dishList.isEmpty()){
            for(SetmealDish setmealDish : dishList){
                //TODO 增强for可以避免索引越界异常,但不保证集合内元素不为null
                if(setmealDish!=null){
                    setmealDish.setSetmealId(setmeal.getId());
                }else{
                    continue;
                }
            }
            setMealDishMapper.insertBatch(dishList);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public SetmealDTO getById(Long id) {
        Setmeal setmeal=setmealMapper.getById(id);
        if(setmeal==null){
            return null;
        }
        SetmealDTO setmealDTO=new SetmealDTO();
        BeanUtils.copyProperties(setmeal,setmealDTO);
        List<SetmealDish> setmealDishes = setMealDishMapper.getById(setmeal.getId());
        setmealDTO.setSetmealDishes(setmealDishes);
        return setmealDTO;
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
        //套餐在售则不能删除
//        for(Long id : ids){
//            Setmeal setmeal=setmealMapper.getById(id);
//            if(setmeal.getStatus()== StatusConstant.ENABLE){
//                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
//            }
//        }

        //TODO 数据库N+1查询问题 -> 直接查询套餐内全部起售菜品
        List<Dish> onSaleList=dishMapper.getByIdsAndStatus(ids,StatusConstant.ENABLE);
        if(onSaleList!=null&&!onSaleList.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        setmealMapper.delete(ids);
        setMealDishMapper.delete(ids);
    }
}
