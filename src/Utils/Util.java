package com.test;

import sun.awt.datatransfer.DataTransferer;

import java.io.*;
import java.util.*;

public class Util {


    /**
     * dblp.xml 文件中一级标签：dblp
     * 二级标签：article, book, proceedings, inproceedings, www, mastersthesis, incollection, phdthesis
     * 三级标签：author, title, journal, year, ee, publisher, isbn, volume, month, url, note, cdrom, editor,
     * booktitle, series, crossref, pages, school, cite, number, chapter, address
     * 四级标签：sup, sub, i, tt
     * getTitleIndex()返回记录论文title和对应文件位置偏移量的Hashtable
     * getAuthorIndex()记录论文author和对应论文文件位置偏移量链表的Hashtable
     */
    //记录论文title和对应文件位置偏移量的Hashtable
    static Hashtable<String, Long> titleIndex = new Hashtable<String, Long>();

    //记录论文author和对应论文文件位置偏移量链表的Hashtable
    static Hashtable<String, ArrayList<Long>> authorIndex = new Hashtable<String, ArrayList<Long>>();

    //记录论文year和文章内容的Hashtable
    static Hashtable<Integer, ArrayList<String>> yearSentence = new Hashtable<>();

    static HashSet<String> Preposition = new HashSet();


    static String pathname;
    
    public static Hashtable<String, Long> getTitleIndex() {
        return titleIndex;
    }

    public static Hashtable<String, ArrayList<Long>> getAuthorIndex() {
        return authorIndex;
    }
	//排序耗时
	 public static Map.Entry[] SortByValue(Hashtable<String, Number> ht) {
        long start = System.currentTimeMillis();
        Set<Map.Entry<String, Number>> set = ht.entrySet();
        Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
        Arrays.sort(entries, new Comparator<Map.Entry>() {
            @Override
            //实现接口中的方法
            public int compare(Map.Entry o1, Map.Entry o2) {
                //取得两个值
                int value1 = Integer.parseInt((o1).getValue()
                        .toString());
                int value2 = Integer.parseInt((o2).getValue()
                        .toString());
                //降序
                return ((Comparable) value2).compareTo(value1);
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("排序耗时:" + (end - start));
        return entries;
    }

    public static void main(String[] args) {

        Preposition.add("for");
        Preposition.add("of");
        Preposition.add("on");
        Preposition.add("the");
        Preposition.add("a");
        Preposition.add("to");
        Preposition.add("with");
        Preposition.add("in");
        Preposition.add("and");
        Preposition.add("an");
        Preposition.add("using");
        Preposition.add("by");

        xmlparse("C:\\dbltest\\src\\com\\test\\dblp.xml");
        YearWord();
    }

    public static void xmlparse(String p) {
        long beginTime = System.currentTimeMillis();
        pathname = p;

        MyThread t1 = new MyThread(85, 262972109);
        MyThread t2 = new MyThread(262972109, 520423552);
        MyThread t3 = new MyThread(520423552, 767086229);
        MyThread t4 = new MyThread(767086229, 1019092844);
        MyThread t5 = new MyThread(1019092844, 1181969134);
        MyThread t6 = new MyThread(1181969134, 1243160074);
        MyThread t7 = new MyThread(1243160074, 1304904757);
        MyThread t8 = new MyThread(1304904757, 1366778013);
        MyThread t9 = new MyThread(1366778013, 1428836102);
        MyThread t10 = new MyThread(1428836102, 1641072021);
        MyThread t11 = new MyThread(1641072021, 1899927212);
        MyThread t12 = new MyThread(1899927212, 2155228018L);
        MyThread t13 = new MyThread(2155228018L, 2412732033L);
        MyThread t14 = new MyThread(2412732033L, 2675681975L);
        MyThread t15 = new MyThread(2675681975L, 2785938278L);

        try {
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();
            t6.start();
            t7.start();
            t8.start();
            t9.start();
            t10.start();
            t11.start();
            t12.start();
            t13.start();
            t14.start();
            t15.start();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
            t9.join();
            t10.join();
            t11.join();
            t12.join();
            t13.join();
            t14.join();
            t15.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 测试代码，测试解析和查询性能
         */
        //System.out.println("解析完毕！文件总大小：" + curPosition);
        long beginSearchTime = System.currentTimeMillis();
        System.out.println("解析消耗时间：" + (System.currentTimeMillis() - beginTime));
        System.out.println("查询论文：MvsGCN: A Novel Graph Convolutional Network for Multi-video Summarization.");
        long locator = titleIndex.get("MvsGCN: A Novel Graph Convolutional Network for Multi-video Summarization.");
        byte[] b = new byte[39000];
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(pathname), "rw")) {
            randomAccessFile.seek(locator);
            randomAccessFile.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("论文所在位置：" + locator);
        System.out.println("查询论文消耗时间：" + (System.currentTimeMillis() - beginSearchTime));
        long bgSearchTime = System.currentTimeMillis();
        System.out.println("查询作者:Jiaxin Wu");
        ArrayList<Long> authorList =authorIndex.get("Jiaxin Wu");
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println("Jiaxin Wu论文"+i+"所在位置:  "+authorList.get(i));
        }
        System.out.println("查询作者消耗时间："+(System.currentTimeMillis()-bgSearchTime));
		//获取论文数量前100的作者
        Hashtable<String, Number> Top100Authors = new Hashtable<String, Number>();
//遍历得到的HashTable，键是作者名，值是链表的长度
        Enumeration<String> keys = authorIndex.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Top100Authors.put(key, authorIndex.get(key).size());
        }
        //对Hashtable进行排序
        Map.Entry[] set = SortByValue(Top100Authors);
        //输出排序之后前100的值
        for (int i = 0; i < 100; i++) {
            System.out.println(set[i].getKey().toString() + ":" + set[i].getValue().toString());
        }
    }

    //年度词汇分析
    public static void YearWord(){
        System.out.println(yearSentence.size());
        long firstTime = System.currentTimeMillis();
        Hashtable<Integer,Hashtable<String,Integer>> words = new Hashtable<>();
        Enumeration years = yearSentence.keys();
        while(years.hasMoreElements()) {
            Hashtable<String, Integer> wordsCount = new Hashtable<String, Integer>();
            int thisYear = (Integer) years.nextElement();
            ArrayList<String> titles = yearSentence.get(thisYear);
            //将所有单词都计次并放入wordsCount中
            for (int i = 0; i < titles.size(); i++) {
                //获得title
                String title = titles.get(i);
                //打散title
                if(titles.get(i)==null){
                    continue;
                }

                StringTokenizer st = new StringTokenizer(title, " ,?.!:\"'\n#",false);
                while (st.hasMoreElements()) {
                    String word = st.nextToken().toLowerCase();
                    if(Preposition.contains(word)){
                        continue;
                    }
                    if (wordsCount.containsKey(word)) {
                        //记录加1
                        wordsCount.put(word, wordsCount.get(word) + 1);
                    } else {
                        wordsCount.put(word, 1);
                    }
                }
            }


            //此年的年度词汇集合
            Hashtable<String, Integer> tempTable = new Hashtable<>();
            //选择出出现次数最多的前十个字符


            for (int i = 0; i < 10; i++) {
                int tempCount = 0;
                String tempWord = "";
                Enumeration word = wordsCount.keys();
                while (word.hasMoreElements()) {
                    String thisWord = (String) word.nextElement();
                    int count = wordsCount.get(thisWord);
                    if (count > tempCount) {
                        tempCount = count;
                        tempWord = thisWord;
                    }
                }
                tempTable.put(tempWord, tempCount);
                wordsCount.remove(tempWord);
            }
            words.put(thisYear, tempTable);
        }
        //测试代码
        Enumeration thisYear = words.keys();
        while (thisYear.hasMoreElements()){
            int year = (Integer) thisYear.nextElement();
            System.out.println("years:"+year);
            Enumeration temp = words.get(year).keys();
            while(temp.hasMoreElements()){
                String word = (String) temp.nextElement();
                System.out.print("word:"+word+" ");
                System.out.println("count:"+words.get(year).get(word));
            }
        }
        System.out.println("年度热词生成时间："+(System.currentTimeMillis()-firstTime));
    }


}



class MyThread extends Thread {
    private long beginPosition;
    private long endPosition;

    MyThread(long beginPosition, long endPosition) {
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
    }


    @Override
    public void run() {
        long curPosition = beginPosition;
        long recordPosition;
        RandomAccessFile randomAccessFile = null;
        try {
            int i;
            randomAccessFile = new RandomAccessFile(new File(Util.pathname), "rw");
            randomAccessFile.seek(curPosition);
            byte[] b = new byte[39000];        //经测试分析，文件中记录的最大长度接近但不超多39000
            while (randomAccessFile.read(b) != -1) {     //读取到文件末尾即退出循环
                i=0;
                while (b[i++] != '<') ;       //移动当前数组指针，直到指向'<'
                String str = new String(b, i, 3);    //获取标签前三个字符，与二级标签集合对比
                //关联同一个art下面的year和title
                if (str.equals("art")||str.equals("inp")||str.equals("phd")||str.equals("pro")||str.equals("inc")){
                    //定位到title
                    int p = i;
                    while(b[p++] != '<'|| b[p] != 't');
                    while(b[p++] != '>');
                    int j = 0;
                    //j是记录内容长度的变量
                    while (b[p + j++] != '<') ;
                    //关联year和title
                    int k = p+1;
                    //移动当前数组指针，直到指向'y'
                    while(b[k++] != '<'|| b[k] != 'y');
                    while(b[k++] != '>');
                    int year = Integer.parseInt(new String(b,k,4));

                    if (Util.yearSentence.get(year)!=null)
                    {
                        Util.yearSentence.get(year).add(new String(b, p, j - 1));
                    }
                    else {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(new String(b, p, j - 1));
                        Util.yearSentence.put(year,temp);
                    }
                }
                recordPosition = curPosition + i - 1;      //设置解析到的当前记录的起始文件位置
                String endStr = "/" + str;           //获取当前记录的结束标签，后面判断匹配
                while (i <= 38990) {
                    while (i <= 38990 && b[++i] != '<') ;
                    str = new String(b, ++i, 3);


                    if (str.equals("aut")) {

                        while (b[i++] != '>') ;
                        int j = 0;
                        //j是记录内容长度的变量
                        while (b[i + j++] != '<') ;
                        //写入hashtable
                        String authorName = new String(b,i,j-1);
                        if (Util.authorIndex.get(authorName)!=null)
                        {
                            Util.authorIndex.get(authorName).add(recordPosition);
                        }
                        else {
                            ArrayList<Long> temp = new ArrayList<Long>();
                            temp.add(recordPosition);
                            Util.authorIndex.put(authorName,temp);
                        }
                    }

                    if (str.equals("tit")) {    //找到记录的title，写进hashtable
                        //移动到标签的结尾部分
                        while (b[i++] != '>') ;
                        int j = 0;
                        //j是记录内容长度的变量
                        while (b[i + j++] != '<') ;
                        //写入hashtable
                        Util.titleIndex.put(new String(b, i, j - 1), recordPosition);
                        //title只有一个 所以直接跳出循环
                        break;
                    }
                }
                while (i <= 38990) {
                    while (i <= 38990 && b[i++] != '<') ;    //获取该条记录结束位置
                    str = new String(b, i, 4);
                    if (str.equals(endStr)) {
                        break;
                    }
                }
                curPosition = curPosition + i;   //从该条记录结束位置进行下一条记录解析
                if (curPosition == endPosition) {
                    break;
                }
                randomAccessFile.seek(curPosition);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


