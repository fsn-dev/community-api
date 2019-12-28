package xyz.yunzhongyan.www.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleQuery {
    @ApiModelProperty(value = "每页数量" ,name = "pageSize")
    private Integer pageSize;
    @ApiModelProperty(value = "当前页" ,name = "pageNum")
    private Integer pageIndex;
    @ApiModelProperty(value = "关键字" ,name = "keyWord")
    private String keyWord;
}
