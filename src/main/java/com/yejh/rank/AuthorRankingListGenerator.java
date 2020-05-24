package com.yejh.rank;/**
 * @author yejh
 * @create 2020-02_17 18:33
 */

import com.yejh.bean.Author;
import com.yejh.loader.RecordLoader;
import com.yejh.search.RecordSearcher;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

/**
 * AuthorRankingListGenerator类用于生成发表数前100的作者排行榜
 **/
public class AuthorRankingListGenerator implements RankingListGenerator {
    private static String rankFileLocation;

    static {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = RecordSearcher.class.getClassLoader()
                    .getResourceAsStream("config//global.properties");
            System.out.println(resourceAsStream);
            properties.load(resourceAsStream);
            //properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            rankFileLocation = (String) properties.get("index_file_root") + "//rank//author_top100.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成排行榜文件
     * 注意！！此方法仅用于生成排行榜文件，不会被controller调用
     *
     * @Param: 排行榜大小
     * @return: Map的key为作者Author，value是文章数目
     */
    @Override
    public List<Author> generateRankingList(int listSize) throws IOException {
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

    /**
     * 读取排行榜文件
     */
    @Override
    public List<Author> readRankingList() throws IOException {
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(rankFileLocation)));
        ArrayList<Author> authors = new ArrayList<>();
        while (scanner.hasNextLine()) {
            authors.add(Author.initAuthor(scanner.nextLine(), true));
        }
        return authors;
    }

    @Test
    public void test() throws Exception {
        List<Author> rank = generateRankingList(100);
        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream
                (new FileOutputStream(rankFileLocation)));
        for (Author author : rank) {
            printWriter.write("\"" + author.getName() + "\", " + author.getArticleNumber() + "\n");
        }
        printWriter.flush();

    }

    @Test
    public void test2() throws IOException {
        List<?> list = readRankingList();
        for (Object author : list) {
            System.out.println(author);
        }
    }
}
