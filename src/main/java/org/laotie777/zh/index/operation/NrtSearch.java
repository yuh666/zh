package org.laotie777.zh.index.operation;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.*;
import org.laotie777.zh.index.bean.ResultEntity;
import org.laotie777.zh.index.manager.IndexManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author yuh
 * @Date Created in 下午4:42 2018/2/9
 * @Description 搜索的具体实现
 */
public class NrtSearch {

    private IndexManager indexManager;

    public NrtSearch(String indexName) {
        this.indexManager = IndexManager.getIndexManagerByName(indexName);

    }

    /**
     * 分页搜索排序
     *
     * @param query
     * @param start
     * @param end
     * @param sort
     * @return
     */
    public ResultEntity queryWithSort(Query query, int start, int end, Sort sort) throws IOException {
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        TopDocs docs = indexSearcher.search(query, end, sort);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCount(docs.totalHits);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        end = end > docs.totalHits ? docs.totalHits : end;
        ArrayList<Document> documents = new ArrayList<>();
        for (int i = start; i < end; i++) {
            documents.add(indexSearcher.doc(scoreDocs[i].doc));
        }
        return resultEntity;
    }


    /**
     * 分页搜索
     *
     * @param query
     * @param start
     * @param end
     * @param sort
     * @return
     */
    public ResultEntity query(Query query, int start, int end) throws IOException {
        return queryWithSort(query, start, end, null);
    }

    /**
     * 按照文档序号检索
     * @param start
     * @param count
     * @return
     * @throws IOException
     */
    public ResultEntity queryCount(int start, int count) throws IOException {
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCount(count);

        ArrayList<Document> documents = new ArrayList<>();
        for (int i = start; i < count; i++) {
            documents.add(indexSearcher.doc((start + i) % indexManager.getIndexNum()));
        }
        return resultEntity;
    }


}
