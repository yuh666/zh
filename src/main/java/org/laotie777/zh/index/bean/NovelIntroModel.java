package org.laotie777.zh.index.bean;

/**
 * @Author yuh
 * @Date Created in 下午2:20 2018/2/10
 * @Description
 */
public class NovelIntroModel {
    private String md5Id;
    private String name;
    private String author;
    private String description;
    private String type;
    private String lastChapter;
    private String chapterlisturl;
    private int wordCount;
    private String keyWords;
    private int chapterCount;

    public String getMd5Id() {
        return md5Id;
    }
    public void setMd5Id(String md5Id) {
        this.md5Id = md5Id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLastChapter() {
        return lastChapter;
    }
    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }
    public String getChapterlisturl() {
        return chapterlisturl;
    }
    public void setChapterlisturl(String chapterlisturl) {
        this.chapterlisturl = chapterlisturl;
    }
    public int getWordCount() {
        return wordCount;
    }
    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
    public String getKeyWords() {
        return keyWords;
    }
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
    public int getChapterCount() {
        return chapterCount;
    }
    public void setChapterCount(int chapterCount) {
        this.chapterCount = chapterCount;
    }
}
