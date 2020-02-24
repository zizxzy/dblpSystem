package comsubgraph;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class MyMap {
    private ArrayList<ArrayList<Integer>> myMap = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> temp = null;
    private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    private int count = 0;

    @SuppressWarnings("all")
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
                if(endStr.equals("/boo")){
                    while (i <= 38990) {
                        while (i <= 38990 && b[i++] != '<') ;    //获取该条记录结束位置
                        str = new String(b, i, 6);
                        if (str.equals("/book>")) {
                            break;
                        }
                    }
                }else{
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

            //释放资源
//            for (Map.Entry<String, Integer> stringIntegerEntry : hashMap.entrySet()) {
//                authorNameMap.put(stringIntegerEntry.getValue(),stringIntegerEntry.getKey());
//            }

            //测试
            System.out.println("构建无向图所用时间："+ (System.currentTimeMillis() - bgtime));
            System.out.println("图的总大小" + count);
//            for (int i = 0; i < 100; i++) {
//                System.out.println(i+": "+myMap.get(i));
//            }
            hashMap.clear();
            System.gc();

        } catch (
                Exception e) {
            e.printStackTrace();
        }finally {
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
            if (!hashMap.containsKey(arrayList.get(i))){
                hashMap.put(arrayList.get(i), count++);
                temp = new ArrayList<Integer>();
                myMap.add(temp);
            }
        }
        //写入mymap
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j <arrayList.size() ; j++) {
                if(j!=i){
                    if(!myMap.get(hashMap.get(arrayList.get(i))).contains(hashMap.get(arrayList.get(j)))){
                        myMap.get(hashMap.get(arrayList.get(i))).add(hashMap.get(arrayList.get(j)));
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getMyMap(){
        return myMap;
    }

//    public HashMap<Integer, String> getAuthorNameMap() {
//        return authorNameMap;
//    }
}
