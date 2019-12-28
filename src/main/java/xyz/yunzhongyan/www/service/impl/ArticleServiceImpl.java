package xyz.yunzhongyan.www.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.dao.mongo.ArticleDao;
import xyz.yunzhongyan.www.domain.po.Article;
import xyz.yunzhongyan.www.domain.vo.*;
import xyz.yunzhongyan.www.service.ArticleService;
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
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public Result getArticlebyId(String id) {
        ArticleDetailVo articleDetailVo = new ArticleDetailVo();

        Article article = articleDao.findByIdIs(id);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        articleVo.setArticleContiner(article.getArticleContiner());
        articleVo.setArticleContinerMD(article.getArticleContinerMD());
        articleVo.setAuthor(article.getAuthor());
        articleVo.setThumbPath(article.getThumbPath());
        articleVo.setArticleTitle(article.getArticleTitle());
        articleVo.setTime(TimeUtils.convertDateToStringys(article.getCreateTime()));
        String keyWord = article.getKeyWord();
        if (keyWord != null && keyWord.contains(",")) {
            articleVo.setKeyWord(Arrays.asList(keyWord.split(",")));
        } else {
            articleVo.setKeyWord(Arrays.asList(keyWord == null ? "" : keyWord));
        }
        Article greate = articleDao.findFirstByCreateTimeGreaterThanOrderByCreateTimeAsc(article.getCreateTime());

        Article less = articleDao.findFirstByCreateTimeLessThanOrderByCreateTimeDesc(article.getCreateTime());
        ArticleVo2 lessVo = new ArticleVo2();
        ArticleVo2 greateVo = new ArticleVo2();
        if (greate!=null){
            BeanUtils.copyProperties(greate,greateVo);
            greateVo.setShow(true);
        }
        if (less!=null){
            BeanUtils.copyProperties(less,lessVo);
            lessVo.setShow(true);
        }
        articleDetailVo.setDetail(articleVo);
        articleDetailVo.setLastArticle(lessVo);
        articleDetailVo.setNextArticle(greateVo);
        return Result.success(articleDetailVo);
    }

    @Override
    public Result getArticleList(PagedQueryParam pagedQueryParam) {
        Page<Article> byOrderByCreatTimeDesc = articleDao.findByOrderByCreateTimeDesc(new PageRequest(pagedQueryParam.getPage(),
                pagedQueryParam.getPageSize(),
                Sort.Direction.DESC,
                pagedQueryParam.getSortBy()));
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : byOrderByCreatTimeDesc.getContent()) {
            ArticleVo articleVo = new ArticleVo();
            articleVo.setId(article.getId());
            articleVo.setArticleContiner(article.getArticleContiner());
            articleVo.setArticleContinerMD(article.getArticleContinerMD());
            articleVo.setAuthor(article.getAuthor());
            articleVo.setThumbPath(article.getThumbPath());
            articleVo.setArticleTitle(article.getArticleTitle());
            articleVo.setTime(TimeUtils.convertDateToStringys(article.getCreateTime()));
            String keyWord = article.getKeyWord();
            if (keyWord != null && keyWord.contains(",")) {
                articleVo.setKeyWord(Arrays.asList(keyWord.split(",")));
            } else {
                articleVo.setKeyWord(Arrays.asList(keyWord == null ? "" : keyWord));
            }
            articleVos.add(articleVo);
        }
        return Result.success(PageUtil.builidPage(articleVos, byOrderByCreatTimeDesc.getTotalElements(), pagedQueryParam.getPage(), pagedQueryParam.getPageSize()));
    }

    @Override
    public Result getArticleListBack(PagedQueryParam pagedQueryParam) {
        Page<Article> byOrderByCreatTimeDesc = articleDao.findByOrderByCreateTimeDesc(new PageRequest(pagedQueryParam.getPage(),
                pagedQueryParam.getPageSize(),
                Sort.Direction.DESC,
                pagedQueryParam.getSortBy()));
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : byOrderByCreatTimeDesc.getContent()) {
            ArticleVo articleVo = new ArticleVo();
            articleVo.setId(article.getId());
            articleVo.setAuthor(article.getAuthor());
            articleVo.setArticleTitle(article.getArticleTitle());
            articleVo.setTime(TimeUtils.convertDateToStringys(article.getCreateTime()));
            articleVos.add(articleVo);
        }
        return Result.success(PageUtil.builidPage(articleVos, byOrderByCreatTimeDesc.getTotalElements(), pagedQueryParam.getPage(), pagedQueryParam.getPageSize()));

    }

    @Override
    public Result getArticlebyIdBack(String id) {
        Article article = articleDao.findByIdIs(id);
        return Result.success(article);
    }

    @Override
    public Result addArticleNew(ArticleSave article) {
        try {
            Article article1 = new Article();
            article1.setArticleContiner(article.getArticleContiner());
            article1.setArticleContinerMD(article.getArticleContinerMD());
            article1.setArticleTitle(article.getArticleTitle());
            article1.setAuthor(article.getAuthor());
            article1.setKeyWord(article.getKeyWord());
            if (EmptyUtil.isEmpty(article.getThumbPath())) {
                article1.setThumbPath("http://49.235.37.9:1186/demo.jpg");
            } else {
                article1.setThumbPath(article.getThumbPath());
            }
            if (EmptyUtil.isNotEmpty(article.getId())) {
                article1.setId(article.getId());
            }
            article1.setCreateTime(new Date());
            Article save = articleDao.save(article1);
            if (save != null) {
                return Result.success(save).message("发布成功");
            }
        } catch (BeansException e) {
            return Result.fail("网络异常请稍后再试");
        }
        return Result.success().message("网络异常请稍后再试");
    }

    @Override
    public Result addArticle(Article article) {
        article.setCreateTime(new Date());
        Article article1 = articleDao.insert(article);
        if (article1 != null) {
            Result.success("插入成功，插入的数据为 " + article1);
        } else {
            Result.fail("插入失败，请检查原因");
        }
        return null;
    }

    @Override
    public Result updateArticle(Article article) {
        article.setCreateTime(new Date());
        Article article1 = articleDao.save(article);
        if (article1 != null) {
            Result.success("更新成功，更新后的数据为 " + article1);
        } else {
            Result.fail("更新失败");
        }
        return null;
    }

    @Override
    public Result deleteArticle(String id) {
        try {
            articleDao.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("删除数据" + id + "失败 !!!");
        }
    }

    @Override
    public Result selectArticleById(String id) {
        return Result.success(articleDao.findArticleByIdIs(id));
    }

    @Override
    public Result findAllByPage(Integer pageIndex, Integer pageSize) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
        Sort sort = new Sort(order);
        List<Article> articleList = articleDao.findAll(sort);
        if (pageIndex == null || pageIndex == 0) {
            pageIndex = 1;
        } else if (pageSize == null) {
            pageSize = 10;
        }
        return Result.success(PageUtil.pageNew(articleList, pageIndex, pageSize));
    }

    @Override
    public Result findAllByKeyWordLike(Integer pageIndex, Integer pageSize, String keyWord) {
        if (pageIndex == null || pageIndex == 0) {
            pageIndex = 1;
        } else if (pageSize == null) {
            pageSize = 10;
        }
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
        Sort sort = new Sort(order);
        List<Article> articleList = articleDao.findAllByArticleContinerLike(keyWord, sort);
        return Result.success(PageUtil.pageNew(articleList, pageIndex, pageSize));
    }
}
