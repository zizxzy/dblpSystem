package com.yejh.xmlparse;/**
 * @author yejh
 * @create 2020-02_17 11:40
 */

import com.yejh.indexinit.IndexInitializer;
import com.yejh.utils.TxtUtil;
import com.yejh.xmlparse.impl.IndexXmlParser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Properties;

/**
 * @description: TODO
 **/
public class XmlParserTest {

    private static String xmlFileLocation;

    static {
        try {
            Properties properties = TxtUtil.getProperties();
            xmlFileLocation = (String) properties.get("xml_file_location");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IndexXmlParser indexXmlParser = new IndexXmlParser();
        indexXmlParser.setIndexInitializer(new IndexInitializer());

        indexXmlParser.run();
    }

    //[debug]cur: 77630678, i=540, j=3557


    @Test
    public void test() throws Exception {
        //文件大小：2785899306
        //2785899298L 的位置刚好对应</dblp>
        //long pos = 2114983195L;
        //long pos = 2114983859L - 600;

        long pos = 	2463614749L;
        RandomAccessFile accessFile = new RandomAccessFile(new File(xmlFileLocation), "r");

        byte[] bytes = new byte[4096];
        accessFile.seek(pos);
        accessFile.read(bytes);
        System.out.println(new String(bytes));
    }
}
