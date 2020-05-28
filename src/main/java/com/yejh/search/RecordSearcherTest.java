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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * RecordSearcherTest类用于测试RecordSearcher类的功能和性能
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
    public void testBinarySearchByTitle() throws Exception {
        String title = "The Value-Exploration of the Clinical Breast Diagnosis by Using Thermal Tomography.";
        //得到的location应该是189857193
        Article res = RecordSearcher.binarySearchByTitle(title);
        System.out.println(res);
        System.out.println("论文记录如下======================");
        if (res == null) {
            return;
        }
        for (String record : res.getRecords()) {
            System.out.println(record);
        }
    }

    @Test
    public void testBinarySearchByTitleEfficiency() throws Exception {
        String[] titles = new String[]{
                "The Value-Exploration of the Clinical Breast Diagnosis by Using Thermal Tomography.",
                "Adapting Robot Behavior for Human--Robot Interaction.",
                "An improved algorithm for basis pursuit problem and its applications.",
                "Mixture correntropy for robust learning.",
                "Musing in an Expert Database.",
                "MLSE-PSP receiver with optimized LMS step-size parameter.",
                "R alternative to the WEIBUL program.",
                "Representation of process synchronization.",
                "roomComputers-Bridging Spaces.",
                "S plane control based on parameters optimization with simulated annealing for underwater vehicle.",
                "Smart cities: Advances in research - An information systems perspective.",
                "System Dynamics and the Paging I/O Approach.",
                "The Hawkes process with renewal immigration &amp; its estimation with an EM algorithm.",
                "Through-Silicon Via Fault-Tolerant Clock Networks for 3-D ICs.",
                "Triple-loop networks with arbitrarily many minimum distance diagrams.",
                "TripleID-C: Low Cost Compressed Representation for RDF Query Processing in GPUs.",
                "Timing results of some internal sorting algorithms on the ETA 10-P.",
                "Timing results of some internal sorting algorithms on the IBM 3090.",
                "Timing results of some internal sorting algorithms on vector computers.",
                "Timing results of various compilers using an optimization quality benchmark.",
                "Timing scheme of floating-point based digital controller for DC-DC Converters.",
                "Timing sensitivity of decision feedback and partial response detectors in magnetic recording.",
                "Timing sensory integration.",
                "Timing side channels for traffic analysis.",
                "Timing simulation of digital circuits with binary decision diagrams.",
                "Timing simulation of interconnected AUTOSAR software-components.",
                "Towards a Model of Context Awareness Using Web Services.",
                "Towards a Model of Context-Aware Recommender System.",
                "Towards a Model of Cooperation.",
                "Towards a Model of DNS Client Behavior.",
                "Transmission of high speed data in cdma2000."
        };
        long startTime = System.currentTimeMillis();
        for (String title : titles) {
            Article res = RecordSearcher.binarySearchByTitle(title);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("共搜索" + titles.length + "篇文章，总计耗时" + duration
                + "mills，平均耗时" + ((double) duration / titles.length) + "mills");
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
    public void testBinarySearchByAuthor() throws Exception {

        Author author = RecordSearcher.binarySearchByAuthor("Toshihiro Osaragi", true);
        System.out.println(author);
        if (author == null) {
            return;
        }
        if (author.getCollaborators() != null) {
            System.out.println("=============================");
            System.out.println("合作者如下");
            Map<String, List<String>> collaborators = author.getCollaborators();
            for (String a : collaborators.keySet()) {
                System.out.println(a + ": " + collaborators.get(a));
            }
        }
        if (author.getRecords() != null) {
            System.out.println("=============================");
            System.out.println("记录如下");
            for (String a : author.getRecords()) {
                System.out.println(a);
            }
        }
    }

    @Test
    public void testBinarySearchByAuthorEfficiency() throws Exception {
        //Alfredo Cuzzocrea拥有论文423篇，耗时999ms

        //如果是遍历搜索，搜索第20万的记录至少需要1s
        //String authorName = "Anatole Khelif";
        String[] authorNames = new String[]{
                "Maryam Rasool",
                "Maryam Rastgar-Jazi",
                "Maryam Rastgarpour",
                "Maryam Ravan",
                "Maryam Rayegan",
                "Maryam Razavian",
                "Maryam Razeghian",
                "Maryam Razmhosseini",
                "Baokui Guo",
                "Baokui Li",
                "Baokui Wang",
                "Baokui Zhou",
                "Baokui Zhu",
                "Baokun Ding",
                "Baokun Han",
                "Baokun He",
                "Baokun Hu",
                "Baokun Li",
                "Baokun Pang",
                "Hiroyuki Iketani",
                "Hiroyuki Iki",
                "Hiroyuki Imai",
                "Hiroyuki Imaizumi",
                "Hiroyuki Imazu",
                "Hiroyuki Inahata",
                "Hiroyuki Inamura",
                "Moustapha Drame",
                "Hiroyuki Inatsuka",
                "Hiroyuki Ino",
                "Mousumi Debnath",
                "Mousumi Dhara",
                "Mousumi Dutt",
                "Mousumi Gupta",
                "Mousumi Haque",
                "Mousumi Kabir",
                "Mousumi Kumar",
                "Mousumi Kundu",
                "Hiroyuki Imose",
                "Hiroyuki Inaba",
                "Hiroyuki Inada",
        };
        long startTime = System.currentTimeMillis();
        for (String authorName : authorNames) {
            Author res = RecordSearcher.binarySearchByAuthor(authorName, false);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("共搜索" + authorNames.length + "位作者，总计耗时" + duration
                + "mills，平均耗时" + ((double) duration / authorNames.length) + "mills");

    }
}
