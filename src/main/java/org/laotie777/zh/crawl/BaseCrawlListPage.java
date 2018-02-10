package org.laotie777.zh.crawl;

import utils.DoRegex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author yuh
 * @Date Created in 上午11:39 2018/2/10
 * @Description 爬取网页 数据封装到自己的中 自己再提供可插拔的处理方法
 */
public abstract class BaseCrawlListPage extends BaseCrawl{

    private String pageurl;

    /**
     * @param urlStr
     * @param charsetName
     * @throws IOException
     */
    public BaseCrawlListPage(String urlStr, String charsetName) throws IOException {
        executeByGet(urlStr, charsetName);
        pageurl = urlStr;
    }

    /**
     * @param urlStr
     * @param charsetName
     * @param method
     * @param params
     * @throws IOException
     */
    public BaseCrawlListPage(String urlStr, String charsetName, String method, HashMap<String, String> params) throws IOException{
        execute(urlStr, params,method,charsetName);
        pageurl = urlStr;
    }

    /**
     * @return List<String> 
     * @Author: lulei
     * @Description: 返回页面上需求的链接地址
     */
    public List<String> getPageUrls(){
        List<String> pageUrls = new ArrayList<String>();
        pageUrls = DoRegex.getArrayList(getPageSourceCode(), getUrlRegexString(), pageurl, getUrlRegexStringNum());
        return pageUrls;
    }

    /**
     * @return String
     * @Author: lulei
     * @Description: 返回页面上需求的网址连接的正则表达式
     */
    public abstract String getUrlRegexString();

    /**
     * @return int
     * @Author: lulei
     * @Description: 正则表达式中要去的字段位置
     */
    public abstract int getUrlRegexStringNum();


}
