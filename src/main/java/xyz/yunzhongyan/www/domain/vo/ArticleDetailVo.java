package xyz.yunzhongyan.www.domain.vo;

import lombok.Data;

@Data
public class ArticleDetailVo {

    private ArticleVo detail;
    private ArticleVo2 lastArticle;
    private ArticleVo2 nextArticle;
}
