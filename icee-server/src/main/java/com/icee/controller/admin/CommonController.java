package com.icee.controller.admin;

import com.icee.annotation.AutoFill;
import com.icee.constant.MessageConstant;
import com.icee.result.Result;
import com.icee.utils.AliyunOSSOperator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Tag(name = "通用接口")
public class CommonController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public Result<String> upload(MultipartFile  file) throws Exception {
        try {
            if (file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }
            String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败:{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
