package com.yejh.rank;/**
 * @author yejh
 * @create 2020-02_17 18:33
 */

import com.yejh.bean.Author;
import com.yejh.loader.RecordLoader;
import com.yejh.search.RecordSearcher;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @description: TODO
 **/
public class AuthorRankingListGenerator implements RankingListGenerator {
    @Override
    public List<Author> generateRankingList(int listSize) throws Exception {
        List<Author> top = new ArrayList<>(listSize);
        List<Author>[] buckets = new ArrayList[2000];
        Set<Author> authors = RecordLoader.loadAuthor();
        for (Author author : authors) {
            int articleNumber = author.getArticleNumber();
            if (buckets[articleNumber] == null) {
                buckets[articleNumber] = new ArrayList<>();
            }
            buckets[articleNumber].add(author);
        }

        // 倒序遍历数组获取出现顺序从大到小的排列
        for (int i = buckets.length - 1; i >= 0 && top.size() < listSize; i--) {
            if (buckets[i] == null) continue;
            top.addAll(buckets[i]);
        }
        return top;
    }

    @Test
    public void test() throws Exception{
        List<Author> rank = generateRankingList(100);
        for(int i = 0; i < rank.size(); ++i){
            Author author = rank.get(i);
            System.out.println(author.getName() + ", " + author.getArticleNumber());
        }
    }
}
