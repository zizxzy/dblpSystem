package comsubgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * 统计各阶完全子图数量：
 * 1.构建基于链表的无向图邻接表
 * 2.while循环：（依次判断剩余度不为0的节点）
 * 找出包含此节点所有不重复的最大完全子图，其中度为0的节点统计为一阶完全子图的数量
 * 3.删除重复判断的完全子图，统计完成，释放资源
 */
public class ComSubGraphCount {
    private String XMLPath;
    private ArrayList<ArrayList<Integer>> myMap;
    private ArrayList<Integer> subMax = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> subList = new ArrayList<ArrayList<Integer>>();
    private int[] result = new int[300];
    private LinkedHashMap<Integer, Integer> returnResult = new LinkedHashMap<Integer,Integer>();

    ComSubGraphCount(String XMLPath) {
        this.XMLPath = XMLPath;
    }

    public LinkedHashMap<Integer, Integer> countComSubGraph() {
        //构建并获取整个合作关系图
        MyMap map = new MyMap(XMLPath);
        myMap = map.getMyMap();
//        authorNameMap = map.getAuthorNameMap();
        int p = -1;
        while (++p < myMap.size()) {
            //统计初始度为0的节点数目，即一阶子图数目
            if (myMap.get(p).size() == 0) {
                result[0]++;
            } else if (myMap.get(p).size() >= 1) {
                //找出包含此节点的所有完全子图，并纳入统计
                countSubMax(p);
                //测试
                if (p % 1000 == 0) {
                    System.out.println("执行到：  " + p);
                }
            }
        }
        //因为n阶子图被重读统计n次，所以需除以n
        for (int j = 0; j < 300; j++) {
            result[j] = result[j] / ((j + 1));
            if (result[j] > 0) {
                returnResult.put(j+1,result[j]);
            }
        }

        //释放内存
        myMap.clear();
        subMax.clear();
        subList.clear();
        System.gc();

        //返回结果
        return returnResult;
    }

    //计算包含p节点的所有完全子图
    private void countSubMax(int p) {
        boolean mark = true;
        int size = myMap.get(p).size();
        int temp;
        subList.clear();
        //从不同邻居顶点开始遍历
        for (int k = 0; k < size; k++) {
            subMax = new ArrayList<Integer>();
            //从某一邻居节点开始遍历
            for (int i = 0; i < size; i++) {
                temp = (i + k) % size;
                mark = true;
                //判断这个邻居顶点是否与先前的完全子图构成更大的完全子图，是则加入该完全子图
                for (int j = 0; j < subMax.size(); j++) {
                    if (!myMap.get(myMap.get(p).get(temp)).contains(subMax.get(j))) {
                        mark = false;
                        break;
                    }
                }
                if (mark == true) {
                    subMax.add(myMap.get(p).get(temp));
                }
            }
            subMax.add(p);
            //给子图节点排序，便于判断重复子图
            Collections.sort(subMax);
            //已经存在的重复的完全子图则不纳入统计
            if (!subList.contains(subMax)) {
                subList.add(subMax);
                result[subMax.size() - 1]++;
            }
        }
    }

}