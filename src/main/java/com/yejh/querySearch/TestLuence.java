/*

package com.yejh.querySearch;

import com.scut.service.ArticleService;
import com.yejh.bean.Article;
import com.yejh.funzzyquery.ArticleList_totalRecords;
import com.yejh.search.RecordSearcher;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TestLuence {


    public Article getArticleByName(String articleName) {
        Article article = null;
        try {
            article = RecordSearcher.binarySearchByTitle(articleName);
            System.out.println("ArticleService.getArticleByName(" + articleName + "): " + article);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return article;
    }


    public ArticleList_totalRecords testSearch(String[] queryArray, int pageIndex, int pageSize) throws Exception {
        int size = queryArray.length;
        ArrayList<ArrayList<Article>> totalList = new ArrayList<ArrayList<Article>>(size);
        for (int i = 0; i < queryArray.length; i++) {
            int maxHits = 1000000;
            //1.创建分词器，搜索的时候需要对关键词进行分词，分词器要和创建索引的时候使用的分词器一模一样
            StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
            //2.创建查询对象，参数 默认查询域，分词器。如果查询的关键字中带搜索的域名，则从指定的搜索域中查询，如果不带域名，则从默认的搜索域中查
            QueryParser queryParser = new QueryParser("title", standardAnalyzer);
            //3.设置搜索的关键词
            Query query = queryParser.parse(queryArray.toString());
            //4.创建目录对象，指定索引库的位置
            Directory directory = FSDirectory.open(Paths.get("F://indexDir"));
            //5.创建输入流对象
            IndexReader indexReader = DirectoryReader.open(directory);
            //6.创建搜索对象
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //7.搜索并返回结果，指定返回10条
            TopDocs topDocs = indexSearcher.search(query, maxHits);
            //8.解析结果集，遍历结果集-打印
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            //最多到400万，向上转换不好发生信息丢失
            long totalCount = topDocs.totalHits;
            for (int j = 0; j < topDocs.totalHits; j++) {
                Document document = indexSearcher.doc(scoreDocs[j].doc);
                totalList.get(i).add(getArticleByName(document.get("title")));
            }
        }

 */
/*     int page = (int) Math.ceil((double) totalCount / (double) pageSize);
        //起始查询位置
        int begin = pageSize * (pageIndex - 1);
        int end = Math.min(begin + pageSize, scoreDocs.length);
        List<Article> articleList = new ArrayList<>();
        for (int i = begin; i < end; i++) {
            Document document = indexSearcher.doc(scoreDocs[i].doc);
            articleList.add(getArticleByName(document.get("title")));
        }*//*

       */
/* return new ArticleList_totalRecords(articleList, (int) totalCount);*//*
*/
/*
    }


    @Test
    public void test() throws Exception {
        /*System.out.println(testSearch(new String[]{"development"}, 10, 100).toString());*//*

    }
}

*/
