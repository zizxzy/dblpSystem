package com.yejh.xmlparse;/**
 * @author yejh
 * @create 2020-02_16 14:00
 */

import com.yejh.indexinit.IndexInitializer;

import java.io.*;
import java.nio.IntBuffer;
import java.sql.SQLException;
import java.util.*;

/**
 * @description: TODO
 **/
public class XmlParseRunnable implements Runnable {
    //数组不支持泛型
    private static Map<String, List<Long>>[] authorIndex = new HashMap[27];
    private static Map<String, Long>[] titleIndex = new HashMap[27];

    private IndexInitializer indexInitializer;
    private Boolean writeToFile;

    private static String xmlFileLocation;
    private static int batch;

    static {
        for (int i = 0; i < authorIndex.length; ++i) {
            authorIndex[i] = new HashMap<>();
            titleIndex[i] = new HashMap<>();
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            xmlFileLocation = (String) properties.get("xml_file_location");
            batch = Integer.parseInt((String)properties.get("batch"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public IndexInitializer getIndexInitializer() {
        return indexInitializer;
    }

    public void setIndexInitializer(IndexInitializer indexInitializer) {
        this.indexInitializer = indexInitializer;
        this.writeToFile = true;
    }

    private int get_tag_i(String tag) {
        int tag_i = tag.charAt(0) - 'a';
        if (tag_i < 0 || tag_i > 25) {
            tag_i = 26;
        }
        return tag_i;
    }

    private void writeIndex() throws Exception {
        for (int t = 0; t < 27; ++t) {
            String tag = String.valueOf((char) (t + 'a'));
            indexInitializer.writeAuthorIndex(authorIndex[t], tag);
            indexInitializer.writeTitleIndex(titleIndex[t], tag);
            authorIndex[t].clear();
            titleIndex[t].clear();
        }
    }

    @Override
    public void run() {
        String fileName = "D://dblp.xml";
        long startTime = System.currentTimeMillis();
        long curPosition = 85L;
        long endPosition = 2785938278L;

        int authors = 0;
        int titles = 0;

        long recordPosition;
        RandomAccessFile randomAccessFile = null;
        int clear_number = 0;
        try {
            int i;
            randomAccessFile = new RandomAccessFile(new File(fileName), "r");
            randomAccessFile.seek(curPosition);
            byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000
            while (randomAccessFile.read(b) != -1) {     //读取到文件末尾即退出循环

                if (titles % this.batch == 0 && titles != 0) {
                    System.out.println("当前已有" + authors + "条作者记录，" + titles + "条标题记录"
                            + "，当前curPosition: " + curPosition + "(" + ((double) curPosition / endPosition * 100) + "%)");
                    //将index写入文件
                    writeIndex();
                    ++clear_number;
                    System.out.println("当前第" + clear_number + "次清空");
                }

                i = 0;

                //寻找二级标签的左标签
                while (b[i++] != '<') ;       //移动当前数组指针，直到指向'<'
                String str = new String(b, i, 3);    //获取标签前三个字符，与二级标签集合对比
                recordPosition = curPosition + i - 1;      //设置解析到的当前记录的起始文件位置
                String endStr = "/" + str;           //获取当前记录的结束标签，后面判断匹配

                //解析二级标签内包含的author和title信息
                while (i <= 38990) {
                    while (i <= 38990 && b[++i] != '<') ;
                    str = new String(b, ++i, 3);

                    if ("aut".equals(str)) {
                        ++authors;
                        while (b[i++] != '>') ;
                        int j = 0;
                        //j是记录内容长度的变量
                        while (b[i + j++] != '<') ;

                        String authorName = new String(b, i, j - 1).trim();
                        if ("".equals(authorName)) {
                            System.out.println("author为空串: " + authorName);
                            continue;
                        }

                        String tag = authorName.substring(0, 1).toLowerCase();
                        //得到对应的tag_i
                        int tag_i = get_tag_i(tag);

                        if (authorIndex[tag_i].get(authorName) != null) {
                            authorIndex[tag_i].get(authorName).add(recordPosition);
                        } else {
                            ArrayList<Long> temp = new ArrayList<Long>();
                            temp.add(recordPosition);
                            authorIndex[tag_i].put(authorName, temp);
                        }
                    }

                    if ("tit".equals(str)) {    //找到记录的title，写进hashtable
                        ++titles;
                        //移动到标签的结尾部分
                        while (b[i++] != '>') ;
                        int j = 0;
                        //j是记录内容长度的变量
                        //针对于title标签内包含子标签的情况，<title><i>O</i>-Notation.</title>
                        //应该先定位</title>的位置，然后把title的内容提取出来
                        while (i + j < 38990) {
                            while ( b[i + j++] != '<') ;    //获取该条记录结束位置
                            str = new String(b, i+j, 4);
                            if ("/tit".equals(str)) {
                                break;
                            }
                        }

                        String titleName = new String(b, i, j - 1).trim();
                        if ("".equals(titleName)) {
                            //针对于title标签内包含子标签的情况，<title><i>O</i>-Notation.</title>
                            //应该先定位</title>的位置，然后把title的内容提取出来
                            String record = new String(b, 0, i + j + 50);
                            System.out.println("title为空串: " + record);

                            break;
                        }

                        String tag = titleName.substring(0, 1).toLowerCase();
                        //得到对应的tag_i
                        int tag_i = get_tag_i(tag);

                        //写入hashtable
                        titleIndex[tag_i].put(titleName, recordPosition);
                        //title只有一个 所以直接跳出循环
                        break;
                    }
                }
                //该记录包含的作者和标题解析完毕，寻找该记录的右（闭合）标签
                while (i <= 38990) {
                    while (i <= 38990 && b[i++] != '<') ;    //获取该条记录结束位置
                    str = new String(b, i, 4);
                    if (str.equals(endStr)) {
                        break;
                    }
                }
                //定位到右标签的结束位置
                while (i <= 38990 && b[i++] != '>') ;

                curPosition = curPosition + i;   //从该条记录结束位置进行下一条记录解析

                //文件解析完成，结束读取
                if (curPosition >= endPosition) {
                    break;
                }
                //继续解析下一条记录
                randomAccessFile.seek(curPosition);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();

            System.out.println("当前已有" + authors + "条作者记录，" + titles + "条标题记录");

            try {
                //将index写入文件
                writeIndex();
                System.out.println("当前第" + clear_number + "次清空");
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + ": 解析完成，耗时" + (endTime - startTime));
        }
    }
}
