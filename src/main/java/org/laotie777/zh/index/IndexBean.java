package org.laotie777.zh.index;

import org.apache.lucene.analysis.Analyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.Objects;

/**
 * @Author yuh
 * @Date Created in 下午12:48 2018/2/9
 * @Description 索引配置类
 */
public class IndexBean {
    /**
     * IK分词器
     */
    private Analyzer analyzer = new IKAnalyzer();

    /**
     * indexReader重新开启的最小时间间隔
     */
    private double minIndexReopenGap = 0.025;
    /**
     * indexReader重新开启的最大时间间隔
     */
    private double maxIndexReopenGap = 0.60;

    /**
     * 索引位置
     */
    private String indexPos = "./index/";

    /**
     * 索引名称
     */
    private String indexName = "index";

    /**
     * 显示commit的细节
     */
    private boolean bprint = true;

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public double getMinIndexReopenGap() {
        return minIndexReopenGap;
    }

    public void setMinIndexReopenGap(double minIndexReopenGap) {
        this.minIndexReopenGap = minIndexReopenGap;
    }

    public double getMaxIndexReopenGap() {
        return maxIndexReopenGap;
    }

    public void setMaxIndexReopenGap(double maxIndexReopenGap) {
        this.maxIndexReopenGap = maxIndexReopenGap;
    }

    public String getIndexPos() {
        return indexPos;
    }

    public void setIndexPos(String indexPos) {
       if(!(indexPos.endsWith("\\") || indexPos.endsWith("/"))){
           indexPos += "/";
       }
       this.indexPos = indexPos;
    }


    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public boolean isBprint() {
        return bprint;
    }

    public void setBprint(boolean bprint) {
        this.bprint = bprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        IndexBean indexBean = (IndexBean) o;
        return Double.compare(indexBean.minIndexReopenGap, minIndexReopenGap) == 0 &&
                Double.compare(indexBean.maxIndexReopenGap, maxIndexReopenGap) == 0 &&
                bprint == indexBean.bprint &&
                Objects.equals(indexPos, indexBean.indexPos) &&
                Objects.equals(indexName, indexBean.indexName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(minIndexReopenGap, maxIndexReopenGap, indexPos, indexName, bprint);
    }

    @Override
    public String toString() {
        return "IndexBean{" +
                "analyzer=" + analyzer +
                ", minIndexReopenGap=" + minIndexReopenGap +
                ", maxIndexReopenGap=" + maxIndexReopenGap +
                ", indexPos='" + indexPos + '\'' +
                ", indexName='" + indexName + '\'' +
                ", bprint=" + bprint +
                '}';
    }
}
