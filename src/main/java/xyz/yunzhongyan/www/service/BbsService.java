package xyz.yunzhongyan.www.service;

import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.domain.vo.bbs.CommentSaveVo;
import xyz.yunzhongyan.www.domain.vo.bbs.CommentUpdateVo;
import xyz.yunzhongyan.www.domain.vo.bbs.TopicQueryParam;
import xyz.yunzhongyan.www.domain.vo.bbs.TopicSaveVo;


/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */
public interface BbsService {

    Result saveVote(AuthenticatedUser authenticatedUser, String topicId);

    Result updateComment(AuthenticatedUser authenticatedUser, CommentUpdateVo commentUpdateVo);

    Result saveComment(AuthenticatedUser authenticatedUser, CommentSaveVo commentSaveVo);

    Result getComments(AuthenticatedUser authenticatedUser, String topicId, PagedQueryParam pagedQueryParam);

    Result getTopic(AuthenticatedUser authenticatedUser, String topicId);

    /**
     * 保存、更新话题
     *
     * @return
     */
    Result saveTopic(AuthenticatedUser authenticatedUser, TopicSaveVo topicSaveVo);


    Result getUserTopicList(AuthenticatedUser authenticatedUser, PagedQueryParam pagedQueryParam);

    /**
     * 查看主题列表
     *
     * @return
     */
    Result getTopicList(TopicQueryParam topicQueryParam);

    /**
     * 模块列表查看
     *
     * @return
     */
    Result getTagList();

    /**
     * 模块下拉框
     *
     * @return
     */
    Result getTagCombobox();
}
