package com.yejh.hotwordsInit;
 /* @author csh
  * @create 2020-02_29 12:00
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotWords {
    /** 年度词汇分析
     * 热词分析和热词文件 annual_hot_word_top10.txt生成的代码
     * @param yearSentence  记录论文year和文章内容的Hashtable
     * @param Preposition 过滤掉的介词列表，
     *         Preposition.add("for");
     *         Preposition.add("of");
     *         Preposition.add("on");
     *         Preposition.add("the");
     *         Preposition.add("a");
     *         Preposition.add("to");
     *         Preposition.add("with");
     *         Preposition.add("in");
     *         Preposition.add("and");
     *         Preposition.add("an");
     *         Preposition.add("using");
     *         Preposition.add("by");
     */
    public static void YearWord(ConcurrentHashMap<Integer, ArrayList<String>> yearSentence,CopyOnWriteArraySet<String> Preposition) {
        System.out.println(yearSentence.size());
        long firstTime = System.currentTimeMillis();
        Hashtable<Integer, Hashtable<String, Integer>> words = new Hashtable<>();
        Enumeration years = yearSentence.keys();
        while (years.hasMoreElements()) {
            Hashtable<String, Integer> wordsCount = new Hashtable<String, Integer>();
            int thisYear = (Integer) years.nextElement();
            ArrayList<String> titles = yearSentence.get(thisYear);
            //将所有单词都计次并放入wordsCount中
            for (int i = 0; i < titles.size(); i++) {
                //获得title
                String title = titles.get(i);
                //打散title
                if (titles.get(i) == null) {
                    continue;
                }

                StringTokenizer st = new StringTokenizer(title, " -(),?.!:\"'\n#", false);
                while (st.hasMoreElements()) {
                    String word = st.nextToken().toLowerCase();
                    if (Preposition.contains(word)) {
                        continue;
                    }
                    if (wordsCount.containsKey(word)) {
                        //记录加1
                        wordsCount.put(word, wordsCount.get(word) + 1);
                    } else {
                        wordsCount.put(word, 1);
                    }
                }
            }

            //此年的年度词汇集合
            Hashtable<String, Integer> tempTable = new Hashtable<>();
            //选择出出现次数最多的前十个字符

            for (int i = 0; i < 10; i++) {
                int tempCount = 0;
                String tempWord = "";
                Enumeration word = wordsCount.keys();
                while (word.hasMoreElements()) {
                    String thisWord = (String) word.nextElement();
                    int count = wordsCount.get(thisWord);
                    if (count > tempCount) {
                        tempCount = count;
                        tempWord = thisWord;
                    }
                }
                tempTable.put(tempWord, tempCount);
                wordsCount.remove(tempWord);
            }
            words.put(thisYear, tempTable);
        }
        //测试代码
        try {
            String filename = new String("C:/java-project/YearWords.txt");
            FileWriter fileWriter = new FileWriter(filename);
            Enumeration thisYear = words.keys();
            FileWriter writer = new FileWriter(filename);
            while (thisYear.hasMoreElements()) {
                String thisYw = new String();
                int year = (Integer) thisYear.nextElement();
                thisYw += "Year:"+Integer.toString(year)+'\n';
                Enumeration temp = words.get(year).keys();
                while (temp.hasMoreElements()) {
                    String word = (String) temp.nextElement();
                    thisYw += word +" :"+ words.get(year).get(word)+'\n';
                }
                writer.write(thisYw);
            }
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("年度热词生成时间：" + (System.currentTimeMillis() - firstTime));
    }

}



