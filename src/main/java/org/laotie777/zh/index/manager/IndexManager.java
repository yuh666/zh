package org.laotie777.zh.index.manager;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.laotie777.zh.index.bean.IndexBean;
import org.laotie777.zh.index.bean.IndexConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author yuh
 * @Date Created in 下午1:49 2018/2/9
 * @Description 索引管理类
 */
public class IndexManager {
    private static Logger logger = Logger.getLogger(IndexManager.class);
    /**
     * 写索引
     */
    private IndexWriter indexWriter;
    /**
     * 这个用来检测到索引更新 用来检测索引更新情况
     */
    private NRTManager.TrackingIndexWriter trackingIndexWriter;
    /**
     * 索引文件采用的分词器
     */
    private Analyzer analyzer;
    /**
     * 管理内存索引和磁盘索引
     */
    private NRTManager nrtManager;
    /**
     * 内存索引重读线程
     */
    private NRTManagerReopenThread nrtManagerReopenThread;
    /**
     * 自定义索引更新线程
     */
    private IndexCommitThread indexCommitThread;
    /**
     * 索引地址
     */
    private String indexPath;
    /**
     * 索引重读最大、最小时间间隔
     */
    private double indexReopenMaxStaleSec;
    /**
     * 索引重读最大、最小时间间隔
     */
    private double indexReopenMinStaleSec;
    /**
     * 索引commit时间
     */
    private int indexCommitSeconds;
    /**
     * 索引名称
     */
    private String indexManagerName;
    /**
     * commit时是否输出相关信息
     */
    private boolean bprint = true;


    private IndexManager(IndexBean indexBean) {
        this.analyzer = indexBean.getAnalyzer();
        this.indexPath = indexBean.getIndexPos();
        this.indexManagerName = indexBean.getIndexName();
        this.indexReopenMaxStaleSec = indexBean.getMaxIndexReopenGap();
        this.indexReopenMinStaleSec = indexBean.getMinIndexReopenGap();
        //构造主要成员
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        Directory directory = null;
        try {
            directory = NIOFSDirectory.open(new File(indexPath + indexManagerName));
            if (IndexWriter.isLocked(directory)) {
                IndexWriter.unlock(directory);
            }
            this.indexWriter = new IndexWriter(directory, indexWriterConfig);
            this.trackingIndexWriter = new NRTManager.TrackingIndexWriter(this.indexWriter);
            this.nrtManager = new NRTManager(this.trackingIndexWriter, new SearcherFactory());
            this.setThead();
        } catch (Exception e) {
            logger.error("索引目录打开失败 => {}", e);
            throw new RuntimeException("索引目录打开失败");
        }
    }

    /**
     * 首次加载所有索引管理类
     */
    private static class LazyLoadManager {
        private static final HashMap<String, IndexManager> managers = new HashMap<>();

        static {
            for (IndexBean bean : IndexConfig.getConfig()) {
                managers.put(bean.getIndexName(), new IndexManager(bean));
            }
        }
    }

    /**
     * 启动两个守护线程
     */
    private void setThead() {
        //开始内存reader重开线程
        this.nrtManagerReopenThread = new NRTManagerReopenThread(this.nrtManager, indexReopenMaxStaleSec, indexReopenMinStaleSec);
        this.nrtManagerReopenThread.setName(String.format("reopen daemon thread for %s", indexManagerName));
        //优先级比当前线程高点
        this.nrtManagerReopenThread.setPriority(Math.min(Thread.currentThread().getPriority() + 2, Thread.MAX_PRIORITY));
        this.nrtManagerReopenThread.setDaemon(true);
        this.nrtManagerReopenThread.start();
        //开始定时提交线程
        IndexCommitThread indexCommitThread = new IndexCommitThread(String.format("commit daemon thread for %s", indexManagerName));
        indexCommitThread.setDaemon(true);
        indexCommitThread.start();
    }

    private class IndexCommitThread extends Thread {
        boolean flag;

        public IndexCommitThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            flag = true;
            while (true) {
                try {
                    indexWriter.commit();
                    if (IndexManager.this.bprint) {
                        logger.info("提交索引更新");
                    }
                    TimeUnit.SECONDS.sleep(indexCommitSeconds);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }

    /**
     * 重新打开索引
     * @return
     */
    public String setCommitThread(){
        if(indexCommitThread.isAlive()){
            return "is alive";
        }
        //开始定时提交线程
        IndexCommitThread indexCommitThread = new IndexCommitThread(String.format("commit daemon thread for %s", indexManagerName));
        indexCommitThread.setDaemon(true);
        indexCommitThread.start();
        return indexManagerName;
    }

    /**
     * 索引管理器
     * @param name
     * @return
     */
    public IndexManager getIndexManagerByName(String name){
        return LazyLoadManager.managers.get(name);
    }

    /**
     * 获得检索器
     * @return
     * @throws IOException
     */
    public IndexSearcher getIndexSearcher() throws IOException {
        return nrtManager.acquire();
    }

    /**
     * TrackingIndexWriter
     * @return
     */
    public NRTManager.TrackingIndexWriter getTrackingIndexWriter() {
        return trackingIndexWriter;
    }

    /**
     * 分析器
     * @return
     */
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * 索引中的记录条数 标记删除的不算
     * @return
     */
    public int getIndexNum(){
        return indexWriter.numDocs();
    }

}
