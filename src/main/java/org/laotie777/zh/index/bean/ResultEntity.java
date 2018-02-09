package org.laotie777.zh.index.bean;

import javax.swing.text.Document;
import java.io.Serializable;
import java.util.List;

/**
 * @Author yuh
 * @Date Created in 下午4:37 2018/2/9
 * @Description
 */
public class ResultEntity implements Serializable{
    /**
     * 查询到的数目
     */
    private int count;

    /**
     * 返回的文档
     */
    private List<Document> documents ;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "count=" + count +
                ", documents=" + documents +
                '}';
    }
}
