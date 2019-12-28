package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;


@Data
public class TopicQueryParam extends PagedQueryParam {
    private String tagId;//模块id 默认为null 返回所有模块的数据
    private Integer pageType;//0：模块页面 1:最新页面  2:最热页面
}
