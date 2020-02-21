package com.yejh.search;/**
 * @author yejh
 * @create 2020-02_19 11:45
 */

import com.yejh.bean.Article;
import com.yejh.bean.Author;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.Set;

/**
 * @description: TODO
 **/
public class RecordSearcherTest {

    private static String indexFileLocation;
    private static String xmlFileLocation;


    static {
        Properties properties = new Properties();
        try {
//            InputStream resourceAsStream = RecordSearcher.class.getClassLoader()
//                    .getResourceAsStream("config//global.properties");
//            System.out.println(resourceAsStream);
//            properties.load(resourceAsStream);
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            indexFileLocation = (String) properties.get("index_file_root");
            xmlFileLocation = (String) properties.get("xml_file_location");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test() throws Exception {
        String title = "\"Synchrony\" as a way to choose an interacting partner.";
        //得到的location应该是189857193
        Article res = RecordSearcher.binarySearchByTitle(title);
        System.out.println(res);
        System.out.println("论文记录如下======================");
        if(res == null){
            return;
        }
        for (String record : res.getRecords()){
            System.out.println(record);
        }
    }

    @Test
    public void test2() throws Exception {

        long location = 189857193;
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(xmlFileLocation), "r");
        byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000

        randomAccessFile.seek(location);
        randomAccessFile.read(b);

        String oneRecord = RecordSearcher.getOneRecord(b);
        System.out.println(oneRecord);
    }

    @Test
    public void test3() throws Exception {
        long startTime = System.currentTimeMillis();
        //Alfredo Cuzzocrea拥有论文423篇，耗时999ms

        //如果是遍历搜索，搜索第20万的记录至少需要1s
        //String authorName = "Anatole Khelif";
        String authorName = "Ana Paula Caruso";
        Author author = RecordSearcher.searchByAuthor(authorName, true);
//        List<String> articles = author.getArticles();
//        for(String article: articles){
//            System.out.println(article);
//            System.out.println("==========================");
//        }

        Set<String> collaborators = author.getCollaborators();
        System.out.println("合作者如下：");
        System.out.println(collaborators);


        System.out.println("共耗时" + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void test4() throws Exception {
        long pos = 1100L;
        String fileName = RecordSearcher.getFileName("aaa");
        if (fileName == null) {
            return;
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(fileName), "r");

        randomAccessFile.seek(pos);
        byte[] bytes = new byte[1024];
        randomAccessFile.read(bytes);
        System.out.println(new String(bytes));

        long res = RecordSearcher.toNextRecordHead(randomAccessFile, pos);
        randomAccessFile.seek(res);
        randomAccessFile.read(bytes);
        System.out.println("============res=" + res);
        System.out.println(new String(bytes));
    }

    @Test
    public void test5() throws Exception {

        Author author = RecordSearcher.binarySearchByAuthor("Toshihiro Osaragi", true);
        System.out.println(author);
        if(author == null){
            return;
        }
        if(author.getCollaborators()!=null){
            System.out.println("=============================");
            System.out.println("合作者如下");
            for(String a : author.getCollaborators()){
                System.out.println(a);
            }
        }
        if(author.getArticles() != null){
            System.out.println("=============================");
            System.out.println("记录如下");
            for(String a : author.getArticles()){
                System.out.println(a);
            }
        }
    }
}
