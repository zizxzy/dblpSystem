package com.yejh.funzzyquery;

import com.scut.comsubgraph.ComSubGraphGetter;
import com.yejh.titleInit.titleInitializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.yejh.funzzyquery.FuzzyQueryUtil;

@Component("taskJob")
public class keepQuote {
    /**
     * 定时器维持关键列表的引用，在关键词查询中需要用到的title列表，根据关键词查询到的结果列表以及根据阶数查询到的完全子图
     */
    //@Scheduled(cron = "0 * 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * ?")
    public void keepqoute() {
        int size = titleInitializer.stringListHashMap.size();
        int size1  =  FuzzyQueryUtil.articleList.size();
        int size3 = ComSubGraphGetter.comSubGraph.getNum();
        System.out.println(new String("运行中  "+"title列表的长度是："+size+"根据关键词查询到的列表长度是："
                +size1+"根据阶数查询到的完全子图的信息是："+size3));
    }
}
