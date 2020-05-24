package com.scut.controller;/**
 * @author yejh
 * @create 2020-02_07 13:46
 */

import com.scut.bean.InfoDTO;
import com.scut.service.ArticleService;
import com.yejh.bean.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ArticleController用于接收与Article有关的请求
 **/

@Controller
@RequestMapping(value = "/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 跳转到article_list.jsp页面
     */
    @RequestMapping(value = "/toList")
    public String toArticleList() {
        return "article_list";
    }

    /**
     * 从 title\\#{tag}.csv 中载入pageSize条文章记录
     * @param tag 文件名标识
     * @param pageNumber 页号
     * @param pageSize 页大小
     */
    @ResponseBody
    @GetMapping("/json")
    public InfoDTO getArticlesToJson(@RequestParam(value = "tag", defaultValue = "a") String tag,
                                     @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                     Model model) {

        List<Article> articles = articleService.getPageArticles(tag, pageNumber, pageSize);
        //System.out.println(articles);
        return InfoDTO.success().addData("pageNum", pageNumber).addData("pageArticles", articles);
    }


    /**
     * 根据文章名搜索记录
     */
    @ResponseBody
    @GetMapping("/{articleName}")
    public InfoDTO getArticleByName(@PathVariable("articleName") String articleName) {
        Article article = articleService.getArticleByName(articleName);
        if (article != null) {
            return InfoDTO.success().addData("article", article);
        } else {
            return InfoDTO.fail();
        }
    }

    /**
     * 根据文章名搜索记录（接收POST请求）
     */
    @ResponseBody
    @PostMapping()
    public InfoDTO getArticleByNamePost(String articleName) {
        Article article = articleService.getArticleByName(articleName);
        if (article != null) {
            return InfoDTO.success().addData("article", article);
        } else {
            return InfoDTO.fail();
        }
    }
}
