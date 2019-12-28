package xyz.yunzhongyan.www.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    private String id;
    private String thumbPath;
    private List<String> keyWord;
    private String articleTitle;
    private String articleContiner;
    private String articleContinerMD;
    private String author;
    private String time;

}
