package com.yejh.combination;/**
 * @author yejh
 * @create 2020-02_17 14:21
 */

import com.yejh.indexinit.IndexInitializer;
import com.yejh.utils.TxtUtil;
import org.junit.jupiter.api.Test;

import javax.persistence.Index;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Combination类作用于由XmlParseRunnable类产生的author文件
 * 由于author文件中相同作者的记录通常会有多条，所以需要把相同作者的记录合并在一起，从而使得检索更方便
 **/
public class Combination {
    private static String authorIndexFileLocation;
    private static String titleIndexFileLocation;

    private static String[] authorIndexFileNames;
    private static String[] titleIndexFileNames;

    private static String[] tags;

    //Map示例是TreeMap，保证了作者名称的有序性
    private Map<String, List<Long>> authorIndex;
    private Map<String, List<Long>> titleIndex;

    private IndexInitializer indexInitializer = new IndexInitializer();

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            authorIndexFileLocation = (String) properties.get("index_file_root") + "//author";
            titleIndexFileLocation = (String) properties.get("index_file_root") + "//title";
            authorIndexFileNames = new String[27];
            titleIndexFileNames = new String[27];
            tags = new String[27];
            for (int i = 0; i < authorIndexFileNames.length; ++i) {
                tags[i] = String.valueOf((char) (i + 'a'));
                authorIndexFileNames[i] = authorIndexFileLocation + "//" + String.valueOf((char) (i + 'a') + ".csv");
                titleIndexFileNames[i] = titleIndexFileLocation + "//" + String.valueOf((char) (i + 'a') + ".csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void combineAuthor() throws Exception {
        for (String tag : tags) {
            combineSingleAuthorFile(tag);
            System.out.println(tag + "处理完毕");
        }
    }

    public void combineTitle() throws Exception {
        for (String tag : tags) {
            combineSingleTitleFile(tag);
            System.out.println(tag + "处理完毕");
        }
    }

    //"Ali Ridha Mahjoub", 38209119, 38456844, 38600115, 38615171
    private void combineSingleAuthorFile(String tag) throws IOException {
        String fileName = authorIndexFileLocation + "//" + tag + ".csv";
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));
        String line = null;
        //初始化authorIndex
        authorIndex = new TreeMap<>();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            //System.out.println(line);
            String[] split = line.split(", ");
            String authorName = TxtUtil.pairTrim(split[0], '\"').trim();
            ArrayList<Long> longs = new ArrayList<>();
            for (int i = 1; i < split.length; ++i) {
                longs.add(Long.valueOf(split[i]));
            }
            if (authorIndex.containsKey(authorName)) {
                authorIndex.get(authorName).addAll(longs);
            } else {
                authorIndex.put(authorName, longs);
            }
        }

        //写入到文件中
        indexInitializer.writeAuthorIndex(authorIndex, tag, false);
    }

    private void combineSingleTitleFile(String tag) throws IOException {
        String fileName = titleIndexFileLocation + "//" + tag + ".csv";
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));
        String line = null;
        //初始化authorIndex
        int i = 0;
        titleIndex = new TreeMap<>();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            //System.out.println(line);
            int gap = TxtUtil.findLast(line, ',');
            String title = TxtUtil.pairTrim(line.substring(0, gap), '\"').trim();
            long location = Long.valueOf(line.substring(gap+2));
            if(titleIndex.containsKey(title)){
                titleIndex.get(title).add(location);
                ++i;
            }else{
                ArrayList<Long> locations = new ArrayList<>();
                locations.add(location);
                titleIndex.put(title, locations);
            }
        }
        System.out.println(titleIndex.size());

        //写入到文件中
        System.out.println("有" + i + "条同名标题");
        indexInitializer.writeBatchTitleIndex(titleIndex, tag, false);
    }


    @Test
    public void test() throws Exception {
        combineAuthor();

        //大约需要25s
        //411 MB (431,464,857 字节)
        combineTitle();
        //406 MB (426,685,697 字节)

        //931075 + 10623 = 941698
        //combineSingleTitleFile("a");
    }
}
