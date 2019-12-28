package xyz.yunzhongyan.www.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.yunzhongyan.www.domain.po.Article;
import xyz.yunzhongyan.www.domain.vo.ArticleSave;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.service.ArticleService;

/**
 * @program: api
 * @description:
 * @author: wuxinghan
 * @create: 2019-11-12 15:27
 **/

@Api("技术文章服务")
@RestController
@RequestMapping("/article")
@Slf4j
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @ApiOperation(value = "文章", notes = "")
    @GetMapping("/articleList")
    public Result getArticleList(PagedQueryParam pagedQueryParam) {
        return articleService.getArticleList(pagedQueryParam);
    }

    @ApiOperation(value = "根据id获取文章", notes = "")
    @GetMapping("/byId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "string", paramType = "query"),
    })
    public Result getArticlebyId(String id) {
        return articleService.getArticlebyId(id);
    }

    @ApiOperation("添加/更新--技术文章--根据id是否为空判断")
    @PostMapping()
    public Result addArticleNew(@RequestBody ArticleSave article) {
        return articleService.addArticleNew(article);
    }

    @ApiOperation("按照id删除技术文章")
    @DeleteMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "string", paramType = "query"),
    })
    public Result deleteArticleById(String id) {
        try {
            return articleService.deleteArticle(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation(value = "文章列表--后台管理列表", notes = "")
    @GetMapping("/articleListBack")
    public Result getArticleListBack(PagedQueryParam pagedQueryParam) {
        return articleService.getArticleListBack(pagedQueryParam);
    }

    @ApiOperation(value = "根据id获取文章", notes = "")
    @GetMapping("/byIdBack")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "string", paramType = "query"),
    })
    public Result getArticlebyIdBack(String id) {
        return articleService.getArticlebyIdBack(id);
    }


    @ApiOperation("添加技术文章")
    @PostMapping(value = "/addArticle/")
    public Result addArticle(@RequestBody Article article) {
        try {
            return articleService.addArticle(article);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("修改技术文章")
    @PostMapping(value = "/updateArticle/")
    public Result updateArticle(@RequestBody Article article) {
        try {
            return articleService.updateArticle(article);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("按照id删除技术文章")
    @GetMapping(value = "/deleteArticle/")
    public Result deleteArticle(@ApiParam(value = "文章id") @RequestParam(name = "id", required = true) String id) {
        try {
            return articleService.deleteArticle(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("按照id查询技术文章")
    @GetMapping(value = "/selectArticleById/")
    public Result selectArticleById(@ApiParam(value = "文章id") @RequestParam(name = "id", required = true) String id) {
        try {
            return articleService.selectArticleById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("分页查询技术文章")
    @GetMapping(value = "/findAllByPage/")
    public Result findAllByPage(@ApiParam(value = "页数") @RequestParam(name = "pageIndex", required = true) Integer pageIndex,
                                @ApiParam(value = "每页条数") @RequestParam(name = "pageSize", required = true) Integer pageSize) {
        try {
            return articleService.findAllByPage(pageIndex, pageSize);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }


    @ApiOperation("根据关键字查询相似文章")
    @GetMapping(value = "/findAllByKeyWordLike/")
    public Result findAllByKeyWordLike(@ApiParam(value = "页数") @RequestParam(name = "pageIndex", required = true) Integer pageIndex,
                                       @ApiParam(value = "每页条数") @RequestParam(name = "pageSize", required = true) Integer pageSize,
                                       @ApiParam(value = "关键字") @RequestParam(name = "keyWord", required = true) String keyWord) {
        try {
            return articleService.findAllByKeyWordLike(pageIndex, pageSize, keyWord);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }


}
