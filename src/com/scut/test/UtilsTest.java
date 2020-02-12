package com.scut.test;

/**
 * @author yejh
 * @create 2020-02_12 21:12
 */

import com.scut.utils.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static com.scut.utils.Util.YearWord;

public class UtilsTest {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src\\com\\scut\\global.properties"));
        String fileName = properties.getProperty("data_location");
        Util.xmlParse(fileName);
        Util.YearWord();
    }
}
