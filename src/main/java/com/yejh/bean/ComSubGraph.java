package com.yejh.bean;/*
 * Created by lizeyu on 2020/3/29 16:08
 * 存储对应阶数的完全子图的个数以及完全子图详细信息的类
 */

import java.util.ArrayList;

public class ComSubGraph {
    private int num;
    private ArrayList<ArrayList<String>> comSubGraphAuthors;

    @Override
    public String toString() {
        return "ComSubGraph{" +
                "num=" + num +
                ", comSubGraphAuthors=" + comSubGraphAuthors +
                '}';
    }

    public ComSubGraph() {
    }

    public ComSubGraph(int num, ArrayList<ArrayList<String>> comSubGraphAuthors) {
        this.num = num;
        this.comSubGraphAuthors = comSubGraphAuthors;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<ArrayList<String>> getComSubGraphAuthors() {
        return comSubGraphAuthors;
    }

    public void setComSubGraphAuthors(ArrayList<ArrayList<String>> comSubGraphAuthors) {
        this.comSubGraphAuthors = comSubGraphAuthors;
    }
}
