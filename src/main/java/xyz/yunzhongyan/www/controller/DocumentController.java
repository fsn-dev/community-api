package xyz.yunzhongyan.www.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.yunzhongyan.www.domain.po.Document;
import xyz.yunzhongyan.www.domain.vo.DocumentSave;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.service.DocumentService;

/**
 * @program: api
 * @description:
 * @author: wuxinghan
 * @create: 2019-11-12 15:27
 **/

@Api("技术文章服务")
@RestController
@RequestMapping("/document")
@Slf4j
@CrossOrigin
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "话题页面列表--根据指定要求", notes = "")
    @GetMapping("/byId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文档id", required = true, dataType = "string", paramType = "query"),
    })
    public Result getDocumentbyId(String id) {
        return documentService.getDocumentbyId(id);
    }

    @ApiOperation(value = "文档列表", notes = "")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "keyword模糊查询使用", dataType = "string", paramType = "query"),
    })
    public Result getTree(String keyword) {
        return documentService.getTree(keyword);
    }

    @ApiOperation("添加、更新文档--根据id是否为空判断")
    @PostMapping()
    public Result addDocument(@RequestBody DocumentSave document) {
        try {
            return documentService.addDocument(document);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("按照id删除文档")
    @DeleteMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文档id", required = true, dataType = "string", paramType = "query"),
    })
    public Result deleteDocumentById(String id) {
        try {
            return documentService.deleteDocument(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation(value = "文章列表--后台管理列表", notes = "")
    @GetMapping("/listBack")
    public Result getDocumentListBack(PagedQueryParam pagedQueryParam) {
        return documentService.getDocumentListBack(pagedQueryParam);
    }
}
