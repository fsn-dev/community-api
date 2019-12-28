package xyz.yunzhongyan.www.service;

import xyz.yunzhongyan.www.domain.vo.DocumentSave;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;
import xyz.yunzhongyan.www.domain.vo.Result;

public interface DocumentService {

    Result addDocument(DocumentSave document);

    Result getTree(String keyword);

    Result getDocumentbyId(String id);

    Result getDocumentListBack(PagedQueryParam pagedQueryParam);

    Result deleteDocument(String id);
}
