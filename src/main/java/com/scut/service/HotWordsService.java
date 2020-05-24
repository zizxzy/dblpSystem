package com.scut.service;/*
 * Created by lizeyu on 2020/3/29 0:30
 *
 */

import com.yejh.bean.Word;

import com.yejh.rank.AnnualHotWordRankingListGenerator;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.*;

/**
 * 每年热词service类
 */
public class HotWordsService {
    /**
     * 获取每年热词
     * @return 返回一个hashmap，键是年份，值是热词列表
     */
    public HashMap<String, ArrayList<Word>> getHotWords() {
        AnnualHotWordRankingListGenerator annualHotWordRankingListGenerator = new AnnualHotWordRankingListGenerator();
        try {
            return annualHotWordRankingListGenerator.readRankingList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
