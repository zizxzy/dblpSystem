package comsubgraph;

import java.io.*;
import java.util.LinkedHashMap;
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
            if(file.exists()) {
                System.out.println("文件已存在，请确认是否已经初始化");
                return;
            }
            //统计
            ComSubGraphCount comSubGraphCount = new ComSubGraphCount(XMLPath);
            LinkedHashMap<Integer,Integer> ComSubGraph = comSubGraphCount.countComSubGraph();
            //输出结果
            System.out.println("统计完全子图总用时: "+(System.currentTimeMillis()-beginTime));
            for (Integer integer : ComSubGraph.keySet()) {
                System.out.println(integer+"阶完全子图总数： "+ComSubGraph.get(integer));
            }
            if(exportCsv(file,ComSubGraph)){
                System.out.println("导出csv文件成功");
            }else{
                System.out.println("导出csv文件失败，请检查错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean exportCsv(File file, LinkedHashMap result){
        boolean isSucess=false;

        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(result!=null && !result.isEmpty()){
                for (Object item : result.keySet()) {
                    bw.append(item+","+result.get(item)).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSucess;
    }
}
