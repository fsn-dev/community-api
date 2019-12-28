package xyz.yunzhongyan.www.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.dao.mongo.UserDao;
import xyz.yunzhongyan.www.dao.mongo.bbs.CommentDao;
import xyz.yunzhongyan.www.dao.mongo.bbs.TagDao;
import xyz.yunzhongyan.www.dao.mongo.bbs.TopicDao;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.po.bbs.Comment;
import xyz.yunzhongyan.www.domain.po.bbs.Tag;
import xyz.yunzhongyan.www.domain.po.bbs.Topic;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.domain.vo.bbs.*;
import xyz.yunzhongyan.www.service.BbsService;
import xyz.yunzhongyan.www.util.EmptyUtil;
import xyz.yunzhongyan.www.util.PageUtil;
import xyz.yunzhongyan.www.util.TimeUtils;

import java.util.*;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-11-22 15:12
 **/
@Component
@Slf4j
public class BbsServiceImpl implements BbsService {

    @Autowired
    protected TagDao tagDao;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IntegralServiceImpl integralServiceImpl;
    @Override
    public Result saveVote(AuthenticatedUser authenticatedUser, String topicId) {
        if ("5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())) {
            return Result.fail("请先登录");
        }
        if (EmptyUtil.isEmpty(topicId)) {
            return Result.success().message("话题的id呢？");
        }
        Topic topic = topicDao.findByIdIs(topicId);
        if (EmptyUtil.isNotEmpty(topic.getUpIds()) && topic.getUpIds().contains(authenticatedUser.getUserId())) {
            return Result.success().message("你已经点过赞啦");
        }
        String ups = topic.getUpIds() == null ? (authenticatedUser.getUserId() + ",") : (topic.getUpIds() + authenticatedUser.getUserId() + ",");
        topic.setUpIds(ups);
        Topic save = topicDao.save(topic);
        if (EmptyUtil.isEmpty(save)) {
            return Result.success().message("网络异常请稍后再试");
        }
        //点赞
        topicAddInfo(topicId, "up");
        tagAddInfo(topicId, "up");
        //处理积分
        AuthenticatedUser authenticatedUser1 = new AuthenticatedUser();
        authenticatedUser1.setUserId(topic.getUserId());
        integralServiceImpl.addRecord(authenticatedUser1,3);
        return Result.success().message("点赞成功");
    }

    /**
     * topic 点赞，浏览 ，回帖处理
     *
     * @param topicId
     * @param type
     */
    private void topicAddInfo(String topicId, String type) {
        Integer count = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicId, type));
        redisTemplate.opsForValue().set(String.format(String.format("topic:%s_%s:topic_id", topicId, type)), count == null ? 1 : count + 1);
        updateHootValue(topicId);
    }

    private void updateHootValue(String topicId) {
        try {
            Integer view = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicId, "view"));
            view = view == null ? 0 : view;
            Integer up = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicId, "up"));
            up = up == null ? 0 : up;
            Integer commentCount = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicId, "comment"));
            commentCount = commentCount == null ? 0 : commentCount;
            Double hoot = Math.log(fac(view)) + (1.75 * up) + commentCount;
            Topic topic = topicDao.findByIdIs(topicId);
            topic.setHootValue(hoot.floatValue());
            topicDao.save(topic);
        } catch (Exception e) {
            log.error("updateHootValue异常", e.getMessage());
        }
    }

    private static long fac(int n) {
        if (n == 0 || n == 1) return 1;
        return n * fac(n - 1);
    }


    /**
     * tag 点赞，浏览 ，回帖处理
     *
     * @param tagId
     * @param type
     */
    private void tagAddInfo(String tagId, String type) {
        Integer count = (Integer) redisTemplate.opsForValue().get(String.format("tag:%s_%s:tag_id", tagId, type));
        redisTemplate.opsForValue().set(String.format(String.format("tag:%s_%s:tag_id", tagId, type)), count == null ? 1 : count + 1);
    }

    @Override
    public Result updateComment(AuthenticatedUser authenticatedUser, CommentUpdateVo commentUpdateVo) {
        try {
            if ("5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())) {
                return Result.fail("请先登录");
            }
            if (EmptyUtil.isEmpty(commentUpdateVo.getCommentId())) {
                return Result.success().message("评论的id呢？");
            }
            if (EmptyUtil.isEmpty(commentUpdateVo.getContent())) {
                return Result.success().message("请输入评论内容");
            }
            Comment comment = commentDao.findOne(commentUpdateVo.getCommentId());
            comment.setContent(commentUpdateVo.getContent());
            comment.setContentMD(commentUpdateVo.getContentMD());
            Comment save = commentDao.save(comment);
            if (EmptyUtil.isEmpty(save)) {
                return Result.success().message("网络异常请稍后再试");
            }
            return Result.success().message("评论已更新");
        } catch (Exception e) {
            log.info("BbsServiceImpl saveComment异常", e.getMessage());
            return Result.success().message("网络异常请稍后再试");
        }
    }

    @Override
    public Result saveComment(AuthenticatedUser authenticatedUser, CommentSaveVo commentSaveVo) {
        if ("5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())) {
            return Result.fail("请先登录");
        }
        try {
            if (EmptyUtil.isEmpty(commentSaveVo.getContent())) {
                return Result.success().message("请输入评论内容");
            }
            if (EmptyUtil.isEmpty(commentSaveVo.getTopicId())) {
                return Result.success().message("话题ID呢?");
            }
            if (EmptyUtil.isEmpty(commentSaveVo.getContent())) {
                return Result.success().message("请输入评论内容");
            }
            Topic topic = topicDao.findByIdIs(commentSaveVo.getTopicId());
            if (EmptyUtil.isEmpty(topic)) {
                return Result.success().message("你晚了一步，话题可能已经被删除了");
            }
            // 组装comment对象
            Comment comment = new Comment();
            comment.setCommentId(commentSaveVo.getCommentId());
            comment.setContent(commentSaveVo.getContent());
            comment.setContentMD(commentSaveVo.getContentMD());
            comment.setInTime(new Date());
            comment.setTopicId(commentSaveVo.getTopicId());
            comment.setUserId(authenticatedUser.getUserId());
            //非一级标题 记录一级标题id
            if (EmptyUtil.isNotEmpty(commentSaveVo.getCommentId())) {
                Comment one = commentDao.findOne(commentSaveVo.getCommentId());
                comment.setFistCommentId(one.getFistCommentId());
            }
            Comment save = commentDao.save(comment);
            //记录一级评论的id
            if (EmptyUtil.isEmpty(commentSaveVo.getCommentId())) {
                save.setFistCommentId(save.getId());
                commentDao.save(save);
            }
            if (EmptyUtil.isEmpty(save)) {
                return Result.success().message("网络异常请稍后再试");
            }
            //记录回帖数
            topicAddInfo(save.getTopicId(), "comment");
            tagAddInfo(topic.getTagId(), "comment");
            //处理积分
            integralServiceImpl.addRecord(authenticatedUser,4);
            AuthenticatedUser authenticatedUser1 = new AuthenticatedUser();
            authenticatedUser1.setUserId(topic.getUserId());
            integralServiceImpl.addRecord(authenticatedUser1,3);
            return Result.success().message("评论已添加");
        } catch (Exception e) {
            log.info("BbsServiceImpl saveComment异常", e.getMessage());
            return Result.success().message("网络异常请稍后再试");
        }
    }

    @Override
    public Result getComments(AuthenticatedUser authenticatedUser, String topicId, PagedQueryParam pagedQueryParam) {
        List<List<TopicCommentDetailOneVo>> listArrayList = new ArrayList<>();
        Page<Comment> commentPage = commentDao.findByTopicIdIsAndCommentIdIsNullOrderByInTimeDesc(topicId, new PageRequest(pagedQueryParam.getPage(),
                pagedQueryParam.getPageSize(),
                Sort.Direction.DESC,
                "inTime"));
        commentPage.getContent().forEach(comment -> {
            List<TopicCommentDetailOneVo> list = new ArrayList<>();
            list.add(formartTopicCommentDetailOneVo(authenticatedUser, comment));
            List<Comment> comments = commentDao.findByFistCommentIdIsAndCommentIdIsNotNullOrderByInTimeDesc(comment.getId());
            for (Comment comment1 : comments) {
                list.add(formartTopicCommentDetailOneVo(authenticatedUser, comment1));
            }
            listArrayList.add(list);
        });
        return Result.success(PageUtil.page(listArrayList, pagedQueryParam.getPage(), pagedQueryParam.getPageSize()));
    }

    public TopicCommentDetailOneVo formartTopicCommentDetailOneVo(AuthenticatedUser authenticatedUser, Comment comment) {
        TopicCommentDetailOneVo topicCommentDetailOneVo = new TopicCommentDetailOneVo();
        topicCommentDetailOneVo.setId(comment.getId());
        topicCommentDetailOneVo.setUserId(comment.getUserId());
        User user = userDao.findUserByIdIs(comment.getUserId());
        topicCommentDetailOneVo.setUserName(user.getUsername() == null ? "待处理第三方登录" : user.getUsername());
        topicCommentDetailOneVo.setUserIcon(user.getHeadPortrait());
        topicCommentDetailOneVo.setContent(comment.getContent());
        topicCommentDetailOneVo.setContentMD(comment.getContentMD());
        topicCommentDetailOneVo.setTime(TimeUtils.formartDateToString(comment.getInTime()));
        if (comment.getUserId().equals(authenticatedUser.getUserId())) {
            topicCommentDetailOneVo.setEdit(true);
            topicCommentDetailOneVo.setReply(false);
        } else {
            topicCommentDetailOneVo.setEdit(false);
            topicCommentDetailOneVo.setReply(true);
        }
        //处理默认账号
        if ("5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())) {
            topicCommentDetailOneVo.setEdit(false);
            topicCommentDetailOneVo.setReply(true);
        }
        if (EmptyUtil.isEmpty(comment.getCommentId())) {
            topicCommentDetailOneVo.setLayer(0);
        } else {
            Comment commentIdIs = commentDao.findOne(comment.getCommentId());
            User userByIdIs = userDao.findUserByIdIs(commentIdIs.getUserId());
            topicCommentDetailOneVo.setLayer(1);
            topicCommentDetailOneVo.setReplyToUserId(userByIdIs.getId());
            topicCommentDetailOneVo.setReplyToUserName(userByIdIs.getUsername());
        }
        return topicCommentDetailOneVo;
    }

    public Result judge(AuthenticatedUser authenticatedUser) {
        Buttun buttun = new Buttun();
        buttun.setName("发布主题");
        if (!"5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())) {
            buttun.setShow(true);
        }
        return Result.success(buttun);
    }

    @Override
    public Result getTopic(AuthenticatedUser authenticatedUser, String topicId) {
        TopicDetailVo topicDetailVo = new TopicDetailVo();
        try {
            if (EmptyUtil.isEmpty(topicId)) {
                return Result.success().message("话题的id不能为空");
            }
            Topic topic = topicDao.findByIdIs(topicId);
            if (EmptyUtil.isEmpty(topic)) {
                return Result.success().message("话题可能被删了");
            }
            BeanUtils.copyProperties(topic, topicDetailVo);
            topicDetailVo.setModifyTime(TimeUtils.formartDateToString(topic.getModifyTime()));
            Tag tag = tagDao.findByIdIs(topic.getTagId());
            topicDetailVo.setTagName(tag.getTitle());
            User userByIdIs = userDao.findUserByIdIs(topic.getUserId());
            topicDetailVo.setUserName(userByIdIs.getUsername());
            topicDetailVo.setUserIcon(userByIdIs.getHeadPortrait());

            //处理统计
            Integer view = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topic.getId(), "view"));
            topicDetailVo.setView(view == null ? 0 : view);
            Integer up = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topic.getId(), "up"));
            topicDetailVo.setUp(up == null ? 0 : up);
            Integer commentCount = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topic.getId(), "comment"));
            topicDetailVo.setCommentCount(commentCount == null ? 0 : commentCount);


            //处理自己是否赞了
            String ups = topic.getUpIds();
            if (ups != null && ups.contains(authenticatedUser.getUserId())) {
                topicDetailVo.setUpSelf(true);
            }
            //记录回帖数
            topicAddInfo(topicId, "view");
            tagAddInfo(topic.getTagId(), "view");
            return Result.success(topicDetailVo);
        } catch (BeansException e) {
            log.info("BbsServiceImpl getTopic异常", e.getMessage());
            return Result.success(topicDetailVo).message("网络异常请稍后再试");
        }
    }

    @Override
    public Result saveTopic(AuthenticatedUser authenticatedUser, TopicSaveVo topicSaveVo) {
        try {
            if ("5dcfbc5b8ef6d314f07a41e3".equals(authenticatedUser.getUserId())) {
                return Result.fail("请先登录");
            }
            if (EmptyUtil.isEmpty(topicSaveVo.getTitle())) {
                return Result.success().message("话题的标题不能为空");
            }
            if (EmptyUtil.isEmpty(topicSaveVo.getContent())) {
                return Result.success().message("话题的内容不能为空");
            }
            if (EmptyUtil.isEmpty(topicSaveVo.getTagId())) {
                return Result.success().message("请选择话题的模块");
            }
            Topic topic = new Topic();
            if (EmptyUtil.isNotEmpty(topicSaveVo.getTopicId())) {
                topic = topicDao.findByIdIs(topicSaveVo.getTopicId());
                topic.setContent(topicSaveVo.getContent());
                topic.setContentMD(topicSaveVo.getContentMD());
                topic.setTagId(topicSaveVo.getTagId());
                topic.setTitle(topicSaveVo.getTitle());
                topic.setModifyTime(new Date());
            } else {
                topic.setContent(topicSaveVo.getContent());
                topic.setContentMD(topicSaveVo.getContentMD());
                topic.setTagId(topicSaveVo.getTagId());
                topic.setTitle(topicSaveVo.getTitle());
                topic.setUserId(authenticatedUser.getUserId());
                topic.setCreatTime(new Date());
                topic.setModifyTime(new Date());
            }
            Topic topic1 = topicDao.save(topic);
            if (topic1 != null) {
                //统计模块的topic
                if (EmptyUtil.isEmpty(topicSaveVo.getTopicId())) {
                    tagAddInfo(topic1.getTagId(), "topic");
                    integralServiceImpl.addRecord(authenticatedUser,2);
                }
                Topic topic2 = new Topic();
                topic2.setId(topic.getId());
                return Result.success(topic2).message("话题已保存");
            }
        } catch (Exception e) {
            return Result.success().message("网络异常，请稍后再试");
        }
        return Result.success().message("网络异常，请稍后再试");
    }

    @Override
    public Result getUserTopicList(AuthenticatedUser authenticatedUser, PagedQueryParam pagedQueryParam) {
        List<UserInfoTopicVo> topicVos = new ArrayList<>();
        Page<Topic> topicPage = topicDao.findByUserIdIsOrderByCreatTimeDesc(authenticatedUser.getUserId(), new PageRequest(pagedQueryParam.getPage(),
                pagedQueryParam.getPageSize(),
                Sort.Direction.DESC,
                pagedQueryParam.getSortBy()));
        topicPage.getContent().forEach(topic -> {
            UserInfoTopicVo topicVo = new UserInfoTopicVo();
            BeanUtils.copyProperties(topic, topicVo);
            //处理统计
            Integer view = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicVo.getId(), "view"));
            topicVo.setView(view == null ? 0 : view);
            Integer up = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicVo.getId(), "up"));
            topicVo.setUp(up == null ? 0 : up);
            Integer commentCount = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicVo.getId(), "comment"));
            topicVo.setCommentCount(commentCount == null ? 0 : commentCount);
            topicVo.setModifyTime(TimeUtils.convertDateToStringyd(topic.getModifyTime()));
            topicVos.add(topicVo);
        });
        Page<UserInfoTopicVo> topicVos1 = PageUtil.builidPage(topicVos, topicPage.getTotalElements(), pagedQueryParam.getPage(), pagedQueryParam.getPageSize());
        return Result.success(topicVos1);
    }

    @Override
    public Result getTopicList(TopicQueryParam topicQueryParam) {
        TopicListVo topicListVo = new TopicListVo();
        List<TopicVo> topicVos = new ArrayList<>();
        Page<Topic> topicPage;
        if (EmptyUtil.isNotEmpty(topicQueryParam.getTagId())) {
            if (topicQueryParam.getPageType() != null && topicQueryParam.getPageType() == 2) {
                //热度 根据hotValue 排序
                topicPage = topicDao.findByTagIdIsOrderByHootValueDesc(topicQueryParam.getTagId(), new PageRequest(topicQueryParam.getPage(),
                        topicQueryParam.getPageSize(),
                        Sort.Direction.DESC,
                        topicQueryParam.getSortBy()));
            } else {
                //根据时间排序
                topicPage = topicDao.findByTagIdIsOrderByCreatTimeDesc(topicQueryParam.getTagId(), new PageRequest(topicQueryParam.getPage(),
                        topicQueryParam.getPageSize(),
                        Sort.Direction.DESC,
                        topicQueryParam.getSortBy()));
            }

        } else {
            if (topicQueryParam.getPageType() != null && topicQueryParam.getPageType() == 2) {
                //热度 根据hotValue 排序
                topicPage = topicDao.findByOrderByHootValueDesc(new PageRequest(topicQueryParam.getPage(),
                        topicQueryParam.getPageSize(),
                        Sort.Direction.DESC,
                        topicQueryParam.getSortBy()));
            } else {
                //根据时间排序
                topicPage = topicDao.findByOrderByCreatTimeDesc(new PageRequest(topicQueryParam.getPage(),
                        topicQueryParam.getPageSize(),
                        Sort.Direction.DESC,
                        topicQueryParam.getSortBy()));
            }
        }
        topicPage.getContent().forEach(topic -> {
            TopicVo topicVo = new TopicVo();
            BeanUtils.copyProperties(topic, topicVo);
            //处理统计
            Integer view = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicVo.getId(), "view"));
            topicVo.setView(view == null ? 0 : view);
            Integer up = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicVo.getId(), "up"));
            topicVo.setUp(up == null ? 0 : up);
            Integer commentCount = (Integer) redisTemplate.opsForValue().get(String.format("topic:%s_%s:topic_id", topicVo.getId(), "comment"));
            topicVo.setCommentCount(commentCount == null ? 0 : commentCount);
            topicVo.setModifyTime(TimeUtils.formartDateToString(topic.getModifyTime()));

            /*String ups = topic.getUpIds();
            if (ups != null && ups.contains(",")) {
                topicVo.setUp(ups.split(",").length);
            }*/
            Tag byIdIs = tagDao.findByIdIs(topic.getTagId());
            topicVo.setTagName(byIdIs.getTitle());
            topicVo.setTagIcon(byIdIs.getIcon());
            topicListVo.setTagid(byIdIs.getId());
            topicListVo.setTagName(byIdIs.getTitle());
            //设定话题头像
            User userByIdIs = userDao.findUserByIdIs(topic.getUserId());
            topicVo.setTopicIcon(userByIdIs.getHeadPortrait());
            topicVo.setUserName(userByIdIs.getUsername() == null ? "" : userByIdIs.getUsername());
            Comment comment = commentDao.findFirstByTopicIdIsOrderByInTimeDesc(topic.getId());
            TopicCommentVo topicCommentVo = new TopicCommentVo();
            if (comment != null) {
                BeanUtils.copyProperties(comment, topicCommentVo);
                User user = userDao.findUserByIdIs(comment.getUserId());
                topicCommentVo.setUserName(user.getUsername());
                topicCommentVo.setIcon(user.getHeadPortrait());
                topicCommentVo.setTime(TimeUtils.formartDateToString(comment.getInTime()));
                //改用MD
                topicCommentVo.setContentMD(comment.getContentMD());
                topicCommentVo.setContent(comment.getContent());
                topicVo.setTopicCommentVo(topicCommentVo);
            }
            topicVos.add(topicVo);
        });
        Page<TopicVo> topicVos1 = PageUtil.builidPage(topicVos, topicPage.getTotalElements(), topicQueryParam.getPage(), topicQueryParam.getPageSize());
        topicListVo.setTopicVos(topicVos1);
        return Result.success(topicListVo);
    }

    @Override
    public Result getTagList() {
        List<TagVo> tagVos = new ArrayList<>();
        try {
            tagDao.findAll().forEach(tag -> {
                TagVo tagVo = new TagVo();
                BeanUtils.copyProperties(tag, tagVo);
                //增加 统计
                Integer comment = (Integer) redisTemplate.opsForValue().get(String.format("tag:%s_%s:tag_id", tagVo.getId(), "comment"));
                tagVo.setCommentCount(comment == null ? 0 : comment);
                Integer view = (Integer) redisTemplate.opsForValue().get(String.format("tag:%s_%s:tag_id", tagVo.getId(), "view"));
                tagVo.setView(view == null ? 0 : view);
                Integer topicCount = (Integer) redisTemplate.opsForValue().get(String.format("tag:%s_%s:tag_id", tagVo.getId(), "topic"));
                tagVo.setTopicCount(topicCount == null ? 0 : topicCount);

                Topic topic = topicDao.findFirstByTagIdIsOrderByCreatTimeDesc(tag.getId());
                if (topic != null) {
                    User userByIdIs = userDao.findUserByIdIs(topic.getUserId());
                    TagTopicVo tagTopicVo = new TagTopicVo();
                    tagTopicVo.setId(topic.getId());
                    tagTopicVo.setTitle(topic.getTitle());
                    tagTopicVo.setIcon(userByIdIs.getHeadPortrait());
                    //改用MD
                    tagTopicVo.setContentMD(topic.getContentMD());
                    tagTopicVo.setContent(topic.getContent());
                    tagTopicVo.setUserName(userByIdIs.getUsername());
                    tagTopicVo.setUserId(userByIdIs.getId());
                    tagTopicVo.setModifyTime(TimeUtils.formartDateToString(topic.getModifyTime()));
                    tagVo.setTagTopicVo(tagTopicVo);
                }
                tagVos.add(tagVo);
            });
        } catch (Exception e) {
            log.error("BbsServiceImpl getTagList查询异常" + e.getMessage());
        }
        return Result.success(tagVos);
    }

    @Override
    public Result getTagCombobox() {
        List<Tag> tags = new ArrayList<>();
        try {
            tags = tagDao.findAll();
        } catch (Exception e) {
            log.error("BbsServiceImpl getTagCombobox查询异常" + e.getMessage());
        }
        return Result.success(tags);
    }
}
