package com.scut.controller;/**
 * @author yejh
 * @create 2020-02_07 13:46
 */

import com.github.pagehelper.PageInfo;
import com.scut.bean.InfoDTO;
import com.scut.service.ArticleService;
import com.yejh.bean.Article;
import com.yejh.bean.Author;
import com.yejh.search.RecordSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: TODO
 **/

@Controller
@RequestMapping(value = "/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/toList")
    public String toArticleList(){
        return "article_list";
    }

    @ResponseBody
    @RequestMapping("/json")
    public InfoDTO getArticlesToJson(@RequestParam(value = "tag", defaultValue = "a") String tag,
                                 @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 Model model) {

        List<Article> articles = articleService.getPageArticles(tag, pageNumber, pageSize);
        //System.out.println(articles);
        return InfoDTO.success().addData("pageNum", pageNumber).addData("pageArticles", articles);
    }

    @GetMapping("/{authorName}")
    @ResponseBody
    public InfoDTO getArticleByName(@PathVariable("authorName") String authorName) {
        Article article = articleService.getArticleByName(authorName);
        if (article != null) {
            return InfoDTO.success().addData("article", article);
        } else {
            return InfoDTO.fail();
        }
    }

    @PostMapping()
    @ResponseBody
    public InfoDTO getArticleByNamePost(String articleName){
        Article article = articleService.getArticleByName(articleName);
        if (article != null) {
            return InfoDTO.success().addData("article", article);
        } else {
            return InfoDTO.fail();
        }
    }
}
