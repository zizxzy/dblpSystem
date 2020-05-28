package com.scut.service;/**
 * @author yejh
 * @create 2020-02_19 14:40
 */

import com.yejh.bean.Author;
import com.yejh.rank.impl.AuthorRankingListGenerator;
import com.yejh.search.RecordSearcher;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * AuthorService类
 **/

@Service
public class AuthorService {


    /**
     * 根据作者名获取作者记录Author
     * simplified=true: 通过作者名来查询作者基本信息，仅包括作者名和他的文章索引值
     * simplified=false: 通过作者名来查询作者详细信息，包括作者名、他的文章索引值、文章记录和合作者
     */
    public Author getAuthorByName(String authorName, boolean simplified){
        Author author = null;
        try{
            author = RecordSearcher.binarySearchByAuthor(authorName, !simplified);
            System.out.println("AuthorService.getAuthorByName(" + authorName + "): " + author);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    /**
     * 获取Authors分页数据
     */
    public List<Author> getPageAuthors(String tag, int pageNumber, int pageSize){
        try {
            return RecordSearcher.getAuthorsByTag(tag, pageNumber, pageSize);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取发表数前100的作者
     */
    public List<Author> getTopAuthors() {
        List<Author> authors = null;
        try {
            authors = new AuthorRankingListGenerator().readRankingList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authors;
    }
}
