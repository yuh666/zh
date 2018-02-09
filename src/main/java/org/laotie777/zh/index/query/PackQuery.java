package org.laotie777.zh.index.query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.laotie777.zh.index.manager.IndexManager;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yuh
 * @Date Created in 下午7:37 2018/2/9
 * @Description
 */
public class PackQuery {

    /**
     * 查询语句分析器
     */
    private Analyzer analyzer;

    public PackQuery(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public PackQuery(String indexName) {
        analyzer = IndexManager.getIndexManagerByName(indexName).getAnalyzer();
    }

    /**
     * 多个域查询 检索的内容一致
     *
     * @param key
     * @param fields
     * @return
     * @throws ParseException
     */
    public Query getMultiFieldQuery(String key, String[] fields) throws ParseException {
        return new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzer).parse(key);
    }

    /**
     * 单个域查询
     *
     * @param key
     * @param field
     * @return
     * @throws ParseException
     */
    public Query getOneFieldQuery(String key, String field) throws ParseException {
        return new QueryParser(Version.LUCENE_43, field, analyzer).parse(key);
    }

    /**
     * 返回Boolean查询集合
     *
     * @param key
     * @param fields
     * @param occurs
     * @return
     */
    public Query getBooleanQuery(String key, String[] fields, BooleanClause.Occur[] occurs) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(key));
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        List<String> words = new ArrayList<>();
        while (tokenStream.incrementToken()) {
            words.add(charTermAttribute.toString());
        }
        BooleanQuery query = new BooleanQuery();
        for (int i = 0; i < fields.length; i++) {
            BooleanQuery subQuery = new BooleanQuery();
            for (String word : words) {
                subQuery.add(new TermQuery(new Term(fields[i], word)), BooleanClause.Occur.SHOULD);
            }
            query.add(subQuery, occurs[i]);
        }
        return query;
    }

    /**
     * 组合多喝查询
     *
     * @param queries
     * @param occurs
     * @return
     */
    public Query combineQuery(List<Query> queries, List<BooleanClause.Occur> occurs) {
        BooleanQuery booleanQuery = new BooleanQuery();
        for (int i = 0; i < queries.size(); i++) {
            booleanQuery.add(queries.get(i), occurs.get(i));
        }
        return booleanQuery;
    }

    /**
     * 查询未分词的域 StringField
     *
     * @param key
     * @param field
     * @return
     */
    public Query getStringField(String key, String field) {
        TermQuery termQuery = new TermQuery(new Term(field, key));
        return termQuery;
    }

    /**
     * 单个字段前缀查询
     * @param key
     * @param field
     * @return
     */
    public Query getStartQuery(String key,String field){
        return new PrefixQuery(new Term(field,key));
    }

    /**
     * 组合多个域的前缀查询
     * @param key
     * @param fields
     * @param occur
     * @return
     */
    public Query getStartQuery(String key, String[] fields, BooleanClause.Occur occur){
        ArrayList<Query> querys = new ArrayList<Query>();
        ArrayList<BooleanClause.Occur> occurs = new ArrayList<BooleanClause.Occur>();
        for (String field : fields) {
            querys.add(getStartQuery(key, field));
            occurs.add(occur);
        }
        return combineQuery(querys, occurs);
    }

    /**
     * 组合多个域的前缀查询 关系为或者
     * @param key
     * @param fields
     * @param occur
     * @return
     */
    public Query getStartQuery(String key, String[] fields){
       return getStartQuery(key,fields, BooleanClause.Occur.SHOULD);
    }

    /**
     * 短语查询
     * @param field
     * @param key
     * @param slop
     * @return
     */
    public Query getPhraseQuery(String field,String key,int slop) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(key));
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        PhraseQuery phraseQuery = new PhraseQuery();
        phraseQuery.setSlop(slop);
        while (tokenStream.incrementToken()) {
            phraseQuery.add(new Term(field,charTermAttribute.toString()));
        }
        return phraseQuery;
    }

    /**
     * 短语组合查询
     * @param field
     * @param key
     * @param slop
     * @return
     */
    public Query getPhraseQuery(String[] field, String key, int slop, BooleanClause.Occur occur) throws IOException {
        ArrayList<Query> querys = new ArrayList<Query>();
        ArrayList<BooleanClause.Occur> occurs = new ArrayList<BooleanClause.Occur>();
        for (String s : field) {
            querys.add(getPhraseQuery(s,key,slop));
            occurs.add(occur);
        }
        return combineQuery(querys,occurs);
    }

    /**
     * 短语组合查询
     * @param field
     * @param key
     * @param slop
     * @return
     */
    public Query getPhraseQuery(String[] field, String key, int slop) throws IOException {
        return getPhraseQuery(field,key,slop, BooleanClause.Occur.SHOULD);
    }



}
