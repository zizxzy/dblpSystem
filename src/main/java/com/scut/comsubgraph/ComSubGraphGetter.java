package com.scut.comsubgraph;
/* @author lss
 * @create 2020-02_26 12:00
 */

import com.yejh.bean.ComSubGraph;
import com.yejh.funzzyquery.ArticleList_totalRecords;
import com.yejh.utils.TxtUtil;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class ComSubGraphGetter {
    private static String indexFileLocation;
    public static ComSubGraph comSubGraph = new ComSubGraph();
    private static HashMap<Integer, Integer> comSubGraphCounts = null;
    private static HashMap<String, String> nameMap = null;

    static {
        try {
            //从配置文件获取csv存放的目录路径
            Properties properties = TxtUtil.getProperties();
            indexFileLocation = (String) properties.get("index_file_root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取 各阶子图计数 文件
     */
    public static HashMap<Integer, Integer> getComSubGraphCounts() {
        if (comSubGraphCounts != null) {
            return comSubGraphCounts;
        }
        HashMap<Integer, Integer> comSubGraphCounts = new HashMap<Integer, Integer>();
        BufferedReader reader = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(indexFileLocation).append("//comsubgraph//comsubgraph").append(".csv");
            //读取文件内容
            reader = new BufferedReader(new FileReader(String.valueOf(stringBuilder)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] item = line.split(",");
                comSubGraphCounts.put(Integer.parseInt(item[0]), Integer.parseInt(item[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return comSubGraphCounts;
    }

    private static HashMap<String, String> getNameMap() throws IOException {
        if (nameMap != null) {
            return nameMap;
        }
        HashMap<String, String> nameMap = new HashMap<>();
        StringBuilder nameMapPath = new StringBuilder();
        nameMapPath.append(indexFileLocation).append("//comsubgraph//nameMap").append(".csv");
        BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(nameMapPath)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            ArrayList<String> temp = new ArrayList<String>();
            String[] item = line.split(",");
            nameMap.put(item[0], item[1]);
        }
        return nameMap;
    }


    public static ArrayList<ArrayList<String>> getComSubGraphAuthors(int rank) {
        if (!getComSubGraphCounts().containsKey(rank)) {
            System.out.println("不存在" + rank + "阶的完全子图，请重新获取");
            return null;
        }
        ArrayList<ArrayList<String>> comSubGraphAuthors = new ArrayList<ArrayList<String>>();
        HashMap<String, String> nameMap = new HashMap<>();
        BufferedReader reader = null;
        try {
            //获取nameMap
            nameMap = getNameMap();

            //读取文件内容
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(indexFileLocation).append("//comsubgraph//").append(rank).append(".csv");
            reader = new BufferedReader(new FileReader(String.valueOf(stringBuilder)));
            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> temp = new ArrayList<String>();
                String[] item = line.split(",");
                for (int i = 0; i < rank; i++) {
                    temp.add(nameMap.get(item[i]));
                }
                comSubGraphAuthors.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return comSubGraphAuthors;
    }


    @Test
    public void test() {
        long beginTime = System.currentTimeMillis();
        //获取统计结果
        HashMap<Integer, Integer> result = getComSubGraphCounts();
        for (Map.Entry<Integer, Integer> item : result.entrySet()) {
            System.out.println(item.getKey() /*+ "阶子图的数量是" + item.getValue()*/);
        }
        //获取各阶完全子图的相信顶点信息，即作者名，参数rank填需要获取的阶数，获取需要2-5秒的时间
        ArrayList<ArrayList<String>> comSubGraphAuthors = getComSubGraphAuthors(55);
        if (comSubGraphAuthors != null && !comSubGraphAuthors.isEmpty()) {
            for (ArrayList<String> comSubGraphAuthor : comSubGraphAuthors) {
                System.out.println(comSubGraphAuthor);
            }
        }
        System.out.println("所用时间：  " + (System.currentTimeMillis() - beginTime));
    }

    private static ComSubGraph getComSubGraphsByPage(Integer pageNumber, Integer pageSize) {
        ArrayList<ArrayList<String>> articleList = new ArrayList<>();
        ComSubGraph comSubGraph2 = ComSubGraphGetter.comSubGraph;
        int size = comSubGraph2.getNum();
        if (comSubGraph2.getNum() <= pageSize) {
            return comSubGraph2;
        }
        System.out.println(comSubGraph2.getNum());
        if ((pageNumber - 1) * pageSize < size && size < (pageNumber * pageSize)) {
            for (int i = (pageNumber - 1) * pageSize; i < size; i++) {
                articleList.add(comSubGraph2.getComSubGraphAuthors().get(i));
            }
            return new ComSubGraph(size, articleList);
        } else {
            for (int i = (pageNumber - 1) * pageSize; i < pageNumber * pageSize; i++) {
                articleList.add(comSubGraph2.getComSubGraphAuthors().get(i));
            }
            return new ComSubGraph(size, articleList);
        }
    }

    /**
     * 根据阶数k查询所有k阶子图对应的作者集合
     * @param pageNumber 页号
     * @param pageSize 页大小
     * @param tag
     * @param k 阶数
     * @return ComSubGraph类用以封装结果
     */
    public static ComSubGraph getSubComGraphs(Integer pageNumber, Integer pageSize, Boolean tag, Integer k) {
        if (tag) {
            return ComSubGraphGetter.getComSubGraphsByPage(pageNumber, pageSize);
        } else {
            //获取统计结果
            HashMap<Integer, Integer> result = getComSubGraphCounts();
            int num = result.get(k);

            //获取各阶完全子图的相连顶点信息，即作者名，参数rank填需要获取的阶数，获取需要2-5秒的时间
            ArrayList<ArrayList<String>> comSubGraphAuthors = getComSubGraphAuthors(k);
            if (comSubGraphAuthors != null && !comSubGraphAuthors.isEmpty() && num != 0) {
                ComSubGraphGetter.comSubGraph.setNum(num);
                ComSubGraphGetter.comSubGraph.setComSubGraphAuthors(comSubGraphAuthors);
                return ComSubGraphGetter.getComSubGraphsByPage(pageNumber, pageSize);
            }
            return new ComSubGraph(0, new ArrayList<ArrayList<String>>());
        }
    }

}
