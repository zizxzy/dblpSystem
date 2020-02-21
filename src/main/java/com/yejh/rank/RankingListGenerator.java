package com.yejh.rank;/**
 * @author yejh
 * @create 2020-02_17 18:15
 */

import java.util.List;
import java.util.Map;

/**
 * @description: 该类用于生成排行榜，主要完成下列任务
 * 1、作者统计功能。输出写文章最多的前100名作者。
 * 2、热点分析功能。分析每一年发表的文章中，题目所包含的单词中，出现频率排名前10的关键词。
 *
 **/
public interface RankingListGenerator {
    
    /** 
    * @Description: 生成排行榜 
    * @Param: 排行榜大小 
    * @return: Map的key为作者Author或者是单词，value是文章数目或者是单词出现频率
    * @Author: yejh
    * @Date: 2020/2/17 
    */ 
    List<?> generateRankingList(int listSize) throws Exception;

    List<?> readRankingList() throws Exception;

}
