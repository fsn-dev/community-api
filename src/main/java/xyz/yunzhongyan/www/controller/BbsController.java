package xyz.yunzhongyan.www.controller;/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.yunzhongyan.www.annotation.Authentication;
import xyz.yunzhongyan.www.annotation.CurrentUser;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.domain.vo.bbs.CommentSaveVo;
import xyz.yunzhongyan.www.domain.vo.bbs.CommentUpdateVo;
import xyz.yunzhongyan.www.domain.vo.bbs.TopicQueryParam;
import xyz.yunzhongyan.www.domain.vo.bbs.TopicSaveVo;
import xyz.yunzhongyan.www.service.impl.BbsServiceImpl;

/**
 * @program: api
 * @description: 论坛
 * @author: wander
 * @create: 2019-11-22 15:09
 **/
@RestController
@RequestMapping("/bbs")
@CrossOrigin
@Slf4j
public class BbsController {
    @Autowired
    private BbsServiceImpl bbsService;

    @ApiOperation(value = "点赞", notes = "")
    @GetMapping("/vote")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @Authentication
    public Result saveVote(@CurrentUser AuthenticatedUser authenticatedUser, String topicId) {
        return bbsService.saveVote(authenticatedUser, topicId);
    }

    @ApiOperation(value = "创建评论", notes = "")
    @PostMapping("/comment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @Authentication
    public Result saveComment(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody CommentSaveVo commentSaveVo) {
        return bbsService.saveComment(authenticatedUser, commentSaveVo);
    }

    @ApiOperation(value = "修改评论", notes = "")
    @PutMapping("/comment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @Authentication
    public Result updateComment(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody CommentUpdateVo commentUpdateVo) {
        return bbsService.updateComment(authenticatedUser, commentUpdateVo);
    }

    @ApiOperation(value = "判断是否有权限发布话题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @Authentication
    @GetMapping("/judge")
    public Result judge(@CurrentUser AuthenticatedUser authenticatedUser) {
        return bbsService.judge(authenticatedUser);
    }

    @ApiOperation(value = "获取话题详情--所有评论--分页", notes = "")
    @GetMapping("/comments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "topicId", value = "话题id", required = true, dataType = "string", paramType = "query"),
    })
    @Authentication
    public Result getComments(@CurrentUser AuthenticatedUser authenticatedUser, String topicId, PagedQueryParam pagedQueryParam) {
        return bbsService.getComments(authenticatedUser, topicId, pagedQueryParam);
    }

    @ApiOperation(value = "获取话题详情", notes = "")
    @GetMapping("/topic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "topicId", value = "话题id", required = true, dataType = "string", paramType = "query"),
    })
    @Authentication
    public Result getTopic(@CurrentUser AuthenticatedUser authenticatedUser, String topicId) {
        return bbsService.getTopic(authenticatedUser, topicId);
    }

    @ApiOperation(value = "保存或修改话题", notes = "")
    @PostMapping("/topic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @Authentication
    public Result saveTopic(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody TopicSaveVo topicSaveVo) {
        return bbsService.saveTopic(authenticatedUser, topicSaveVo);
    }

    @ApiOperation(value = "话题页面列表--根据指定要求", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tagId", value = "模块id 默认为null 返回所有模块的数据", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageType", value = "//0：模块页面 1:最新页面  2:最热页面", required = true, dataType = "string", paramType = "query"),
    })
    @GetMapping("/topicList")
    public Result getTopicList(TopicQueryParam topicQueryParam) {
        return bbsService.getTopicList(topicQueryParam);
    }


    @ApiOperation(value = "板块页面列表", notes = "")
    @GetMapping("/tagList")
    public Result getTagList() {
        return bbsService.getTagList();
    }

    @ApiOperation(value = "板块下拉框", notes = "")
    @GetMapping("/tagCombobox")
    public Result getTagCombobox() {
        return bbsService.getTagCombobox();
    }


    @ApiOperation(value = "我的主题", notes = "")
    @GetMapping("/user/topicList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @Authentication
    public Result getUserTopicList(@CurrentUser AuthenticatedUser authenticatedUser, PagedQueryParam pagedQueryParam) {
        return bbsService.getUserTopicList(authenticatedUser,pagedQueryParam);
    }
}
