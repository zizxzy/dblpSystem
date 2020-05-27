package com.scut.service;
 /*
 * @author yejh
 * @create 2020-02_17 21:07
 */

import com.yejh.bean.Article;
import com.yejh.funzzyquery.ArticleList_totalRecords;
import com.yejh.funzzyquery.FuzzyQueryUtil;
import com.yejh.search.RecordSearcher;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ArticleService类
 **/

@Service
public class ArticleService {

    /**
     * 根据文章名获取文章记录Article
     */
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

    /**
     * 获取Articles分页数据
     */
    public List<Article> getPageArticles(String tag, int pageNumber, int pageSize) {
        try {
            return RecordSearcher.getArticlesByTag(tag, pageNumber, pageSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据一页的条数、页号、关键字数组和刷新标记返回10条记录
     * @param pageNumber 一页的条数
     * @param pageSize   页号
     * @param strs       关键字数组
     * @param tag        刷新标记
     * @return 长度为10的列表
     */
    public ArticleList_totalRecords getPageArticlesByFuzzyQuery(Integer pageNumber, Integer pageSize, String[] strs, Boolean tag) {
        System.out.println("success");
        if (FuzzyQueryUtil.fuzzyQueryRes(strs, tag)) {
            List<Article> articleList = new ArrayList<Article>();

          /*  List<Article> articleList1 = FuzzyQueryUtil.articleList;*/
            List<Article> articleList1 = FuzzyQueryUtil.resultList;
            if (articleList1.size() <= pageSize) {
                return new ArticleList_totalRecords(articleList1, articleList1.size());
            }
            if ((pageNumber - 1) * pageSize < articleList1.size() && articleList1.size() < (pageNumber * pageSize)) {
                for (int i = (pageNumber - 1) * pageSize; i < articleList1.size(); i++) {
                    articleList.add(articleList1.get(i));
                }
            } else {
                for (int i = (pageNumber - 1) * pageSize; i < pageNumber * pageSize; i++) {
                    articleList.add(articleList1.get(i));
                }
            }
            return new ArticleList_totalRecords(articleList, articleList1.size());
        }
        return null;
    }
}
