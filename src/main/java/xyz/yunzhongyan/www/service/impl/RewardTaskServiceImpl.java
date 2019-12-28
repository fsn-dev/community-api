package xyz.yunzhongyan.www.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.dao.mongo.RewardTaskDao;
import xyz.yunzhongyan.www.dao.mongo.TaskProposalDao;
import xyz.yunzhongyan.www.dao.mongo.UserDao;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.po.RewardTask;
import xyz.yunzhongyan.www.domain.po.TaskProposal;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.vo.Accessory;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.domain.vo.RewardTask.*;
import xyz.yunzhongyan.www.util.ColorUtil;
import xyz.yunzhongyan.www.util.EmptyUtil;
import xyz.yunzhongyan.www.util.PageUtil;
import xyz.yunzhongyan.www.util.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @program: api
 * @description:
 * @author: wuxinghan
 * @create: 2019-11-12 15:27
 **/

@Component
@Slf4j
public class RewardTaskServiceImpl {

    @Autowired
    private RewardTaskDao rewardTaskDao;
    @Autowired
    private TaskProposalDao taskProposalDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private IntegralServiceImpl integralServiceImpl;

    public Result getHootRewardPage(Integer type, PagedQueryParam pagedQueryParam) {
        List<RewardTaskHootVo> rewardTaskHootVos = new ArrayList<>();
        List<Integer> states = new ArrayList<>();
        if (type == -1) {
            states.add(0);
            states.add(1);
            states.add(2);
        } else {
            states.add(type);
        }
        Page<RewardTask> rewardTaskPage;
        if (pagedQueryParam.getOrder() != null && pagedQueryParam.getOrder().equals("asc")) {
            rewardTaskPage = rewardTaskDao.findByStateIn(states, new PageRequest(pagedQueryParam.getPage(),
                    pagedQueryParam.getPageSize(),
                    Sort.Direction.ASC,
                    pagedQueryParam.getSortBy()));
        } else {
            rewardTaskPage = rewardTaskDao.findByStateIn(states, new PageRequest(pagedQueryParam.getPage(),
                    pagedQueryParam.getPageSize(),
                    Sort.Direction.DESC,
                    pagedQueryParam.getSortBy()));
        }
        rewardTaskPage.forEach(rewardTask -> {
            RewardTaskHootVo rewardTaskHootVo = new RewardTaskHootVo();
            BeanUtils.copyProperties(rewardTask, rewardTaskHootVo);
            StringBuffer msg = new StringBuffer();
            if (rewardTask.getState() == 0) {
                msg.append("报名中").append("截止日期").append(TimeUtils.convertDateToStringyd2(rewardTask.getDeadline()));
            } else if (rewardTask.getState() == 1) {
                msg.append("进行中");
            } else {
                msg.append("已完成");
            }
            rewardTaskHootVo.setStatusInformation(msg.toString());
            rewardTaskHootVos.add(rewardTaskHootVo);
        });
        return Result.success(PageUtil.builidPage(rewardTaskHootVos, rewardTaskPage.getTotalElements(), pagedQueryParam.getPage(), pagedQueryParam.getPageSize()));
    }

    /**
     * @param
     * @return
     */
    public Result getHootRewardTask() {
        List<RewardTaskHootVo> rewardTaskHootVos = new ArrayList<>();
        try {
            Page<RewardTask> rewardTasks = rewardTaskDao.findByHootIsOrderByDeadlineDesc(true, new PageRequest(0,
                    3,
                    Sort.Direction.DESC,
                    "inTime"));
            rewardTasks.forEach(rewardTask -> {
                RewardTaskHootVo rewardTaskHootVo = new RewardTaskHootVo();
                BeanUtils.copyProperties(rewardTask, rewardTaskHootVo);
                rewardTaskHootVo.setCreater("Fosc官方");
                List<Integer> integers = Arrays.asList(0, 1, 2);
                List<TaskProposal> taskProposals = taskProposalDao.findByStateInAndRewardTaskIdIsOrderByTimeDesc(integers,rewardTask.getId());
                rewardTaskHootVo.setParticipant(taskProposals==null?0:taskProposals.size());
                rewardTaskHootVos.add(rewardTaskHootVo);
            });
            return Result.success(rewardTaskHootVos);
        } catch (Exception e) {
            log.info("getHootRewardTask", e.getMessage());
            return Result.fail("网络异常");
        }
    }

    /**
     * @param rewardTaskVoSave
     * @return
     */
    public Result addRewardTask(AuthenticatedUser authenticatedUser, RewardTaskVoSave rewardTaskVoSave) {
        RewardTask rewardTask = new RewardTask();
        rewardTask.setUserId(authenticatedUser.getUserId());
        rewardTask.setBonus(rewardTaskVoSave.getBonus());
        rewardTask.setScheme(rewardTaskVoSave.getScheme());
        rewardTask.setTitle(rewardTaskVoSave.getTitle());
        // 处理附件
        if (EmptyUtil.isEmpty(rewardTaskVoSave.getAccessory())) {
            rewardTask.setAccessory(new ArrayList<>());
        } else if (rewardTaskVoSave.getAccessory().contains(",")) {
            rewardTask.setAccessory(Arrays.asList(rewardTaskVoSave.getAccessory().split(",")));
        } else {
            rewardTask.setAccessory(Arrays.asList(rewardTaskVoSave.getAccessory()));
        }
        if (EmptyUtil.isNotEmpty(rewardTaskVoSave.getId())) {
            rewardTask.setId(rewardTaskVoSave.getId());
            rewardTask.setCreatDate(new Date());
        }
        Date date = TimeUtils.convertString2Date(rewardTaskVoSave.getDeadline() + " 23:59:59");
        rewardTask.setDeadline(date);
        rewardTask.setParticipant(0);
        rewardTask.setState(0);
        rewardTaskDao.save(rewardTask);
        return Result.success(rewardTask).message("发布成功");
    }

    public Result addTaskProposal(AuthenticatedUser authenticatedUser, TaskProposalVoSave taskProposalVoSave) {
        TaskProposal taskProposal = new TaskProposal();
        taskProposal.setState(0);
        taskProposal.setUserId(authenticatedUser.getUserId());
        taskProposal.setRewardTaskId(taskProposalVoSave.getRewardTaskId());
        taskProposal.setScheme(taskProposalVoSave.getScheme());

        // 处理附件
        if (EmptyUtil.isEmpty(taskProposalVoSave.getAccessory())) {
            taskProposal.setAccessory(new ArrayList<>());
        } else if (taskProposalVoSave.getAccessory().contains(",")) {
            if (taskProposalVoSave.getAccessory().endsWith(","))
                taskProposalVoSave.setAccessory(taskProposalVoSave.getAccessory().substring(0, taskProposalVoSave.getAccessory().length() - 1));
            taskProposal.setAccessory(Arrays.asList(taskProposalVoSave.getAccessory().split(",")));
        } else {
            if (taskProposalVoSave.getAccessory().endsWith(","))
                taskProposalVoSave.setAccessory(taskProposalVoSave.getAccessory().substring(0, taskProposalVoSave.getAccessory().length() - 1));
            taskProposal.setAccessory(Arrays.asList(taskProposalVoSave.getAccessory()));
        }
        if (EmptyUtil.isNotEmpty(taskProposalVoSave.getId())) {
            taskProposal.setId(taskProposalVoSave.getId());
        }
        taskProposal.setTime(new Date());
        TaskProposal save = taskProposalDao.save(taskProposal);

        integralServiceImpl.addRecord(authenticatedUser, 5);
        return Result.success(save).message("发布成功");
    }

    public Result getRewardTaskByUser(AuthenticatedUser authenticatedUser, PagedQueryParam pagedQueryParam) {
        List<UserRewardTask> userRewardTasks = new ArrayList<>();
        Page<TaskProposal> taskProposals = taskProposalDao.findByUserIdIsOrderByTimeDesc(authenticatedUser.getUserId(), new PageRequest(pagedQueryParam.getPage(),
                pagedQueryParam.getPageSize(),
                Sort.Direction.DESC,
                pagedQueryParam.getSortBy()));
        taskProposals.forEach(taskProposal -> {
            UserRewardTask userRewardTask = new UserRewardTask();
            RewardTask one = rewardTaskDao.findOne(taskProposal.getRewardTaskId());
            //统计报名人数
            List<TaskProposal> taskProposal2 = taskProposalDao.findByStateInAndRewardTaskIdIsOrderByTimeDesc(Arrays.asList(1, 2, 3),one.getId());
            int count = taskProposal2 == null ? 0 : taskProposal2.size();
            userRewardTask.setRegistration(count + "人已报名");
            userRewardTask.setId(one.getId());
            userRewardTask.setBonus(one.getBonus());
            userRewardTask.setTitle(one.getTitle());
            userRewardTask.setCheckEnrollmentStatus(one.getState() == 0 ? "报名中" : (one.getState() == 1 ? "进行中" : (one.getState() == 2 ? "已完成" : "已过期")));
            userRewardTask.setCheckEnrollmentStatusColor(one.getState() == 0 ? ColorUtil.GREENCOMMON : (one.getState() == 1 ? ColorUtil.ORANGECOMMON : (one.getState() == 2 ? ColorUtil.REDCOMMON : ColorUtil.GRAYCOMMON)));

            String msg = "审核中";
            String color = ColorUtil.GREENCOMMON;
            if (taskProposal.getState() == 0) {
                msg = "审核中";
                color = ColorUtil.ORANGECOMMON;
            } else if (taskProposal.getState() == 1) {
                color = ColorUtil.GREENCOMMON;
                msg = "进行中";
            } else if (taskProposal.getState() == 2) {
                color = ColorUtil.GREENCOMMON;
                msg = "已完成";
            } /*else if (taskProposal.getState() == 3) {
                color = ColorUtil.REDCOMMON;
                msg = "已完成";
            } else if (taskProposal.getState() == 4) {
                color = ColorUtil.GRAYCOMMON;
                msg = "已过期";
            }*/
            userRewardTask.setTaskStatus(msg);
            userRewardTask.setTaskStatusColor(color);
            userRewardTasks.add(userRewardTask);
        });

        return Result.success(PageUtil.builidPage(userRewardTasks, taskProposals.getTotalElements(), pagedQueryParam.getPage(), pagedQueryParam.getPageSize()));
    }

    public Result getRewardTaskDetail(String id) {
        RewardTaskDeatilVo rewardTaskDeatilVo = new RewardTaskDeatilVo();
        RewardTask one = rewardTaskDao.findOne(id);
        BeanUtils.copyProperties(one, rewardTaskDeatilVo);
        rewardTaskDeatilVo.setTime(TimeUtils.convertDateToStringyd2(one.getDeadline()));
        rewardTaskDeatilVo.setStatusInformation(one.getState() == 0 ? "报名中" : (one.getState() == 1 ? "进行中" : "已完成"));
        List<Integer> integers = Arrays.asList(0, 1, 2);
        List<TaskProposal> taskProposals = taskProposalDao.findByStateInAndRewardTaskIdIsOrderByTimeDesc(integers,id);
        List<Participant> participants = new ArrayList<>();
        taskProposals.forEach(taskProposal -> {
            User userByIdIs = userDao.findUserByIdIs(taskProposal.getUserId());
            Participant participant = new Participant();
            participant.setId(userByIdIs.getId());
            participant.setUsername(userByIdIs.getUsername());
            participant.setHeadPortrait(userByIdIs.getHeadPortrait());
            participants.add(participant);
        });
        List<Accessory> accessorys = new ArrayList<>();
        one.getAccessory().forEach(accessory -> {
            if (accessory.contains("/")) {
                String[] split = accessory.split("/");
                String s = split[split.length - 1];
                if (s.contains(".")) {
                    int i = s.lastIndexOf(".");
                    Accessory accessory1 = new Accessory();
                    accessory1.setName(s.substring(0, i));
                    accessory1.setType(s.substring(i + 1));
                    accessory1.setUrl(accessory);
                    accessorys.add(accessory1);
                }
            }
        });
        rewardTaskDeatilVo.setAccessorys(accessorys);
        rewardTaskDeatilVo.setParticipants(participants);
        return Result.success(rewardTaskDeatilVo);
    }

    public Result geProposalsListEdit(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        TaskProposal taskProposal = taskProposalDao.findOne(rewardTaskQuery.getId());
        TaskProposalStateEdit rewardTaskStateEdit = new TaskProposalStateEdit();
        rewardTaskStateEdit.setId(taskProposal.getId());
        rewardTaskStateEdit.setTaskStatusId(taskProposal.getState());
        rewardTaskStateEdit.setScheme(taskProposal.getScheme());
        String msg = "审核中";
        String color = ColorUtil.GREENCOMMON;
        if (taskProposal.getState() == 0) {
            msg = "审核中";
            color = ColorUtil.ORANGECOMMON;
        } else if (taskProposal.getState() == 1) {
            color = ColorUtil.GREENCOMMON;
            msg = "进行中";
        } else if (taskProposal.getState() == 2) {
            color = ColorUtil.GREENCOMMON;
            msg = "已完成";
        }
        rewardTaskStateEdit.setTaskStatus(msg);
        rewardTaskStateEdit.setTaskStatusColor(color);
        List<RewardTaskState> rewardTaskStates = new ArrayList<>();
        rewardTaskStates.add(new RewardTaskState(0, "审核中"));
        rewardTaskStates.add(new RewardTaskState(1, "进行中"));
        rewardTaskStates.add(new RewardTaskState(2, "已完成"));
        rewardTaskStates.forEach(rewardTaskState -> {
            if (taskProposal.getState() == rewardTaskState.getId()) {
                rewardTaskState.setFlag(true);
            }
        });
        rewardTaskStateEdit.setRewardTaskStates(rewardTaskStates);
        return Result.success(rewardTaskStateEdit);
    }

    public Result  proposalsListEditSave(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        TaskProposal one = taskProposalDao.findOne(rewardTaskQuery.getId());
        one.setState(rewardTaskQuery.getState());
        TaskProposal save = taskProposalDao.save(one);
        return Result.success().message("success");
    }

    public Result getRewardTaskListEdit(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        RewardTask one = rewardTaskDao.findOne(rewardTaskQuery.getId());
        RewardTaskStateEdit rewardTaskStateEdit = new RewardTaskStateEdit();
        rewardTaskStateEdit.setId(one.getId());
        rewardTaskStateEdit.setTaskStatusId(one.getState());
        rewardTaskStateEdit.setTaskStatus(one.getState() == 0 ? "报名中" : (one.getState() == 1 ? "进行中" : (one.getState() == 2 ? "已完成" : "已过期")));
        rewardTaskStateEdit.setTaskStatusColor(one.getState() == 0 ? ColorUtil.GREENCOMMON : (one.getState() == 1 ? ColorUtil.ORANGECOMMON : (one.getState() == 2 ? ColorUtil.REDCOMMON : ColorUtil.GRAYCOMMON)));
        List<RewardTaskState> rewardTaskStates = new ArrayList<>();
        rewardTaskStates.add(new RewardTaskState(0, "报名中"));
        rewardTaskStates.add(new RewardTaskState(1, "进行中"));
        rewardTaskStates.add(new RewardTaskState(2, "已完成"));
        rewardTaskStates.add(new RewardTaskState(3, "已过期"));
        rewardTaskStates.forEach(rewardTaskState -> {
            if (one.getState() == rewardTaskState.getId()) {
                rewardTaskState.setFlag(true);
            }
        });
        rewardTaskStateEdit.setRewardTaskStates(rewardTaskStates);
        return Result.success(rewardTaskStateEdit);
    }

    public Result getRewardTaskListHootSave(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        RewardTask one = rewardTaskDao.findOne(rewardTaskQuery.getId());
        List<RewardTask> hootIs = rewardTaskDao.findByHootIs(true);
        if (rewardTaskQuery.getHot() == 1 & hootIs != null && hootIs.size() >= 3) {
            return Result.fail("最多添加三个热门");
        }
        one.setHoot(rewardTaskQuery.getHot()==1?true:false);
        RewardTask save = rewardTaskDao.save(one);
        return Result.success().message("success");
    }

    public Result getRewardTaskListEditSave(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        RewardTask one = rewardTaskDao.findOne(rewardTaskQuery.getId());
        one.setState(rewardTaskQuery.getState());
        RewardTask save = rewardTaskDao.save(one);
        return Result.success().message("success");
    }

    public Result getRewardTaskList(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        List<Integer> states = new ArrayList<>();
        if (rewardTaskQuery.getState() == null || rewardTaskQuery.getState() == -1) {
            states.add(0);
            states.add(1);
            states.add(2);
            states.add(3);
        } else {
            states.add(rewardTaskQuery.getState());
        }
        Page<RewardTask> rewardTasks;
        if ("asc".equals(rewardTaskQuery.getOrder())) {
            rewardTasks = rewardTaskDao.findByStateInOrderByDeadlineAsc(states, new PageRequest(rewardTaskQuery.getPage(),
                    rewardTaskQuery.getPageSize(),
                    Sort.Direction.DESC,
                    rewardTaskQuery.getSortBy()));
        } else {
            rewardTasks = rewardTaskDao.findByStateInOrderByDeadlineDesc(states, new PageRequest(rewardTaskQuery.getPage(),
                    rewardTaskQuery.getPageSize(),
                    Sort.Direction.DESC,
                    rewardTaskQuery.getSortBy()));
        }
        List<UserRewardTask> userRewardTasks = new ArrayList<>();
        rewardTasks.forEach(rewardTask -> {
            UserRewardTask userRewardTask = new UserRewardTask();
            userRewardTask.setHot(rewardTask.getHoot()==null?false:rewardTask.getHoot());
            userRewardTask.setId(rewardTask.getId());
            userRewardTask.setBonus(rewardTask.getBonus());
            userRewardTask.setTitle(rewardTask.getTitle());
            userRewardTask.setTaskStatus(rewardTask.getState() == 0 ? "报名中" : (rewardTask.getState() == 1 ? "进行中" : (rewardTask.getState() == 2 ? "已完成" : "已过期")));
            userRewardTask.setTaskStatusColor(rewardTask.getState() == 0 ? ColorUtil.GREENCOMMON : (rewardTask.getState() == 1 ? ColorUtil.ORANGECOMMON : (rewardTask.getState() == 2 ? ColorUtil.REDCOMMON : ColorUtil.GRAYCOMMON)));
            userRewardTask.setCreatTime(rewardTask.getCreatDate() == null ? TimeUtils.convertDateToStringyd(new Date()) : TimeUtils.convertDateToStringyd(rewardTask.getCreatDate()));
            userRewardTasks.add(userRewardTask);
        });
        return Result.success(PageUtil.builidPage(userRewardTasks, rewardTasks.getTotalElements(), rewardTaskQuery.getPage(), rewardTaskQuery.getPageSize()));
    }

    public Result getRewardTaskListStates(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        List<RewardTaskState> rewardTaskStates = new ArrayList<>();
        rewardTaskStates.add(new RewardTaskState(-1, "已发布"));
        rewardTaskStates.add(new RewardTaskState(0, "报名中"));
        rewardTaskStates.add(new RewardTaskState(1, "进行中"));
        rewardTaskStates.add(new RewardTaskState(2, "已完成"));
        rewardTaskStates.add(new RewardTaskState(3, "已过期"));
        return Result.success(rewardTaskStates);
    }

    public Result getRewardTaskListProposal(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        UserRewardTask userRewardTask = new UserRewardTask();
        RewardTask rewardTask = rewardTaskDao.findOne(rewardTaskQuery.getId());
        userRewardTask.setId(rewardTask.getId());
        userRewardTask.setBonus(rewardTask.getBonus());
        userRewardTask.setTitle(rewardTask.getTitle());
        userRewardTask.setTaskStatus(rewardTask.getState() == 0 ? "报名中" : (rewardTask.getState() == 1 ? "进行中" : (rewardTask.getState() == 2 ? "已完成" : "已过期")));
        userRewardTask.setTaskStatusColor(rewardTask.getState() == 0 ? ColorUtil.GREENCOMMON : (rewardTask.getState() == 1 ? ColorUtil.ORANGECOMMON : (rewardTask.getState() == 2 ? ColorUtil.REDCOMMON : ColorUtil.GRAYCOMMON)));
        userRewardTask.setCreatTime(rewardTask.getCreatDate() == null ? TimeUtils.convertDateToStringyd(new Date()) : TimeUtils.convertDateToStringyd(rewardTask.getCreatDate()));
        userRewardTask.setScheme(rewardTask.getScheme());
        return Result.success(userRewardTask);
    }

    public Result getRewardTaskListProposals(AuthenticatedUser authenticatedUser, RewardTaskQuery rewardTaskQuery) {
        Page<TaskProposal> taskProposals;
        if ("asc".equals(rewardTaskQuery.getOrder())) {
            taskProposals = taskProposalDao.findByRewardTaskIdIsOrderByTimeAsc(rewardTaskQuery.getId(), new PageRequest(rewardTaskQuery.getPage(),
                    rewardTaskQuery.getPageSize(),
                    Sort.Direction.DESC,
                    rewardTaskQuery.getSortBy()));
        } else {
            taskProposals = taskProposalDao.findByRewardTaskIdIsOrderByTimeDesc(rewardTaskQuery.getId(), new PageRequest(rewardTaskQuery.getPage(),
                    rewardTaskQuery.getPageSize(),
                    Sort.Direction.DESC,
                    rewardTaskQuery.getSortBy()));
        }
        List<TaskProposalVo> taskProposalVos = new ArrayList<>();
        taskProposals.forEach(taskProposal -> {
            TaskProposalVo taskProposalVo = new TaskProposalVo();
            taskProposalVo.setId(taskProposal.getId());
            User user = userDao.findUserByIdIs(taskProposal.getUserId());
            taskProposalVo.setUserName(user.getUsername() == null ? "" : user.getUsername());
            taskProposalVo.setEmail(user.getMailbox());
            taskProposalVo.setTime(TimeUtils.convertDateToStringyd(taskProposal.getTime()));
            String msg = "审核中";
            String color = ColorUtil.GREENCOMMON;
            if (taskProposal.getState() == 0) {
                msg = "审核中";
                color = ColorUtil.ORANGECOMMON;
            } else if (taskProposal.getState() == 1) {
                color = ColorUtil.GREENCOMMON;
                msg = "进行中";
            } else if (taskProposal.getState() == 2) {
                color = ColorUtil.GREENCOMMON;
                msg = "已验收";
            } else if (taskProposal.getState() == 3) {
                color = ColorUtil.REDCOMMON;
                msg = "已完成";
            } else if (taskProposal.getState() == 4) {
                color = ColorUtil.GRAYCOMMON;
                msg = "已过期";
            }
            taskProposalVo.setCheckEnrollmentStatus(msg);
            taskProposalVo.setCheckEnrollmentStatusColor(color);
            taskProposalVos.add(taskProposalVo);
        });
        return Result.success(PageUtil.builidPage(taskProposalVos, taskProposals.getTotalElements(), rewardTaskQuery.getPage(), rewardTaskQuery.getPageSize()));
    }
}
