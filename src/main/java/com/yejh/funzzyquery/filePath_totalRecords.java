package com.yejh.funzzyquery;
import com.yejh.funzzyquery.testA;
import com.yejh.bean.Article;
import com.yejh.titleInit.titleInitializer;
import org.junit.jupiter.api.Test;

import java.util.List;

public class test {
    @Test
    public void te()
    {
        List<Article> stringListHashMap = titleInitializer.stringListHashMap;
        System.out.println(stringListHashMap.size());
        System.out.println(testA.list.size());
    }
}
