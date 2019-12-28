package xyz.yunzhongyan.www.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.dao.mongo.DocumentDao;
import xyz.yunzhongyan.www.domain.po.Article;
import xyz.yunzhongyan.www.domain.po.Document;
import xyz.yunzhongyan.www.domain.vo.*;
import xyz.yunzhongyan.www.service.DocumentService;
import xyz.yunzhongyan.www.util.EmptyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @program: api
 * @description:
 * @author: wuxinghan
 * @create: 2019-11-12 15:27
 **/

@Component
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentDao documentDao;

    @Override
    public Result getDocumentListBack(PagedQueryParam pagedQueryParam) {
        Page<Document> byOrderByCreatTimeDesc = documentDao.findByOrderByIdDesc(new PageRequest(pagedQueryParam.getPage(),
                pagedQueryParam.getPageSize(),
                Sort.Direction.DESC,
                pagedQueryParam.getSortBy()));
        return Result.success(byOrderByCreatTimeDesc);
    }

    @Override
    public Result getDocumentbyId(String id) {
        Document one = documentDao.findOne(id);
        return Result.success(one);
    }

    @Override
    public Result deleteDocument(String id) {
        try {
            documentDao.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("删除数据" + id + "失败 !!!");
        }
    }

    @Override
    public Result addDocument(DocumentSave document) {
        Document document1 = new Document();
        document1.setContentMD(document.getContentMD());
        document1.setContentHTML(document.getContentHTML());
        document1.setTitle(document.getTitle());
        if (EmptyUtil.isNotEmpty(document.getId())) {
            document1.setId(document.getId());
        }
        Document save = documentDao.save(document1);
        return Result.success(save).message("发布成功");
    }

    @Override
    public Result getTree(String keyword) {
        List<DocumentTree> documentTrees = new ArrayList<>();
        List<Document> all = documentDao.findAll();
        if (EmptyUtil.isNotEmpty(keyword)) {
            all = all.stream().filter(document -> (document.getTitle() != null && document.getTitle().contains(keyword))).collect(Collectors.toList());
        }
        DocumentTree documentTree = new DocumentTree();
        documentTree.setTitle("技术文档");
        List<DocumentTree2> documentTree2 = new ArrayList<>();
        for (Document document : all) {
            DocumentTree2 documentTree21 = new DocumentTree2();
            documentTree21.setId(document.getId());
            documentTree21.setTitle(document.getTitle());
            documentTree2.add(documentTree21);
        }
        documentTree.setList(documentTree2);
        documentTrees.add(documentTree);
        return Result.success(documentTrees);
    }
}
