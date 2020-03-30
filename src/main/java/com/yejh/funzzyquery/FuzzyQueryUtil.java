package com.yejh.funzzyquery;

import com.yejh.AhoCorasickDoubleArrayTrie.AhoCorasickDoubleArrayTrie;
import com.yejh.bean.Article;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FuzzyQueryUtil {
    public static List<Article> articleList = new ArrayList<>();

    static class queryThread extends Thread {
        private int beginPosition;
        private int endPosition;
        private String[] str;

        /**
         * 多线程提高查询的效率
         * @param beginPosition 起始查询位置
         * @param endPosition 结束查询位置
         * @param str 查询关键字数组
         */
        public queryThread(int beginPosition, int endPosition, String[] str) {
            this.beginPosition = beginPosition;
            this.endPosition = endPosition;
            this.str = str;
        }

        @Override
        public void run() {
            for (int i = beginPosition; i < endPosition; i++) {
                if (com.yejh.titleInit.titleInitializer.stringListHashMap.get(i) != null && com.yejh.titleInit.titleInitializer.stringListHashMap.get(i).getTitle() != null) {
                    if (FuzzyQueryUtil.SearchTitleByKeyword(str, com.yejh.titleInit.titleInitializer.stringListHashMap.get(i).getTitle())) {
                        articleList.add(com.yejh.titleInit.titleInitializer.stringListHashMap.get(i));
                    }
                }
            }
        }
    }

    /**
     * 开启多线程的方法
     * @param str 查询关键字数组
     * @param tag 是否重新查询的标志
     * @return 查询的结果
     */
    public static boolean fuzzyQueryRes(String[] str, boolean tag) {
        if (tag) {
            return true;
        } else {
            articleList = new ArrayList<>();
            System.out.println("beginquery");

            try {
                queryThread queryThread1 = new queryThread(0, 400000, str);
                queryThread queryThread2 = new queryThread(400001, 800000, str);
                queryThread queryThread3 = new queryThread(800001, 1200000, str);
                queryThread queryThread4 = new queryThread(1200001, 1600000, str);
                queryThread queryThread5 = new queryThread(1600001, 2000000, str);
                queryThread queryThread6 = new queryThread(2000001, 2400000, str);
                queryThread queryThread7 = new queryThread(2400001, 2800000, str);
                queryThread queryThread8 = new queryThread(2800001, 3200000, str);
                queryThread queryThread9 = new queryThread(3200001, 3400000, str);
                queryThread queryThread10 = new queryThread(3400001, 4000000, str);
                queryThread queryThread11 = new queryThread(4000001, com.yejh.titleInit.titleInitializer.stringListHashMap.size(), str);
                queryThread1.start();
                queryThread2.start();
                queryThread3.start();
                queryThread4.start();
                queryThread5.start();
                queryThread6.start();
                queryThread7.start();
                queryThread8.start();
                queryThread9.start();
                queryThread10.start();
                queryThread11.start();
                queryThread1.join();
                queryThread2.join();
                queryThread3.join();
                queryThread4.join();
                queryThread5.join();
                queryThread6.join();
                queryThread7.join();
                queryThread8.join();
                queryThread9.join();
                queryThread10.join();
                queryThread11.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("end_query");
            return true;
        }
    }

    /**
     * 单个字符（关键字）的部分搜索匹配，使用正则表达式，时间消耗不高
     * @param words 要查询的关键词字符串
     * @return 返回符合匹配的键是title，值是偏移量的hashtable
     */
    public static boolean SearchTitleByKeyword(String words, String parseString) {
        Hashtable<String, Long> stringLongHashtable = new Hashtable<String, Long>();
        Pattern pattern = Pattern.compile(words);
        Matcher matcher = pattern.matcher(parseString);
        //find模糊查询
        return matcher.find();
    }

    /**
     * 多个字符（关键字）的部分搜索匹配，使用正则表达式，建议在本项目中使用
     * @param words 多个关键词组成的列表
     * @return 返回符合匹配的键是title，值是偏移量的hashtable
     */
    public static boolean SearchTitleByKeyword(String[] words, String parseString) {
        StringBuilder regexp = new StringBuilder();
        for (String word :
                words) {
            regexp.append("(?=.*").append(word).append(")");
        }
        Pattern pattern = Pattern.compile(regexp.toString());
        {
            Matcher matcher = pattern.matcher(parseString);
            //find模糊查询
            if (matcher.find()) {
               /* System.out.println(parseString);*/
                return true;
            }
        }
        //返回
        return false;
    }

    /**
     * 多个字符（关键字）的部分搜索匹配，使用AC自动机，使用双数组实现形式的AC自动机，实现的双数组AC自动机在src/main/java/com/yejh/AhoCorasickDoubleArrayTrie包中
     *在本项目中较慢，在内存已经被占用的情况下速度不佳，不建议使用
     * @param words       多个关键词组成String数组
     * @param parseString 传入要解析的值
     * @return 返回符合匹配的键是title，值是偏移量的hashtable
     */
    public static boolean SearchTitleByKeywordByDoubleArrayAc(String[] words, String parseString) {
        //创建一颗双数组实现形式的AC自动机
        AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
        TreeMap<String, String> map = new TreeMap<String, String>();
        for (String key : words) {
            map.put(key, key);
        }
        //传入map参数 构建树
        acdat.build(map);
        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(parseString);
        //去重
        for (int i = 0; i < hits.size(); i++) {
            for (int j = hits.size() - 1; j > i; j--) {
                if (hits.get(i).value.equals(hits.get(j).value)) {
                    hits.remove(j);
                }
            }
        }
        //放到新开的Hashtable中
        if (hits.size() == words.length) {
            System.out.println(hits);
            System.out.println(parseString);
            return true;
        }
        return false;
    }
}

