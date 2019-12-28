package xyz.yunzhongyan.www.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.dao.mongo.RewardTaskDao;
import xyz.yunzhongyan.www.dao.mongo.TaskProposalDao;
import xyz.yunzhongyan.www.dao.mongo.UserDao;
import xyz.yunzhongyan.www.dao.mongo.UserIntergralRecordDao;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.po.TaskProposal;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.po.UserIntegralRecord;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.domain.vo.StateButton;
import xyz.yunzhongyan.www.domain.vo.UserIntergral2Vo;
import xyz.yunzhongyan.www.domain.vo.UserIntergralVo;
import xyz.yunzhongyan.www.util.TimeUtils;

import java.time.LocalDate;
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
public class IntegralServiceImpl {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserIntergralRecordDao userIntergralRecordDao;
    @Autowired
    private RewardTaskDao rewardTaskDao;
    @Autowired
    private TaskProposalDao taskProposalDao;

    public Result getbutton(AuthenticatedUser authenticatedUser,Integer type,String id) {
        StateButton stateButton = new StateButton();
        if (type==0){
            //判断是否可以看后台管理
            stateButton.setName("后台管理");
            User user = userDao.findUserByIdIs(authenticatedUser.getUserId());
            if (user.getAdmin()!=null && user.getAdmin()==1){
                stateButton.setState(true);
            }
        }else if (type==1){
            //判断立刻报名按钮是否可见
            stateButton.setName("立刻报名");
            if ("5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())){
                return Result.success(stateButton);
            }
            List<TaskProposal> taskProposals = taskProposalDao.findByUserIdIsAndRewardTaskIdIs(authenticatedUser.getUserId(), id);
            if (taskProposals!=null && taskProposals.size()>0){
                return Result.success(stateButton);
            }
            stateButton.setState(true);
        }else if (type==2){
            //是否签到
            stateButton.setName("签到按钮");
            if ("5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())){
                return Result.success(stateButton);
            }
            LocalDate now = LocalDate.now();
            Date date = TimeUtils.localDate2Date(now);
            UserIntegralRecord userIntegralRecord = userIntergralRecordDao.findByUserIdIsAndTimeIsAndTypeIs(authenticatedUser.getUserId(), date,1);
            if (userIntegralRecord!=null ){
                return Result.success(stateButton);
            }
            stateButton.setState(true);
        }
        return Result.success(stateButton);
    }

    public Result getSignIn(AuthenticatedUser authenticatedUser) {
        LocalDate now = LocalDate.now();
        Date date = TimeUtils.localDate2Date(now);
        UserIntegralRecord userIntegralRecord = userIntergralRecordDao.findByUserIdIsAndTimeIsAndTypeIs(authenticatedUser.getUserId(), date,1);
        if (userIntegralRecord==null){
            //积分处理
            addRecord(authenticatedUser,1);
            return Result.success("签到成功");
        }else {
            return Result.success("你今天已经签过到啦");
        }
    }

    public Result getUserIntegral(AuthenticatedUser authenticatedUser) {
        UserIntergralVo userIntergralVo = new UserIntergralVo();
        Integer count = (Integer) redisTemplate.opsForValue().get(String.format("integral:%s:user_id", authenticatedUser.getUserId()));
        userIntergralVo.setIntergral(count == null ? 0 : count);
        List<UserIntergral2Vo> userIntergral2Vos = new ArrayList<>();
        UserIntergral2Vo userIntergral2Vo = new UserIntergral2Vo();
        userIntergral2Vo.setType("去发帖");
        userIntergral2Vo.setValues(Arrays.asList("论坛发布主题成功+5", "发布主题有人赞或评论+5", "评论论坛主题+2"));
        userIntergral2Vos.add(userIntergral2Vo);
        UserIntergral2Vo userIntergral2Vo2 = new UserIntergral2Vo();
        userIntergral2Vo2.setType("去看看");
        userIntergral2Vo2.setValues(Arrays.asList("成功参与一次悬赏任务+20"));
        userIntergral2Vos.add(userIntergral2Vo2);
        userIntergralVo.setUserIntergral2Vos(userIntergral2Vos);
        return Result.success(userIntergralVo);
    }

    /**
     * topic 积分增加
     *
     * @param
     * @param
     */
    private void add(AuthenticatedUser authenticatedUser,Integer type) {
        Integer value=0;
        if (type==1){
            value=5;
        }else if (type==2){
            value=5;
        }if (type==3){
            value=5;
        }if (type==4){
            value=2;
        }if (type==5){
            value=20;
        }
        Integer count = (Integer) redisTemplate.opsForValue().get(String.format("integral:%s:user_id", authenticatedUser.getUserId()));
        redisTemplate.opsForValue().set(String.format(String.format("integral:%s:user_id", authenticatedUser.getUserId())), count == null ? value : count + value);
    }

    public void addRecord(AuthenticatedUser authenticatedUser,Integer type) {
        LocalDate now = LocalDate.now();
        Date date = TimeUtils.localDate2Date(now);
        UserIntegralRecord userIntegralRecord = userIntergralRecordDao.findByUserIdIsAndTimeIsAndTypeIs(authenticatedUser.getUserId(), date,type);
        //处理记录
        if (userIntegralRecord==null){
            UserIntegralRecord userIntegralRecord1 = new UserIntegralRecord();
            userIntegralRecord1.setCount(1);
            userIntegralRecord1.setTime(date);
            userIntegralRecord1.setType(type);
            userIntegralRecord1.setUserId(authenticatedUser.getUserId());
            userIntergralRecordDao.save(userIntegralRecord1);
        }else {
            userIntegralRecord.setCount(userIntegralRecord.getCount()+1);
            userIntergralRecordDao.save(userIntegralRecord);
        }

        //处理积分
        add(authenticatedUser,type);

    }

    private void updateHootValue(String topicId) {
        try {
            Integer view = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicId, "view"));
            view = view == null ? 0 : view;
            Integer up = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicId, "up"));
            up = up == null ? 0 : up;
            Integer commentCount = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicId, "comment"));
            commentCount = commentCount == null ? 0 : commentCount;
        } catch (Exception e) {
            log.error("updateHootValue异常", e.getMessage());
        }
    }
}
