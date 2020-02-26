package comsubgraph;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ComSubGraphGetter {
    private static String indexFileLocation;

    static {
        Properties properties = new Properties();
        try {
            //从配置文件获取csv存放的目录路径
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            indexFileLocation = (String) properties.get("index_file_root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<Integer, Integer> getComSubGraphCounts() {
        LinkedHashMap<Integer, Integer> comSubGraphCounts = new LinkedHashMap<Integer, Integer>();
        BufferedReader reader = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(indexFileLocation).append("//comsubgraph//comsubgraph").append(".csv");
            //读取文件内容
            reader = new BufferedReader(new FileReader(String.valueOf(stringBuilder)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
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

    public static ArrayList<ArrayList<String>> getComSubGraphAuthors(int rank) {
        if (!getComSubGraphCounts().containsKey(rank)) {
            System.out.println("不存在" + rank + "阶的完全子图，请重新获取");
            return null;
        }
        ArrayList<ArrayList<String>> ComSubGraphAuthors = new ArrayList<ArrayList<String>>();
        HashMap<String, String> nameMap = new HashMap<>();
        BufferedReader reader = null;
        try {
            //获取nameMap
            StringBuilder nameMapPath = new StringBuilder();
            nameMapPath.append(indexFileLocation).append("//comsubgraph//nameMap").append(".csv");
            reader = new BufferedReader(new FileReader(String.valueOf(nameMapPath)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> temp = new ArrayList<String>();
                String item[] = line.split(",");
                nameMap.put(item[0], item[1]);
            }

            //读取文件内容
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(indexFileLocation).append("//comsubgraph//").append(rank).append(".csv");
            reader = new BufferedReader(new FileReader(String.valueOf(stringBuilder)));

            while ((line = reader.readLine()) != null) {
                ArrayList<String> temp = new ArrayList<String>();
                String item[] = line.split(",");
                for (int i = 0; i < rank; i++) {
                    temp.add(nameMap.get(item[i]));
                }
                ComSubGraphAuthors.add(temp);
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
        return ComSubGraphAuthors;
    }


    @Test
    private void test() {
        long beginTime = System.currentTimeMillis();
        //获取统计结果
        LinkedHashMap<Integer, Integer> result = getComSubGraphCounts();
        for (Map.Entry<Integer, Integer> item : result.entrySet()) {
            System.out.println(item.getKey() + "阶子图的数量是" + item.getValue());
        }
        //获取各阶完全子图的相信顶点信息，即作者名，参数rank填需要获取的阶数，获取需要2-5秒的时间
        ArrayList<ArrayList<String>> comSubGraphAuthors = getComSubGraphAuthors(7);
        if (comSubGraphAuthors != null && !comSubGraphAuthors.isEmpty()) {
            for (ArrayList<String> comSubGraphAuthor : comSubGraphAuthors) {
                System.out.println(comSubGraphAuthor);
            }
        }
        System.out.println("所用时间：  " + (System.currentTimeMillis() - beginTime));
    }
}
