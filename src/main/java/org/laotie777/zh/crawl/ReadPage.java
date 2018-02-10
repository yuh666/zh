package org.laotie777.zh.crawl;

import utils.DoRegex;
import utils.ParseUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author yuh
 * @Date Created in 下午1:09 2018/2/10
 * @Description
 */
public class ReadPage extends BaseCrawl{
    private static final String CONTENT = "<div id=\"readerFs\" class=\"\">(.*?)</div>";
    private static final String TITLE = "chapterName=\"(.*?)\"";
    private static final String WORDCOUNT = "itemprop=\"wordCount\">(\\d*)</span>";
    private String pageUrl;
    private static HashMap<String, String> params;
    /**
     * 添加相关头信息，对请求进行伪装
     */
    static {
        params = new HashMap<String, String>();
        params.put("Referer", "http://book.zongheng.com");
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
    }

    public ReadPage(String url) throws IOException {
        executeByGet(url,null, "utf-8",params);
        this.pageUrl = url;
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 章节标题
     */
    private String getTitle() {
        return DoRegex.getFirstString(getPageSourceCode(), TITLE, 1);
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 字数
     */
    private int getWordCount() {
        String wordCount = DoRegex.getFirstString(getPageSourceCode(), WORDCOUNT, 1);
        return ParseUtil.parseStringToInt(wordCount, 0);
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 正文
     */
    private String getContent() {
        String ori =  DoRegex.getFirstString(getPageSourceCode(), CONTENT, 1);
        return ori.replaceAll("<script>(.*?)</script>","");
    }

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        ReadPage readPage = new ReadPage("http://book.zongheng.com/chapter/377897/6166353.html");
        System.out.println(readPage.pageUrl);
        System.out.println(readPage.getTitle());
        System.out.println(readPage.getWordCount());
        System.out.println(readPage.getContent());
    }

}
