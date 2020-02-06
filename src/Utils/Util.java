package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Hashtable;

/*
xml解析的基础部分，利用RandomAccessFile模拟sax流式读取，同时获取title和偏移量组织成Hashtable
*/
public class Util {


    /**
     * dblp.xml 文件中一级标签：dblp
     * 二级标签：article, book, proceedings, inproceedings, www, mastersthesis, incollection, phdthesis
     * 三级标签：author, title, journal, year, ee, publisher, isbn, volume, month, url, note, cdrom, editor,
     * booktitle, series, crossref, pages, school, cite, number, chapter, address
     * 四级标签：sup, sub, i, tt
     */
    private static HashSet<String> first = new HashSet<String>();  //一二三四级标签集合
    private static HashSet<String> second = new HashSet<String>();
    private static HashSet<String> third = new HashSet<String>();
    private static HashSet<String> four = new HashSet<String>();
    private static long curPosition = 85;          //当前文件指针位置
    private static long recordPosition = 0;        //当前记录的起始位置

    //记录论文title和对应文件位置偏移量的Hashtable
    private static Hashtable<String, Long> titleIndex = new Hashtable<String, Long>();

    public static Hashtable<String, Long> getTitleIndex() {
        return titleIndex;
    }

    public static void main(String[] args) {
        xmlparse("E:/javaDtLea/dblp.xml");
    }


    public static void xmlparse(String pathname) {
        long beginParseTime = System.currentTimeMillis();
        initSet();       //初始化标签集合赋值
        RandomAccessFile randomAccessFile = null;
        try {
            //以读写方式读取文件
            randomAccessFile = new RandomAccessFile(new File(pathname), "rw");
            randomAccessFile.seek(curPosition);
            byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000
            while (randomAccessFile.read(b) != -1) {     //读取到文件末尾即退出循环
                int i = 0;
                //这边i在后面已经+1了，所以后面需要减去1
                while (b[i++] != '<') ;       //移动当前数组指针，直到指向'<'，二级标签的情况
                String str = new String(b, i, 3);    //获取标签前三个字符，与二级标签集合对比
                //当读取到的值包含在二级标签中才进入，节约时间
                if (second.contains(str)) {
                    recordPosition = curPosition + i - 1;      //设置解析到的当前记录的起始文件位置 二级标签的起始位置
                    String endStr = "/" + str;           //获取当前记录的结束标签，后面判断匹配
                    while (i <= 38990) {
                        //读取到二级标签的<
                        while (i <= 38990 && b[++i] != '<') ;
                        str = new String(b, ++i, 3);
                        if (str.equals("tit")) {    //找到记录的title，写进hashtable
                            //移动到标签的结尾部分
                            while (b[i++] != '>') ;
                            int j = 0;
                            //j是记录内容长度的变量
                            while (b[i + j++] != '<') ;
                            //写入hashtable
                            titleIndex.put(new String(b, i, j - 1), recordPosition);
                            //title只有一个 所以直接跳出循环
                            break;
                        }
                    }
                    while (i <= 38990) {
                        //移动到标签结束的位置
                        while (i <= 38990 && b[i++] != '<') ;    //获取该条记录结束位置
                        str = new String(b, i, 4);
                        //二级标签结束的位置
                        if (str.equals(endStr)) {
                            //读取到吧标签结束的位置跳出循环
                            break;
                        }
                    }
                    curPosition = curPosition + i;   //从该条记录结束位置进行下一条记录解析，更新curPosition
                    randomAccessFile.seek(curPosition);
                }
            }
            /**
             * 测试代码，测试解析和查询性能
             */
            System.out.println("解析完毕！文件总大小：" + curPosition);
            long beginSearchTime = System.currentTimeMillis();
            System.out.println("解析消耗时间：" + (System.currentTimeMillis() - beginParseTime));
            System.out.println("查询论文：Secure Coprocessor.");
            long locator = titleIndex.get("Secure Coprocessor.");
            randomAccessFile.seek(locator);
            randomAccessFile.read(b);
            System.out.println("论文所在位置：" + locator);
            System.out.println("查询消耗时间：" + (System.currentTimeMillis() - beginSearchTime));
            System.out.println(new String(b));
           // System.out.print("最长记录长度：" + maxLen);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 标签集合赋值
     * 全部设为3个字符串便于匹配操作
     */
    private static void initSet() {
        first.add("dpl");
        second.add("art");
        second.add("boo");
        second.add("pro");
        second.add("inp");
        second.add("www");
        second.add("mas");
        second.add("inc");
        second.add("phd");
        third.add("aut");
        third.add("tit");
        third.add("jou");
        third.add("yea");
        third.add("ee>");
        third.add("pub");
        third.add("isb");
        third.add("vol");
        third.add("mon");
        third.add("url");
        third.add("not");
        third.add("cdr");
        third.add("edi");
        third.add("boo");
        third.add("ser");
        third.add("cro");
        third.add("pag");
        third.add("sch");
        third.add("cit");
        third.add("num");
        third.add("cha");
        third.add("add");
        four.add("sup");
        four.add("sub");
        four.add("i");
        four.add("tt");
    }
}
