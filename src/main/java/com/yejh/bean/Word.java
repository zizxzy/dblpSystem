package com.yejh.bean;
 /* 存储热词的类
 * @author yejh
 * @create 2020-02_17 20:45
 */

import com.yejh.utils.TxtUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @description: TODO
 **/
public class Word {
    private String value;
    private String count;

    public Word(String value, String count) {
        this.value = value;
        this.count = count;
    }

    public Word() {

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Word{" +
                "value='" + value + '\'' +
                ", count=" + count +
                '}';
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    /**
     * 解析每行记录的方法
     * @param line 每行记录
     * @return 返回一个 Word类
     */
    public static Word initHotWords(String line){
        String [] split = line.split(":");
        String key = split[0].trim();
        String value = split[1].trim();
        Word word = new Word();
        word.setValue(key);
        word.setCount(value);
        return word;
    }
}
