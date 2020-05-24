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
 * RecordLoader类用于加载 dblp_index//author 中的索引文件
 **/
public class RecordLoader  {
    private static String authorIndexFileLocation;

    private static String[] fileNames;
    private static String[] tags;

    static {
        try {
            Properties properties = TxtUtil.getProperties();
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

    /**
     * 加载所有位于 dblp_index\\author 文件夹下的文件，把每一行记录封装为一个Author对象
     * @return 所有的author对象的Set集合
     */
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
