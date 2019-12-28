package xyz.yunzhongyan.www.domain.vo;

import lombok.Data;

@Data
public class PagedQueryParam {
    protected Integer page = 0;
    protected Integer pageSize = Integer.MAX_VALUE;
    protected String order = ORDER_DESC;
    protected String sortBy = SORT_BY_ID;

    public static final String ORDER_DESC = "desc";
    public static final String ORDER_ASC = "asc";
    public static final String SORT_BY_ID = "id";
}