package org.laotie777.zh.index.operation;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.laotie777.zh.index.manager.IndexManager;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 下午6:24 2018/2/9
 * @Description
 */
public class NrtIndex {

    private Logger logger = Logger.getLogger(NrtIndex.class);

    /**
     * 索引位置
     */
    private String indexName;
    /**
     * 执行增删改的writer
     */
    private NRTManager.TrackingIndexWriter trackingIndexWriter;

    public NrtIndex(String indexName){
        this.indexName = indexName;
        this.trackingIndexWriter = IndexManager.getIndexManagerByName(indexName).getTrackingIndexWriter();
    }

    /**
     * 添加文档
     * @param document
     */
    public void addDocument(Document document){
        try{
            trackingIndexWriter.addDocument(document);
        }catch (Exception e){
            logger.error("添加文档失败 => {}",e);
        }
    }

    /**
     * 更新文档
     * @param document
     * @param term
     */
    public void updateDocument(Document document, Term term){
        try{
            trackingIndexWriter.updateDocument(term,document);
        }catch (Exception e){
            logger.error("更新文档失败 => {}",e);
        }
    }

    /**
     * 删除文档
     * @param query
     */
    public void deleteDocument(Query query){
        try{
            trackingIndexWriter.deleteDocuments(query);
        }catch (Exception e){
            logger.error("删除文档失败 => {}",e);
        }
    }

    /**
     * 删除全部索引
     */
    public void deleteAll(){
        try{
            trackingIndexWriter.deleteAll();
        }catch (Exception e){
            logger.error("删除全部失败 => {}",e);
        }
    }

    /**
     * 提交索引
     */
    public void commit(){
        try {
            IndexManager.getIndexManagerByName(indexName).getIndexWriter().commit();
        } catch (IOException e) {
            logger.error("提交索引 => {}",e);
        }
    }

}
