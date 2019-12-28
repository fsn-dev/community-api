package xyz.yunzhongyan.www.domain.vo;

import lombok.Data;


@Data
public class ArticleSave {
    private String id;
    private String thumbPath;
    private String keyWord;
    private String articleTitle;
    private String articleContiner;
    private String articleContinerMD;
    private String author;
}
