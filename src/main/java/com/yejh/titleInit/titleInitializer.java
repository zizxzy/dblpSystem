package com.yejh.titleInit;

import com.yejh.bean.Article;
import com.yejh.search.RecordSearcher;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class titleInitializer {
    private static String indexFileLocation;
    public static List<Article> stringListHashMap = new ArrayList<Article>();

    static {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = RecordSearcher.class.getClassLoader()
                    .getResourceAsStream("config//global.properties");
            System.out.println("titleInitializer: " + resourceAsStream);
            properties.load(resourceAsStream);
            indexFileLocation = (String) properties.get("title_file_root");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*titleInit();*/
    }

    /*    public static  int inita()
        {
            a = 16;
            return 16;
        }*/
    public static List<Article> getStringListHashMap() {
        return stringListHashMap;
    }

    /**
     * 初始化源列表
     *
     * @return 返回初始化的结果
     * @throws NullPointerException
     */

    public boolean titleInit() throws NullPointerException {
        stringListHashMap = new ArrayList<Article>();
        File[] file = new File(indexFileLocation).listFiles();

        readThread readThread1 = new readThread(0, 1, file);
        readThread readThread2 = new readThread(2, 3, file);
        readThread readThread3 = new readThread(4, 6, file);
        readThread readThread4 = new readThread(7, 8, file);
        readThread readThread5 = new readThread(9, 10, file);
        readThread readThread6 = new readThread(11, 12, file);
        readThread readThread7 = new readThread(13, 14, file);
        readThread readThread8 = new readThread(15, 16, file);
        readThread readThread9 = new readThread(17, 18, file);
        readThread readThread10 = new readThread(19, 21, file);
        readThread readThread11 = new readThread(22, 24, file);
        readThread readThread12 = new readThread(25, 26, file);
        try {
            System.out.println("初始化模糊查询需要的title列表开始---------------------------------------");
            readThread1.start();
            readThread2.start();
            readThread3.start();
            readThread4.start();
            readThread5.start();
            readThread6.start();
            readThread7.start();
            readThread8.start();
            readThread9.start();
            readThread10.start();
            readThread11.start();
            readThread12.start();
            readThread1.join();
            readThread2.join();
            readThread3.join();
            readThread4.join();
            readThread5.join();
            readThread6.join();
            readThread7.join();
            readThread8.join();
            readThread9.join();
            readThread10.join();
            readThread11.join();
            readThread12.join();
            System.out.println("初始化模糊查询需要的title列表完成-------------------------------------");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

    }
}

class readThread extends Thread {
    private int beginPosition;
    private int endPosition;
    private File[] files;

    /**
     * 根据起始位置、结束位置以及文件数组读取title
     * @param beginPosition 起始位置
     * @param endPosition   结束位置
     * @param files         文件数组
     */
    public readThread(int beginPosition, int endPosition, File[] files) {
        this.files = files;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
    }

    @Override
    public void run() {
        Article article = null;
        for (int i = beginPosition; i <= endPosition; i++) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(new File(files[i].getAbsolutePath()), "r");
                String lineStr = null;
                while ((lineStr = randomAccessFile.readLine()) != null) {
                    article = Article.initArticle(lineStr);
                    titleInitializer.stringListHashMap.add(article);
                }
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
