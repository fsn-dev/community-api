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
import org.springframework.web.bind.annotation.*;
import xyz.yunzhongyan.www.annotation.Authentication;
import xyz.yunzhongyan.www.annotation.CurrentUser;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.po.RewardTask;
import xyz.yunzhongyan.www.domain.vo.DocumentSave;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.domain.vo.RewardTask.RewardTaskQuery;
import xyz.yunzhongyan.www.domain.vo.RewardTask.RewardTaskVoSave;
import xyz.yunzhongyan.www.domain.vo.RewardTask.TaskProposalVoSave;
import xyz.yunzhongyan.www.service.impl.RewardTaskServiceImpl;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-15 18:48
 **/
@Api("悬赏任务")
@RestController
@RequestMapping("/task")
@Slf4j
@CrossOrigin
public class RewardTaskController {
    @Autowired
    private RewardTaskServiceImpl rewardTaskService;

    @ApiOperation("悬赏任务-详情-参与")
    @PostMapping("/proposal")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result addTaskProposal(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody TaskProposalVoSave rewardTaskVoSave) {
        try {
            return rewardTaskService.addTaskProposal(authenticatedUser, rewardTaskVoSave);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("添加、更新悬赏任务--根据id是否为空判断")
    @PostMapping()
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result addRewardTask(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody RewardTaskVoSave rewardTaskVoSave) {
        try {
            return rewardTaskService.addRewardTask(authenticatedUser, rewardTaskVoSave);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("获取热门任务")
    @GetMapping("/hoot")
    public Result getHootRewardTask() {
        try {
            return rewardTaskService.getHootRewardTask();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("获取热门任务")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "-1:已公开  1:进行中  2:已完成", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "倒序：desc,正序:asc", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "按时间：deadline,按人数：participant", required = true, dataType = "string", paramType = "query"),
    })
    public Result getHootRewardPage(Integer type, PagedQueryParam pagedQueryParam) {
        try {
            return rewardTaskService.getHootRewardPage(type, pagedQueryParam);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("获取任务详情")
    @GetMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "悬赏任务id", required = true, dataType = "string", paramType = "query"),
    })
    public Result getRewardTaskDetail(String id) {
        try {
            return rewardTaskService.getRewardTaskDetail(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("我的任务")
    @GetMapping("/userList")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result getRewardTaskByUser(@CurrentUser AuthenticatedUser authenticatedUser, PagedQueryParam pagedQueryParam) {
        try {
            return rewardTaskService.getRewardTaskByUser(authenticatedUser, pagedQueryParam);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("任务列表")
    @GetMapping("/list")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "state", value = "-1:已公开  1:进行中  2:已完成", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "倒序：desc,正序:asc", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "time", required = true, dataType = "string", paramType = "query"),
    })
    public Result getRewardTaskList(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.getRewardTaskList(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("任务列表--编辑--查询")
    @GetMapping("/list/edit")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "string", paramType = "query"),
    })
    public Result getRewardTaskListEdit(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.getRewardTaskListEdit(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("任务列表--编辑--保存")
    @PostMapping("/list/edit")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态id", required = true, dataType = "int", paramType = "query"),
    })
    public Result getRewardTaskListEditSave(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.getRewardTaskListEditSave(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("任务列表--是否热门保存")
    @PostMapping("/list/hot")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "hot", value = "热门:1  非热门：0", required = true, dataType = "int", paramType = "query"),
    })
    public Result getRewardTaskListHootSave(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.getRewardTaskListHootSave(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("任务列表--状态下拉框")
    @GetMapping("/list/states")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result getRewardTaskListStates(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.getRewardTaskListStates(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("任务列表--任务内容")
    @GetMapping("/list/taskDetail")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "string", paramType = "query"),

    })
    public Result getRewardTaskListTaskDetail(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.getRewardTaskListProposal(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("任务列表--方案列表")
    @GetMapping("/list/proposals")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "倒序：desc,正序:asc", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "time", required = true, dataType = "string", paramType = "query"),
    })
    public Result getRewardTaskListProposals(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.getRewardTaskListProposals(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("方案审核--查询")
    @GetMapping("/list/proposals/edit")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "string", paramType = "query"),
    })
    public Result geProposalsListEdit(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.geProposalsListEdit(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }

    @ApiOperation("方案审核--保存")
    @PostMapping("/list/proposals/edit")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态id", required = true, dataType = "int", paramType = "query"),
    })
    public Result proposalsListEditSave(@CurrentUser AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        try {
            return rewardTaskService.proposalsListEditSave(authenticatedUser, rewardTaskQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.fail(ex.getMessage());
        }
    }
}
