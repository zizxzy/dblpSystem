package com.yejh.xmlparse.impl;/**
 * @author yejh
 * @create 2020-02_16 14:00
 */

import com.yejh.indexinit.IndexInitializer;
import com.yejh.utils.TxtUtil;
import com.yejh.xmlparse.XmlParser;

import java.io.*;
import java.nio.IntBuffer;
import java.sql.SQLException;
import java.util.*;

/**
 * IndexXmlParser用于解析dblp.xml文件，生成索引文件
 **/
public class IndexXmlParser implements XmlParser {
    //数组不支持泛型
    private static Map<String, List<Long>>[] authorIndex = new HashMap[27];
    private static Map<String, Long>[] titleIndex = new HashMap[27];

    private IndexInitializer indexInitializer;
    private Boolean writeToFile = true;

    private static String xmlFileLocation;
    private static int batch;

    static {
        for (int i = 0; i < authorIndex.length; ++i) {
            authorIndex[i] = new HashMap<>();
            titleIndex[i] = new HashMap<>();
        }
        try {
            Properties properties = TxtUtil.getProperties();
            xmlFileLocation = (String) properties.get("xml_file_location");
            batch = Integer.parseInt((String) properties.get("batch"));
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

    /**
     * 写索引文件
     */
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
        String fileName = xmlFileLocation;
        long startTime = System.currentTimeMillis();
        long curPosition = 85L;
        long endPosition = 2785899298L - 1;
        //2785899298L 的位置刚好对应</dblp>

        int authors = 0;
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

                    if ("author".equals(str)) {
                        ++authors;
                        int j = 0;
                        while (b[i + j++] != '<') ;
                        String authorName = new String(b, i, j - 1);
                        i += j;

                        //debug分支
                        if ("".equals(authorName)) {
                            System.out.println("author为空串: " + authorName);
                            continue;
                        }

                        //得到对应的tag_i
                        int tag_i = TxtUtil.get_tag_i(authorName);

                        if (authorIndex[tag_i].get(authorName) != null) {
                            authorIndex[tag_i].get(authorName).add(recordPosition);
                        } else {
                            ArrayList<Long> temp = new ArrayList<Long>();
                            temp.add(recordPosition);
                            authorIndex[tag_i].put(authorName, temp);
                        }
                    }

                    if ("title".equals(str)) {    //找到记录的title，写进hashtable
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

                        //得到对应的tag_i
                        int tag_i = TxtUtil.get_tag_i(titleName);

                        //写入hashtable
                        titleIndex[tag_i].put(titleName, recordPosition);
                        //title只有一个 所以直接跳出循环
                        break;
                    }
                }

                //该记录包含的作者和标题解析完毕，寻找该记录的右（闭合）标签
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
