package xyz.yunzhongyan.www.service;

import xyz.yunzhongyan.www.domain.po.Article;
import xyz.yunzhongyan.www.domain.vo.ArticleSave;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;

public interface ArticleService {
    Result addArticle(Article article);

    Result updateArticle(Article article);

    Result deleteArticle(String id);

    Result findAllByPage(Integer pageIndex, Integer pageSize);

    Result findAllByKeyWordLike(Integer pageIndex, Integer pageSize, String keyWord);

    Result selectArticleById(String id);


    //New
    Result getArticlebyId(String id);

    Result getArticlebyIdBack(String id);

    Result getArticleList(PagedQueryParam pagedQueryParam);

    Result getArticleListBack(PagedQueryParam pagedQueryParam);

    Result addArticleNew(ArticleSave article);
}
