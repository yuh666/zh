package org.laotie777.zh.index.operation;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
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
public class NRTSearch {

    private IndexManager indexManager;

    public NRTSearch(String indexName) {
        this.indexManager = IndexManager.getIndexManagerByName(indexName);

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
    public ResultEntity query(Query query, int start, int end, Sort sort) throws IOException {
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        TopDocs docs = indexSearcher.search(query, end);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCount(docs.totalHits);
        end = end > docs.totalHits ? docs.totalHits : end;
        ArrayList<Document> documents = new ArrayList<>();
        return null;
    }
}
