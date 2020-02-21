package com.yejh.indexinit;/**
 * @author yejh
 * @create 2020-02_17 10:16
 */

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

/**
 * @description: TODO
 **/
public class IndexInitializer {

    private static String indexFileLocation;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            indexFileLocation = (String) properties.get("index_file_root");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description: 把authorIndex以csv格式存储在磁盘中（没有表头），第一列为作者名，其余列为文件记录
     * 例如：Tom, 121L, 597L, ..., 8949165498L
     * @Param: authorIndex: 记录有作者和他的文章所在文件位置的Map
     * @Author: yejh
     * @Date: 2020/2/17
     */
    public void writeAuthorIndex(Map<String, List<Long>> authorIndex, String tag, boolean append) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(indexFileLocation).append("//author//").append(tag).append(".csv");

        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(
                new FileOutputStream(stringBuilder.toString(), append)));

        int number = 0;
        stringBuilder = new StringBuilder();

        for (Map.Entry<String, List<Long>> entry : authorIndex.entrySet()) {
            if (number % 50 == 0) {
                printWriter.write(stringBuilder.toString());
                printWriter.flush();
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append("\"").append(entry.getKey()).append("\"");
            for (Long l : entry.getValue()) {
                stringBuilder.append(", ").append(l);
            }
            stringBuilder.append("\n");

            ++number;
        }

        printWriter.write(stringBuilder.toString());
        printWriter.flush();
    }

    public void writeAuthorIndex(Map<String, List<Long>> authorIndex, String tag) throws Exception {
        writeAuthorIndex(authorIndex, tag, true);
    }

    public void writeTitleIndex(Map<String, Long> titleIndex, String tag) throws Exception {
        writeTitleIndex(titleIndex, tag, true);
    }

    public void writeTitleIndex(Map<String, Long> titleIndex, String tag, boolean append) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(indexFileLocation).append("//title//").append(tag).append(".csv");

        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(
                new FileOutputStream(stringBuilder.toString(), append)));

        int number = 0;
        stringBuilder = new StringBuilder();

        for (Map.Entry<String, Long> entry : titleIndex.entrySet()) {
            if (number % 50 == 0) {
                printWriter.write(stringBuilder.toString());
                printWriter.flush();
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append("\"").append(entry.getKey()).append("\"").append(", ")
                    .append(entry.getValue()).append("\n");

            ++number;
        }

        printWriter.write(stringBuilder.toString());
        printWriter.flush();
    }

    public void writeBatchTitleIndex(Map<String, List<Long>> titleIndex, String tag, boolean append) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(indexFileLocation).append("//title//").append(tag).append(".csv");

        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(
                new FileOutputStream(stringBuilder.toString(), append)));

        int number = 0;
        stringBuilder = new StringBuilder();

        for (Map.Entry<String, List<Long>> entry : titleIndex.entrySet()) {
            if (number % 50 == 0) {
                printWriter.write(stringBuilder.toString());
                printWriter.flush();
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append("\"").append(entry.getKey()).append("\"");
            for (Long l : entry.getValue()) {
                stringBuilder.append(", ").append(l);
            }
            stringBuilder.append("\n");

            ++number;
        }

        printWriter.write(stringBuilder.toString());
        printWriter.flush();

    }

    @Test
    public void test() throws Exception {
        //测试作者
        Map<String, List<Long>> authorIndex = new HashMap<>();
        List<Long> longs = new ArrayList<>(5);
        longs.add(2655L);
        longs.add(9824982498491922L);
        longs.add(42189412L);

        for (int i = 0; i < 200; ++i) {
            authorIndex.put("abc" + i, longs);
        }
        writeAuthorIndex(authorIndex, "a");

        //测试标题
        Map<String, Long> titleIndex = new HashMap<>();
        for (int i = 0; i < 200; ++i) {
            titleIndex.put("This is a title, so can i make it?" + i, 459618924L);
        }
        writeTitleIndex(titleIndex, "t");
    }
}