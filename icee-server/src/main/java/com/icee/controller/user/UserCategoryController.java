package com.icee.controller.user;

import com.icee.entity.Category;
import com.icee.result.Result;
import com.icee.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/category")
@Slf4j
@Tag(name = "User端-分类接口")
public class UserCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类列表
     * @param type
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询分类列表")
    public Result<List<Category>> list(Integer type){
        List<Category> categoryList=categoryService.list(type);
        return Result.success(categoryList);
    }
}
