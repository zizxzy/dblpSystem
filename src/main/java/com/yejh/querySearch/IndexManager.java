/*
package com.yejh.querySearch;
*/
/*
 * Created by lizeyu on 2020/5/26 9:17
 *//*



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import com.yejh.titleInit.titleInitializer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
*/
/*
 *索引库的维护
 *//*


public class IndexManager {
    */
/*
     *
     *  创建索引文件，注意：索引目录最好不要设置成C盘的目录
     * @throws Exception
     *//*


    @Test
    public void createIndex() throws Exception {
        //1.采集数据
        titleInitializer titleInitializers = new titleInitializer();
        titleInitializers.titleInit();

        //文档集合
        List<Document> documentList = new ArrayList<>();

        //2.创建文档
        for (String article :
                titleInitializer.stringListHashMap) {
            Document document = new Document();
            //2.1创建域对象，加入文档对象
      */
/*      document.add(new TextField("locations", article.getLocations().toString(), Field.Store.YES));*//*


            if (article != null) {
                document.add(new TextField("title", article, Field.Store.YES));
                documentList.add(document);
            }
        }
        //3.创建分词器，标准分词器对英文分词非常好，对中文分词是一个字就认为是一个词
        Analyzer analyzer = new StandardAnalyzer();
        //4.创建目录对象，目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get("F://indexDir"));
        //5.创建索引输出流配置对象（输出流初始化对象），指定切分词使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //6.创建索引输出流对象，指定输出的位置和配置的初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //7.写入文档到索引库
        for (Document document : documentList) {
            indexWriter.addDocument(document);
        }
        //8释放资源
        indexWriter.close();
    }
}
*/
