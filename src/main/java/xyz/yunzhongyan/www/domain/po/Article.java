package xyz.yunzhongyan.www.domain.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Document(collection = "article")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id主键" ,name = "id")
    private String id;
    @ApiModelProperty(value = "缩略图路径" ,name = "thumbPath")
    private String thumbPath;
    @ApiModelProperty(value = "关键字" ,name = "keyWord")
    private String keyWord;
    @ApiModelProperty(value = "文章标题" ,name = "articleTitle")
    private String articleTitle;
    @ApiModelProperty(value = "技术文章" ,name = "articleContiner")
    private String articleContiner;
    @ApiModelProperty(value = "技术文章MD" ,name = "articleContinerMD")
    private String articleContinerMD;
    @ApiModelProperty(value = "作者" ,name = "author")
    private String author;

    @ApiModelProperty(value = "创建时间" ,name = "createTime")
    private Date createTime;
}
