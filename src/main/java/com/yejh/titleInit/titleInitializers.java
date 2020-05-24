package com.yejh.titleInit;

import com.yejh.utils.TxtUtil;
import com.yejh.AhoCorasickDoubleArrayTrie.*;

import java.io.*;
import java.util.*;

/**
 * 旧文件，已被废弃
 */

@Deprecated
public class titleInitializers implements Runnable {
    private  static  String  xmlFileLocation;
    private static int batch;
    private static HashMap<String,ArrayList<Long>> stringArrayListHashMap = new HashMap<>();

    static {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            xmlFileLocation = (String) properties.get("xml_file_location");
            batch = Integer.parseInt((String) properties.get("batch"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //初始化列表
    private static void initListByStrAndLocation(String str, Long location)
    {

       if (stringArrayListHashMap.get(str)==null)
       {
           ArrayList<Long> longArrayList = new ArrayList<>();
           longArrayList.add(location);
           stringArrayListHashMap.put(str,longArrayList);
       }
       else
       {
           stringArrayListHashMap.get(str).add(location);
       }
    }

    @Override
    public void run() {
        String fileName = xmlFileLocation;
        long startTime = System.currentTimeMillis();
        long curPosition = 85L;
        long endPosition = 2785899298L - 1;
        //2785899298L 的位置刚好对应</dblp>

        int titles = 0;

        long recordPosition;
        RandomAccessFile randomAccessFile = null;
        int clear_number = 0;
        try {
            randomAccessFile = new RandomAccessFile(new File(fileName), "r");
            randomAccessFile.seek(curPosition);
            int byteLen = 16384;
            int maxByteLen = 39000;
            byte[] b = new byte[byteLen];        //经测试分析，文件中记录的最大长度接近但不超多39000
            int read = -1;
            int i;
            while ((read = randomAccessFile.read(b)) != -1) {     //读取到文件末尾即退出循环

                if (titles % batch == 0 && titles != 0) {
                    //已经解析了几次，清空非静态的内存，只有当titles的条数是batch的倍数时候才会进入
                    System.out.println("当前已有" + titles + "条标题记录"
                            + "，当前curPosition: " + curPosition + "(" + ((double) curPosition / endPosition * 100) + "%)");
                    ++clear_number;
                    System.out.println("当前第" + clear_number + "次清空");
                }

                i = 0;

                //寻找二级标签的左标签
                while (b[i++] != '<') ;       //移动当前数组指针，直到指向'<'
                //得到标签的内容
                String str = TxtUtil.getContent(b, i);
                recordPosition = curPosition + i - 1;        //设置解析到的当前记录的起始文件位置

                //更新i
                i += str.length() + 1;

                String endStr = "/" + str;           //获取当前记录的结束标签，后面判断匹配
                //System.out.println(endStr);

                //解析二级标签内包含的author和title信息
                while (true) {
                    while (b[i++] != '<') ;
                    str = TxtUtil.getContent(b, i);
                    i += str.length();
                    while (b[i++] != '>');

                    if ("title".equals(str)) {    //找到记录的title，写进hashtable
                        //有几个titles
                        ++titles;
                        int j = 0;
                        //j是记录内容长度的变量
                        //针对于title标签内包含子标签的情况，<title><i>O</i>-Notation.</title>
                        //应该先定位</title>的位置，然后把title的内容提取出来
                        while (true) {
                            try {
                                while (b[i + j++] != '<') ;    //获取该条记录结束位置
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("[debug]cur: " + curPosition +
                                        ", i=" + i + ", j=" + j);
                            }
                            str = new String(b, i + j, 4);
                            if ("/tit".equals(str)) {
                                break;
                            }
                        }
                        String titleName = new String(b, i, j - 1).trim();
                        //忽略这个if，这是用作bug测试的
                        if ("".equals(titleName)) {
                            String record = new String(b, 0, i + j + 50);
                            System.out.println("title为空串: " + record);
                            break;
                        }

                        //写入hashmap，curPosition一直到最后才更新
                        initListByStrAndLocation(titleName,curPosition);
                        //title只有一个 所以直接跳出循环
                        break;
                    }
                }

                //该记录包含的作者和标题解析完毕，寻找该记录的右（闭合）标签，二级标签
                boolean flag = false;
                byte[] tempBytes = null;
                while (i < byteLen) {
                    while (i < byteLen && b[i++] != '<') ;    //获取该条记录结束位置
                    str = TxtUtil.getContent(b, i);

                    if (endStr.equals(str)) {
                        //System.out.println(endStr);
                        i += str.length() + 1;
                        break;
                    }
                    if (str == null) {
                        flag = true;
                        //表明当前记录超过了byteLen
                        tempBytes = new byte[maxByteLen];
                        randomAccessFile.seek(curPosition);
                        randomAccessFile.read(tempBytes);
                        while (true) {
                            while (tempBytes[i++] != '<') ;
                            str = TxtUtil.getContent(tempBytes, i);
                            if (endStr.equals(str)) {
                                //System.out.println(endStr);
                                i += str.length() + 1;
                                break;
                            }
                        }

                    }
                }

                curPosition += i;   //从该条记录结束位置进行下一条记录解析

                //文件解析完成，结束读取
                if (curPosition >= endPosition) {
                    break;
                }
                //继续解析下一条记录
                randomAccessFile.seek(curPosition);

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[debug]cur: " + curPosition);
            throw new RuntimeException("越界");
        } finally {
            long endTime = System.currentTimeMillis();

            System.out.println("当前已有"+ titles + "条标题记录");

            try {
                //将index写入文件

                System.out.println("当前第" + clear_number + "次清空");
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + ": 解析完成，耗时" + (endTime - startTime));
        }
    }
}
