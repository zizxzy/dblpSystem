package com.yejh.rank;/**
 * @author yejh
 * @create 2020-02_17 18:15
 */

import java.util.List;
import java.util.Map;

/**
 * @description: 该类用于生成排行榜
 * 1、作者统计功能。输出写文章最多的前100名作者。
 **/
public interface RankingListGenerator {
    
    /** 
    * @Description: 生成排行榜 
    * @Param: 排行榜大小 
    * @return: Map的key为作者Author，value是文章数目
    * @Author: yejh
    * @Date: 2020/2/17 
    */ 
    List<?> generateRankingList(int listSize) throws Exception;

    List<?> readRankingList() throws Exception;

}
