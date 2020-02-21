package com.yejh.loader;/**
 * @author yejh
 * @create 2020-02_17 20:16
 */

import com.yejh.bean.Author;
import com.yejh.indexinit.IndexInitializer;
import com.yejh.utils.TxtUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @description: TODO
 **/
public class RecordLoader  {
    private static String authorIndexFileLocation;

    private static String[] fileNames;
    private static String[] tags;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src//main//resources//config//global.properties"));
            authorIndexFileLocation = (String) properties.get("index_file_root") + "//author";
            fileNames = new String[27];
            tags = new String[27];
            for (int i = 0; i < fileNames.length; ++i) {
                tags[i] = String.valueOf((char) (i + 'a'));
                fileNames[i] = authorIndexFileLocation + "//" + String.valueOf((char) (i + 'a') + ".csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<Author> loadAuthor() throws IOException{
        Set<Author> authors = new HashSet<>();
        for (String tag : tags){
            loadSingleFile(authors, tag);
        }
        return authors;
    }

    private static void loadSingleFile(Set<Author> authors, String tag) throws IOException{
        String fileName = authorIndexFileLocation + "//" + tag + ".csv";
        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));
        String line = null;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            Author author = Author.initAuthor(line);
            authors.add(author);
        }

    }
}
