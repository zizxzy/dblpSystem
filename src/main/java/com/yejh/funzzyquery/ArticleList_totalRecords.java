package com.yejh.funzzyquery;

import com.yejh.bean.Article;

import java.util.List;

public class ArticleList_totalRecords {
    private List<Article> articleList;
    private int totalRecords;

    public ArticleList_totalRecords(List<Article> articleList, int totalRecords) {
        this.articleList = articleList;
        this.totalRecords = totalRecords;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public String toString() {
        return "ArticleList_totalRecords{" +
                "articleList=" + articleList +
                ", totalRecords=" + totalRecords +
                '}';
    }
}
