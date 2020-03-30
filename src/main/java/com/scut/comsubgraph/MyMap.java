package com.scut.comsubgraph;
/* @author lss
 * @create 2020-02_26 12:00
 */
import java.io.*;
import java.util.*;

public class MyMap {
    private ArrayList<ArrayList<Integer>> myMap = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> temp = null;
    private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    private int count = 0;

    MyMap(String XMLPath) {
        long bgtime = System.currentTimeMillis();
        RandomAccessFile randomAccessFile = null;
        ArrayList<String> arrayList = new ArrayList<String>();
        long curPosition = 85;
        try {
            randomAccessFile = new RandomAccessFile(new File(XMLPath), "rw");
            randomAccessFile.seek(curPosition);
            byte[] b = new byte[39000];
            while (randomAccessFile.read(b) != -1) {     //读取到文件末尾即退出循环
                int i = 0;
                while (b[i++] != '<') ;       //移动当前数组指针，直到指向'<'
                String str = new String(b, i, 3);    //获取标签前三个字符
                String endStr = "/" + str;           //获取当前记录的结束标签，后面判断匹配
                while (i <= 38990) {
                    while (i <= 38990 && b[++i] != '<') ;
                    str = new String(b, ++i, 3);
                    if (str.equals("aut")) {    //找到记录的author，写进hashtable
                        arrayList.clear();
                        while (str.equals("aut")) {
                            while (b[i++] != '>') ;
                            int j = 0;
                            while (b[i + j++] != '<') ;
                            arrayList.add(new String(b, i, j - 1));
                            while (b[++i] != '<') ;
                            while (b[++i] != '<') ;
                            str = new String(b, ++i, 3);
                        }
                        addEdge(arrayList);
                        break;
                    }
                }
                if (endStr.equals("/boo")) {
                    while (i <= 38990) {
                        while (i <= 38990 && b[i++] != '<') ;    //获取该条记录结束位置
                        str = new String(b, i, 6);
                        if (str.equals("/book>")) {
                            break;
                        }
                    }
                } else {
                    while (i <= 38990) {
                        while (i <= 38990 && b[i++] != '<') ;    //获取该条记录结束位置
                        str = new String(b, i, 4);
                        if (str.equals(endStr)) {
                            break;
                        }
                    }
                }
                curPosition = curPosition + i;   //从该条记录结束位置进行下一条记录解析
                randomAccessFile.seek(curPosition);
            }

            //将nameMap写入指定位置的csv文件
            Properties properties = new Properties();
            String indexFileLocation;
            try {
                //从配置文件获取文件以及csv存放的目录路径
                properties.load(new FileInputStream("src//main//resources//config//global.properties"));
                indexFileLocation = (String) properties.get("index_file_root");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(indexFileLocation).append("//comsubgraph//nameMap").append(".csv");
                //导出
                exportCsv(new File(String.valueOf(stringBuilder)), hashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //测试
            System.out.println("构建无向图所用时间：" + (System.currentTimeMillis() - bgtime));
            System.out.println("图的总大小" + count);

            //释放资源
            hashMap.clear();
            System.gc();

        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addEdge(ArrayList<String> arrayList) {
        //添加新的author
        for (int i = 0; i < arrayList.size(); i++) {
            if (!hashMap.containsKey(arrayList.get(i))) {
                hashMap.put(arrayList.get(i), count++);
                temp = new ArrayList<Integer>();
                myMap.add(temp);
            }
        }
        //写入将这个点mymap
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                if (j != i) {
                    if (!myMap.get(hashMap.get(arrayList.get(i))).contains(hashMap.get(arrayList.get(j)))) {
                        myMap.get(hashMap.get(arrayList.get(i))).add(hashMap.get(arrayList.get(j)));
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getMyMap() {
        return myMap;
    }

    @SuppressWarnings("all")
    public boolean exportCsv(File file, HashMap<String, Integer> map) {
        boolean isSucess = false;
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, Integer> item : map.entrySet()) {
                    bw.append(item.getValue() + "," + item.getKey()).append("\r");
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
