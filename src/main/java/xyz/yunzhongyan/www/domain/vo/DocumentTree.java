package xyz.yunzhongyan.www.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class DocumentTree {
    private String title;
    private List<DocumentTree2> list;
}
