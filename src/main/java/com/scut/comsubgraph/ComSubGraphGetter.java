package comsubgraph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

public class ComSubGraphGetter {

    private static LinkedHashMap<Integer,Integer> comSubGraphCounts;

    static {
        comSubGraphCounts = new LinkedHashMap<Integer, Integer>();
        Properties properties = new Properties();
        String indexFileLocation;
        BufferedReader reader = null;
        try {
            //从配置文件获取csv存放的目录路径
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            indexFileLocation = (String) properties.get("index_file_root");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(indexFileLocation).append("//comsubgraph//comsubgraph").append(".csv");

            //读取文件内容
            reader = new BufferedReader(new FileReader(String.valueOf(stringBuilder)));//换成你的文件名
            String line = null;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");
                comSubGraphCounts.put(Integer.parseInt(item[0]),Integer.parseInt(item[1]));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static LinkedHashMap<Integer,Integer> getComSubGraphCounts(){
        return comSubGraphCounts;
    }
}
