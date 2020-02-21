package com.yejh.bean;/**
 * @author yejh
 * @create 2020-02_07 13:24
 */

import com.yejh.utils.TxtUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO
 **/
public class Article {
    private String title;

    private List<Long> locations;

    private List<String> records;

    public Article() {
    }

    public Article(String title, List<Long> locations) {
        this.title = title;
        this.locations = locations;
    }

    //通过一行记录初始化article
    //例如读取"2D map-building and localization in outdoor environments.", 376645359
    //返回new Article(title, location)
    public static Article initArticle(String line){
        int gap = TxtUtil.findLast(line, '\"');
        String title = TxtUtil.pairTrim(line.substring(1, gap), '\"').trim();
        String rest = line.substring(gap+3);
        String[] split = rest.split(", ");
        ArrayList<Long> longs = new ArrayList<>();
        for(int i = 0; i < split.length; ++i){
            longs.add(Long.valueOf(split[i]));
        }
        return new Article(title, longs);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getLocations() {
        return locations;
    }

    public List<String> getRecords() {
        return records;
    }

    public void addRecord(String record){
        if(records == null){
            this.records = new ArrayList<>();
        }
        this.records.add(record);
    }

    public void setRecords(List<String> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", locations=" + locations +
                '}';
    }
}
