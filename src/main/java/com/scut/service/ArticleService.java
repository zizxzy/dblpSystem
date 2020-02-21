package com.scut.service;/**
 * @author yejh
 * @create 2020-02_17 21:07
 */

import com.yejh.bean.Article;
import com.yejh.search.RecordSearcher;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @description: TODO
 **/

@Service
public class ArticleService {

    public Article getArticleByName(String articleName){
        Article article = null;
        try{
            article = RecordSearcher.binarySearchByTitle(articleName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return article;
    }

    public List<Article> getPageArticles(String tag, int pageNumber, int pageSize){
        try {
           return RecordSearcher.getArticlesByTag(tag, pageNumber, pageSize);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
