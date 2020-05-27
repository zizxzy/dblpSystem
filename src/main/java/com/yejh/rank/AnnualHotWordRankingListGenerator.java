package com.yejh.rank;
/*
 * Created by lizeyu on 2020/3/29 15:40
 */

import com.yejh.bean.Word;
import com.yejh.search.RecordSearcher;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 热词生成类
 **/
public class AnnualHotWordRankingListGenerator {
    private static String rankFileLocation;

    static {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = RecordSearcher.class.getClassLoader()
                    .getResourceAsStream("config//global.properties");
            System.out.println(resourceAsStream);
            properties.load(resourceAsStream);
            rankFileLocation = (String) properties.get("index_file_root") + "//rank//annual_hot_word_top10.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回一个hahsmap，键是年份，值是热词列表
     *
     * @return
     * @throws Exception
     */

    public Map<String, List<Word>> readRankingList() throws Exception {
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(rankFileLocation)));
        Map<String, List<Word>> hotWords = new HashMap<>();

        for (int i = 0; i < 86; i++) {
            ArrayList<Word> wordsList = new ArrayList<>();
            String year = "";
            for (int j = 0; j <= 10; j++) {
                if (scanner.hasNextLine()) {
                    if (j == 0) {
                        year = Word.initHotWords(scanner.nextLine()).getCount();
                    } else {
                        wordsList.add(Word.initHotWords(scanner.nextLine()));
                    }

                }
            }
            hotWords.put(year, wordsList);
        }
        return hotWords;
    }
}
