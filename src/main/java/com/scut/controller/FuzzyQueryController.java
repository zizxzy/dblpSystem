package com.scut.controller;
/*
 * Created by lizeyu on 2020/3/28 12:23
 */

import com.scut.bean.InfoDTO;
import com.scut.service.ArticleService;
import com.yejh.bean.Article;
import com.yejh.funzzyquery.ArticleList_totalRecords;
/*import com.yejh.querySearch.TestLuence;*/
import com.yejh.titleInit.titleInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据url返回每年模糊查询的结果
 **/

@Controller
@RequestMapping(value = "/fuzzyQuery")
public class FuzzyQueryController {
    @Autowired
    private ArticleService articleService;

/*    @Autowired
    private TestLuence testLuence;*/

    @RequestMapping(value = "/toQueryPage")
    public String toQueryPage() {
        return "fuzzy_query";
    }

/*    @RequestMapping(value = "/json", method = RequestMethod.POST)
    @ResponseBody
    public InfoDTO getQueryArticleToJson(@RequestBody Map<String, Object> parms) throws Exception {
        ArrayList<String> arrayList = (ArrayList<String>) parms.get("queryArray");
        String[] strs = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            strs[i] = arrayList.get(i);
        }
        ArticleList_totalRecords articleList_totalRecords = testLuence.testSearch(strs, (Integer) parms.get("pageNumber"), (Integer) parms.get("pageSize"));
        int totalRecords = articleList_totalRecords.getTotalRecords();
        List<Article> articles = articleList_totalRecords.getArticleList();
        return InfoDTO.success().addData("pageNum", parms.get("pageNumber")).addData("pageArticles", articles).addData("totalRecords", totalRecords);
    }*/

    @RequestMapping(value = "/json", method = RequestMethod.POST)
    @ResponseBody
    public InfoDTO getQueryArticleToJson(@RequestBody Map<String, Object> parms) throws Exception {
        ArrayList<String> arrayList = (ArrayList<String>) parms.get("queryArray");
        String[] strs = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            strs[i] = arrayList.get(i);
        }
        ArticleList_totalRecords articleList_totalRecords = articleService.getPageArticlesByFuzzyQuery((Integer) parms.get("pageNumber"), (Integer) parms.get("pageSize"), strs, (Boolean) parms.get("tag"));
        int totalRecords = articleList_totalRecords.getTotalRecords();
        List<Article> articles = articleList_totalRecords.getArticleList();
        return InfoDTO.success().addData("pageNum", parms.get("pageNumber")).addData("pageArticles", articles).addData("totalRecords", totalRecords);
    }
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ResponseBody
    public InfoDTO initTitle(@RequestBody Map<String, Object> map) throws Exception {
        titleInitializer titleInitializer1 = new titleInitializer();
        if (titleInitializer1.titleInit()) {
            return InfoDTO.success().addData("msg", new String("success"));
        } else {
            return InfoDTO.fail().addData("msg", new String("fail"));
        }
    }

    @GetMapping("/{articleName}")
    @ResponseBody
    public InfoDTO getArticleByName(@PathVariable("articleName") String articleName) {
        Article article = articleService.getArticleByName(articleName);
        if (article != null) {
            return InfoDTO.success().addData("article", article);
        } else {
            return InfoDTO.fail();
        }
    }

    @PostMapping("/{articleName}")
    @ResponseBody
    public InfoDTO getArticleByNamePost(String articleName) {
        Article article = articleService.getArticleByName(articleName);
        if (article != null) {
            return InfoDTO.success().addData("article", article);
        } else {
            return InfoDTO.fail();
        }
    }
}
