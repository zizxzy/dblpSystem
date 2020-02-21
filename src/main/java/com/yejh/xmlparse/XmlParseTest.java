package com.yejh.xmlparse;/**
 * @author yejh
 * @create 2020-02_17 11:40
 */

import com.yejh.indexinit.IndexInitializer;
import com.yejh.utils.TxtUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * @description: TODO
 **/
public class XmlParseTest {

    private static String xmlFileLocation;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            xmlFileLocation = (String) properties.get("xml_file_location");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        XmlParseRunnable xmlParseRunnable = new XmlParseRunnable();
        xmlParseRunnable.setIndexInitializer(new IndexInitializer());

        xmlParseRunnable.run();
    }

    @Test
    public void test() throws Exception {
        long pos = 2532214702L;
        RandomAccessFile accessFile = new RandomAccessFile(new File(xmlFileLocation), "r");

        byte[] bytes = new byte[1024];
        accessFile.seek(pos);
        accessFile.read(bytes);
        System.out.println(new String(bytes));
    }
}
