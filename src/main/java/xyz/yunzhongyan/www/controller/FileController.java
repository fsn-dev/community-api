package xyz.yunzhongyan.www.controller;/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.yunzhongyan.www.annotation.Authentication;
import xyz.yunzhongyan.www.annotation.CurrentUser;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.service.impl.FileServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-03 14:05
 **/
@Api("文件管理模块")
@RestController
@RequestMapping("/file")
@Slf4j
@CrossOrigin
public class FileController {
    @Autowired
    private FileServiceImpl fileService;

    @ApiOperation(value = "上传照片 返回URL 单张")
    @Authentication
    @PostMapping("/photo")
    public Result savephoto(@CurrentUser AuthenticatedUser authenticatedUser, HttpServletRequest request) {
        String upload = fileService.upload(request);
        return Result.success(upload);
    }
}
