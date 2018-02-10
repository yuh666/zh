package org.laotie777.zh.crawl;

import utils.DoRegex;
import utils.ParseUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author yuh
 * @Date Created in 下午1:40 2018/2/10
 * @Description
 */
public class IntroPage extends BaseCrawl{
    private static final String NAME = "<meta name=\"og:novel:book_name\" content=\"(.*?)\"/> ";
    private static final String AUTHOR = "<meta name=\"og:novel:author\" content=\"(.*?)\"/> ";
    private static final String DESC = "<meta property=\"og:description\" content=\"(.*?)\"/> ";
    private static final String TYPE = "<meta name=\"og:novel:category\" content=\"(.*?)\"/> ";
    private static final String LATESTCHAPTER = "<meta name=\"og:novel:latest_chapter_name\" content=\"(.*?)\"/> ";
    private static final String CHAPTERLISTURL = "<meta name=\"og:novel:read_url\" content=\"(.*?)\"/> ";
    private static final String WORDCOUNT = "<span itemprop=\"wordCount\">(\\d*?)</span>";
    private static final String KEYWORDS = "<div class=\"keyword\">(.*?)</div>";
    private static final String KEYWORD = "<a.*?>(.*?)</a>";
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

    public IntroPage(String url) throws IOException {
        executeByPost(url ,null,"utf-8", params);
        this.pageUrl = url;
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 获取书名
     */
    private String getName() {
        return DoRegex.getFirstString(getPageSourceCode(), NAME, 1);
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 获取作者名
     */
    private String getAuthor() {
        return DoRegex.getFirstString(getPageSourceCode(), AUTHOR, 1);
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 书籍简介
     */
    private String getDesc() {
        return DoRegex.getFirstString(getPageSourceCode(), DESC, 1);
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 书籍分类
     */
    private String getType() {
        return DoRegex.getFirstString(getPageSourceCode(), TYPE, 1);
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 最新章节
     */
    private String getLatestChapter() {
        return DoRegex.getFirstString(getPageSourceCode(), LATESTCHAPTER, 1);
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 章节列表页Url
     */
    private String getChapterListUrl() {
        return DoRegex.getFirstString(getPageSourceCode(), CHAPTERLISTURL, 1);
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
     * @Description: 标签
     */
    private String keyWords() {
        String keyHtml = DoRegex.getFirstString(getPageSourceCode(), KEYWORDS, 1);
        return DoRegex.getString(keyHtml, KEYWORD, " ", 1);
    }

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        IntroPage intro = new IntroPage("http://book.zongheng.com/book/377897.html");
        System.out.println(intro.pageUrl);
        System.out.println(intro.getName());
        System.out.println(intro.getAuthor());
        System.out.println(intro.getDesc());
        System.out.println(intro.getType());
        System.out.println(intro.getLatestChapter());
        System.out.println(intro.getChapterListUrl());
        System.out.println(intro.getWordCount());
        System.out.println(intro.keyWords());
    }
}
