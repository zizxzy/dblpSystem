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
import java.util.stream.Collectors;

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
            Map<String, List<Long>> authorIndex = new TreeMap<>();
            int i = combineSingleFile(authorIndex, authorIndexFileLocation + "//" + tag + ".csv");
            System.out.println(tag + "处理完毕，有" + i + "条同名作者记录");
            //写入到文件中
            indexInitializer.writeAuthorIndex(authorIndex, tag, false);
        }
    }

    public void combineTitle() throws Exception {
        for (String tag : tags) {
            Map<String, List<Long>> titleIndex = new TreeMap<>();
            int i = combineSingleFile(titleIndex, titleIndexFileLocation + "//" + tag + ".csv");
            System.out.println(tag + "处理完毕，有" + i + "条同名标题记录");
            //写入到文件中
            indexInitializer.writeTitleIndex(titleIndex, tag, false);
        }
    }


    private int combineSingleFile(Map<String, List<Long>> indexMap, String fileName) throws IOException {
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));
        String line = null;
        int i = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            //System.out.println(line);
            int gap = TxtUtil.findLast(line, '\"') + 1;
            String name = TxtUtil.pairTrim(line.substring(0, gap), '\"').trim();
            String locations = line.substring(gap + 2);
            String[] split = locations.split(", ");
            List<Long> collect = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());

            if (indexMap.containsKey(name)) {
                indexMap.get(name).addAll(collect);
                ++i;
            } else {
                indexMap.put(name, collect);
            }
        }
        return i;
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
