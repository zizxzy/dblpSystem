package com.yejh.utils;/**
 * @author yejh
 * @create 2020-02_17 15:01
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO
 **/
public class TxtUtil {
    private static List<Byte> delimiters;

    static {
        delimiters = new ArrayList<>(2);
        delimiters.add((byte) '>');
        delimiters.add((byte) ' ');
    }

    public static String pairTrim(String str, char delimiter) {
        int len = str.length();
        int st = 0;
        char[] val = str.toCharArray();    /* avoid getfield opcode */

        while ((st < len) && (val[st] == delimiter)) {
            st++;
        }
        while ((st < len) && (val[len - 1] == delimiter)) {
            len--;
        }
        int del = Math.min(st, str.length() - len);
        return (del > 0) ? str.substring(del, str.length() - del) : str;
    }

    //得到标签里的内容
    public static String getContent(byte[] bytes, int pos){
        int j = 0;
        int len = bytes.length;
        //System.out.println(new String(bytes));
        while (pos + j < len  && !delimiters.contains(bytes[pos + j++]));
        if(pos + j ==len){
            return null;
        }
        return new String(bytes, pos, j-1);
    }

    public static String getTag(String str) {
        String tag = str.substring(0, 1).toLowerCase();
        if (!tag.matches("[a-z]")) {
            tag = "{";
        }
        return tag;
    }

    public static int get_tag_i(String tag) {
        int tag_i = getTag(tag).charAt(0) - 'a';
        if (tag_i < 0 || tag_i > 25) {
            tag_i = 26;
        }
        return tag_i;
    }

    public static int findLast(String line, char target) {
        int res = -1;
        char[] chars = line.toCharArray();
        for (int i = chars.length - 1; i >= 0; --i) {
            if (chars[i] == target) {
                res = i;
                break;
            }
        }
        return res;
    }


    public static String getNameInRecord(RandomAccessFile randomAccessFile, long pos) throws IOException {
        byte[] bytes = new byte[2048];
        randomAccessFile.seek(pos);
        randomAccessFile.read(bytes);
        if (bytes[0] != '\"') {
            throw new RuntimeException("参数pos(" + pos + ")有误，不是记录的开始位置");
        }
        //提取作者名或者标题
        int end = 0;
        while (end < bytes.length - 1 && bytes[++end] != '\n') ;
        if (end == bytes.length) {
            throw new RuntimeException(randomAccessFile + "文件作者名称或者标题过长：pos=" + pos);
        }
        while (bytes[--end] != '\"') ;
        return new String(bytes, 1, end - 1);
    }
}
