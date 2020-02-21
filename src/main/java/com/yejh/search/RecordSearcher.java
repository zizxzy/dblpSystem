package com.yejh.search;/**
 * @author yejh
 * @create 2020-02_17 15:26
 */

import com.yejh.bean.Article;
import com.yejh.bean.Author;
import com.yejh.utils.TxtUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

/**
 * @description: TODO
 **/
public class RecordSearcher {
    private static String indexFileLocation;
    private static String xmlFileLocation;


    static {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = RecordSearcher.class.getClassLoader()
                    .getResourceAsStream("config//global.properties");
            System.out.println(resourceAsStream);
            properties.load(resourceAsStream);
            //properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            indexFileLocation = (String) properties.get("index_file_root");
            xmlFileLocation = (String) properties.get("xml_file_location");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //二分查找文件记录
    private static String binarySearchRecord(RandomAccessFile randomAccessFile, String nameToFind) throws IOException {
        if (nameToFind == null || "".equals(nameToFind)) {
            throw new RuntimeException("要查找的名称不能为空！");
        }
        String res = null;
        long i = 0, j = randomAccessFile.length();
        long last_i = i;
        long mid;
        byte[] bytes = new byte[1024];
        while (i < j) {
            mid = i + ((j - i) >> 1);
            //定位到下一条记录的开始位置
            mid = toNextRecordHead(randomAccessFile, mid);
            //检验记录的作者名称是否相同
            String name;
            try {
                name = TxtUtil.getNameInRecord(randomAccessFile, mid);
            } catch (RuntimeException e) {
                //此时记录到底了，很可能要搜索的记录就是最后一条，此时就要退出循环，进入特殊情况
                i = j;
                break;
            }
            System.out.println("mid=" + mid + ", name=" + name);
            if (nameToFind.equals(name)) {
                long lineEnd = toNextRecordHead(randomAccessFile, mid);
                byte[] lineBytes = new byte[(int) (lineEnd - mid - 1)];
                randomAccessFile.seek(mid);
                randomAccessFile.read(lineBytes);
                res = new String(lineBytes);
                break;
            } else if (nameToFind.compareTo(name) < 0) {
                //如果记录在mid之上
                if (j == mid) {
                    //退出循环，进入特殊情况
                    i = j;
                    break;
                }
                j = mid;

            } else {
                //如果记录在mid之下
                last_i = i;
                i = mid;
            }
        }
        //可能会漏掉一些特殊情况，在此补充
        if (i == j) {
            while (last_i != j) {
                String name = TxtUtil.getNameInRecord(randomAccessFile, last_i);
                if (nameToFind.equals(name)) {
                    long lineEnd = toNextRecordHead(randomAccessFile, last_i);
                    byte[] lineBytes = new byte[(int) (lineEnd - last_i - 1)];
                    randomAccessFile.seek(last_i);
                    randomAccessFile.read(lineBytes);
                    res = new String(lineBytes);
                    break;
                }
                last_i = toNextRecordHead(randomAccessFile, last_i);
            }
        }
        return res;
    }

    //使用二分搜索来查询论文记录
    public static Article binarySearchByTitle(String title) throws IOException {
        if (title == null || "".equals(title)) {
            return null;
        }
        Article article = null;
        //1、得到首字母对应的tag
        String tag = TxtUtil.getTag(title);

        //防止title前后带有 " 双引号
        title = TxtUtil.pairTrim(title, '\"');

        //2、根据tag找文件
        String fileName = indexFileLocation + "//title//" + tag + ".csv";
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));

        //3、二分查找记录
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(fileName), "r");
        String recordLine = binarySearchRecord(randomAccessFile, title);
        if (recordLine == null || "".equals(recordLine)) {
            return null;
        }
        //4、如果找到了，根据找到的location在dblp.xml中搜索对应的完整记录
        article = Article.initArticle(recordLine);
        System.out.println("binarySearchByTitle(" + title + ")成功：" + article);
        List<Long> locations = article.getLocations();
        randomAccessFile = new RandomAccessFile(new File(xmlFileLocation), "r");
        byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000

        for(Long location : locations){
            randomAccessFile.seek(location);
            randomAccessFile.read(b);

            String oneRecord = getOneRecord(b);
            article.addRecord(oneRecord);
        }

        return article;
    }

    //输入完整的论文的题目，能展示该论文的其他相关信息
    @Deprecated
    public static Article searchByTitle(String title) throws IOException {
        if (title == null || "".equals(title)) {
            return null;
        }
        //1、得到首字母对应的tag
        String tag = TxtUtil.getTag(title);

        //防止title前后带有 " 双引号
        title = TxtUtil.pairTrim(title, '\"');
        Article article = null;

        //2、根据tag找文件
        String fileName = indexFileLocation + "//title//" + tag + ".csv";
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));

        //3、一行行遍历文件，直到找到title或者到达文件末尾
        String line = null;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            article = Article.initArticle(line);
            if (title.equals(article.getTitle())) {
                break;
            }
        }

        //4、如果找到了，根据找到的location在dblp.xml中搜索对应的完整记录
        if (article != null && title.equals(article.getTitle())) {
            System.out.println("searchByTitle(" + title + ")成功：" + article);
            List<Long> locations = article.getLocations();
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(xmlFileLocation), "r");
            byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000

            for(Long location : locations){
                randomAccessFile.seek(location);
                randomAccessFile.read(b);

                String oneRecord = getOneRecord(b);
                article.addRecord(oneRecord);
            }
        }

        //5、返回记录
        return article != null && title.equals(article.getTitle()) ? article : null;
    }

    public static Author binarySearchByAuthor(String authorName) throws Exception {

        return binarySearchByAuthor(authorName, false);
    }


    /**
     * @Description: 基本搜索功能。输入完整的作者名，能展示该作者发表的所有论文信息。
     * 由于索引文件中作者名称是有序的，所以采用二分搜索。时间复杂度为O(log n)
     * @Param: searchPaper：是否搜索他的论文记录，searchCollaborator：是否搜索他的合作者
     * @return:
     * @Author: yejh
     * @Date: 2020/2/17
     */
    public static Author binarySearchByAuthor(String authorName, boolean searchCollaborator) throws Exception {
        Author author = null;
        String fileName = getFileName(authorName);
        if (fileName == null) {
            return null;
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(fileName), "r");
        String recordLine = binarySearchRecord(randomAccessFile, authorName);
        if (recordLine == null || "".equals(recordLine)) {
            return null;
        }
        author = Author.initAuthor(recordLine);
        if (searchCollaborator) {
            author = searchCollaboratorsByAuthor(author);
        }
        return author;
    }

    public static Author searchCollaboratorsByAuthor(Author author) throws IOException {
        //逐个寻找author发表的论文
        List<Long> locations = author.getLocations();
        List<String> articles = new ArrayList<>();
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(xmlFileLocation), "r");
        byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000

        for (Long location : locations) {
            randomAccessFile.seek(location);
            randomAccessFile.read(b);

            //搜索记录
            String oneRecord = getOneRecord(b);
            articles.add(oneRecord);

            //搜索合作者
            Set<String> collaborators = searchCollaborators(oneRecord);
            author.addCollaborators(collaborators);

        }
        author.setArticles(articles);
        return author;
    }


    protected static long toNextRecordHead(RandomAccessFile randomAccessFile, long position) throws IOException {
        randomAccessFile.seek(position);
        byte[] bytes = new byte[1024];
        int read;
        int offset = 0;
        while ((read = randomAccessFile.read(bytes)) != -1) {
            boolean flag = false;
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] == '\n') {
                    flag = true;
                    offset += i;
                    break;
                }
            }
            if (flag) {
                break;
            } else {
                offset += bytes.length;
            }
        }
        return position + offset + 1;
    }


    @Deprecated
    public static Author searchByAuthor(String authorName, boolean searchCollaborator) throws Exception {
        return searchByAuthor(authorName, true, searchCollaborator);
    }

    /**
     * @Description: 基本搜索功能。输入完整的作者名，能展示该作者发表的所有论文信息。
     * @Param: searchPaper：是否搜索他的论文记录，searchCollaborator：是否搜索他的合作者
     * @return:
     * @Author: yejh
     * @Date: 2020/2/17
     */
    @Deprecated
    public static Author searchByAuthor(String authorName, boolean searchPaper, boolean searchCollaborator) throws Exception {

        Author author = null;
        String fileName = getFileName(authorName);
        if (fileName == null) {
            return null;
        }
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));

        //3、一行行遍历文件，直到找到title或者到达文件末尾
        String line = null;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            author = Author.initAuthor(line);
            if (authorName.equals(author.getName())) {
                break;
            }
        }

        //4、如果找到了，根据找到的location在dblp.xml中搜索对应的完整记录
        if (author != null && authorName.equals(author.getName())) {
            System.out.println("searchByAuthor(" + authorName + ")成功：" + author);
            if (!searchPaper && !searchCollaborator) {
                return author;
            }

            //逐个寻找author发表的论文
            List<Long> locations = author.getLocations();
            List<String> articles = new ArrayList<>();
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(xmlFileLocation), "r");
            byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000

            for (Long location : locations) {
                randomAccessFile.seek(location);
                randomAccessFile.read(b);

                //搜索记录
                String oneRecord = getOneRecord(b);
                articles.add(oneRecord);

                if (searchCollaborator) {
                    //搜索合作者
                    Set<String> collaborators = searchCollaborators(oneRecord);
                    author.addCollaborators(collaborators);
                }
            }
            author.setArticles(articles);
        }

        //5、返回记录
        return author != null && authorName.equals(author.getName()) ? author : null;
    }


    //相关搜索。输入作者名，能展示于该作者有合作关系的其他所有作者。
    public static Set<String> searchCollaborators(String record) {
        String leftTag = "aut";
        //String rightTag = "/aut";
        Set<String> collaborators = new HashSet<>();

        int i = 5;
        byte[] bytes = record.getBytes();
        int len = bytes.length;
        String str = null;
        //已知author标签总是连在一起出现，所以一旦author标签结束后的标签不是author就提前结束
        boolean flag = false;
        while (i < len) {
            while (i < len && bytes[i++] != '<') ;
            str = new String(bytes, i, 3);
            if (leftTag.equals(str)) {
                flag = true;
                while (bytes[i++] != '>') ;
                int j = 0;
                //j是记录内容长度的变量
                while (bytes[i + j++] != '<') ;
                String collaboratorName = new String(bytes, i, j - 1).trim();
                collaborators.add(collaboratorName);

                //别忘了把j遍历的部分加回i上
                i += j + 3;
            } else if (flag) {
                break;
            }
        }
        return collaborators;
    }

    //返回该tag对应的文件中前size条标题索引
    public static List<Article> getArticlesByTag(String tag, int pageNumber, int pageSize) throws FileNotFoundException {
        if (tag == null || "".equals(tag)) {
            return null;
        }
        List<Article> articles = new ArrayList<>();
        int start = (pageNumber-1)*pageSize;

        //根据tag找文件
        String fileName = indexFileLocation + "//title//" + tag + ".csv";
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));

        String line = null;
        int i = 0;
        while (scanner.hasNextLine() && i < start) {
            scanner.nextLine();
            ++i;
        }
        while (scanner.hasNextLine() && i < start + pageSize){
            articles.add(Article.initArticle(scanner.nextLine()));
            ++i;
        }
        return articles;
    }

    //返回该tag对应的文件中前size条作者索引
    public static List<Author> getAuthorsByTag(String tag, int pageNumber, int pageSize) throws FileNotFoundException {
        if (tag == null || "".equals(tag)) {
            return null;
        }
        List<Author> authors = new ArrayList<>();
        int start = (pageNumber-1)*pageSize;

        //根据tag找文件
        String fileName = indexFileLocation + "//author//" + tag + ".csv";
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));

        String line = null;
        int i = 0;
        while (scanner.hasNextLine() && i < start) {
            scanner.nextLine();
            ++i;
        }
        while (scanner.hasNextLine() && i < start + pageSize){
            authors.add(Author.initAuthor(scanner.nextLine()));
            ++i;
        }
        return authors;
    }

    protected static String getFileName(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        //1、得到首字母对应的tag
        String tag = TxtUtil.getTag(str);

        //2、根据tag找文件
        return indexFileLocation + "//author//" + tag + ".csv";
    }
    //将多条记录筛选出第一条记录

    protected static String getOneRecord(byte[] bytes) {
        String str = new String(bytes);
        String leftTag = str.substring(1, 4);
        String rightTag = "/" + leftTag;
        int endLocation = -1;

        int i = 4;
        while (true) {
            while (bytes[i++] != '<') ;    //获取该条记录结束位置
            str = new String(bytes, i, 4);
            if (rightTag.equals(str)) {
                while (bytes[i++] != '<') ;
                endLocation = i - 1;
                break;
            }
        }
        if (endLocation != -1) {
            return new String(bytes, 0, endLocation);
        } else {
            throw new RuntimeException("没有对应的右标签");
        }
    }
}
