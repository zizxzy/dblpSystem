package com.yejh.rank;/**
 * @author yejh
 * @create 2020-02_17 18:15
 */

import java.util.List;
import java.util.Map;

/**
 * 该类用于生成排行榜
 * 1、作者统计功能。输出写文章最多的前100名作者。
 **/
public interface RankingListGenerator {

    /**
     * 生成排行榜文件
     * 注意！！此方法仅用于生成排行榜文件，不会被controller调用
     *
     * @Param: 排行榜大小
     * @return: Map的key为作者Author，value是文章数目
     */
    List<?> generateRankingList(int listSize) throws Exception;

    /**
     * 读取排行榜文件
     */
    List<?> readRankingList() throws Exception;

}
