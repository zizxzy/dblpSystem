package comsubgraph;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class SubGraphInit {
    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();

        String XMLPath;
        String indexFileLocation;
        Properties properties = new Properties();
        try {
            //从配置文件获取文件以及csv存放的目录路径
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            XMLPath = (String) properties.get("xml_file_location");
            indexFileLocation = (String) properties.get("index_file_root");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(indexFileLocation).append("//comsubgraph//comsubgraph").append(".csv");
            File file = new File(String.valueOf(stringBuilder));
            //如果初始化过了，则不再进行初始化
            if (file.exists()) {
                System.out.println("文件已存在，请确认是否已经初始化");
                return;
            }

            //统计个阶完全子图的数量
            ComSubGraphCount comSubGraphCount = new ComSubGraphCount(XMLPath);
            HashMap<Integer, HashMap<ArrayList<Integer>, Integer>> ComSubGraph = comSubGraphCount.countComSubGraph();

            //测试，输出结果
            System.out.println("统计完全子图总用时: " + (System.currentTimeMillis() - beginTime));
            for (Integer integer : ComSubGraph.keySet()) {
                System.out.println(integer + "阶完全子图总数： " + ComSubGraph.get(integer).size());
            }
            //导出统计结果到csv文件中
            if (!exportCsv(file, ComSubGraph)) {
                System.out.println("导出统计结果失败，请检查原因");
                return;
            }
            //导出各阶子图的顶点信息到csv文件中
            for (Integer integer : ComSubGraph.keySet()) {
                StringBuilder str = new StringBuilder();
                str.append(indexFileLocation).append("//comsubgraph//").append(integer).append(".csv");
                if (!exportCsvDesc(new File(String.valueOf(str)), ComSubGraph.get(integer))) {
                    System.out.println("导出各阶子图的顶点信息失败，请检查原因");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("all")
    public static boolean exportCsv(File file, HashMap<Integer, HashMap<ArrayList<Integer>, Integer>> result) {
        boolean isSucess = false;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (result != null && !result.isEmpty()) {
                for (Object item : result.keySet()) {
                    bw.append(item + "," + result.get(item).size()).append("\r");
                }
            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSucess;
    }

    @SuppressWarnings("all")
    public static boolean exportCsvDesc(File file, HashMap<ArrayList<Integer>, Integer> result) {
        boolean isSucess = false;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (result != null && !result.isEmpty()) {
                for (ArrayList<Integer> integers : result.keySet()) {
                    for (Integer integer : integers) {
                        bw.append(integer + ",");
                    }
                    bw.append("\r");
                }

            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSucess;
    }
}
