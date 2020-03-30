package com.scut.controller;
 /*
 * Created by lizeyu on 2020/3/29 0:11
 * 根据url返回每年热词结果的controller类
 */


import com.github.pagehelper.PageInfo;
import com.scut.bean.InfoDTO;
import com.scut.service.HotWordsService;

import com.yejh.bean.Word;
import com.yejh.bean.Author;
import com.yejh.funzzyquery.ArticleList_totalRecords;
import com.yejh.search.RecordSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

/**
 * @description: TODO
 **/

@Controller
@RequestMapping(value = "/hotWords")
public class AnnualHotWordTop10Controller {

    private HotWordsService hotWordsService = new HotWordsService();

    @RequestMapping(value = "/toAnnulHotWords")
    public String toAnnulHotWords() {
        return "hot_words";
    }

    /**
     * 返回每年热词的hashMap
     * @return 一个hashMap,键是年份，值是该年份的热词列表
     */
    @GetMapping(value = "json")
    @ResponseBody
    public InfoDTO getQueryArticleToJson() {
        HashMap<String, ArrayList<Word>> hotWords = hotWordsService.getHotWords();
        for (Map.Entry<String, ArrayList<Word>> entry : hotWords.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        if (hotWords != null) {
            return InfoDTO.success().addData("hotWords", hotWords);
        } else {
            return InfoDTO.fail();
        }
    }

}
