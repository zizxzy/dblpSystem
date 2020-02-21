package com.scut.controller;/**
 * @author yejh
 * @create 2020-02_19 14:39
 */

import com.scut.bean.InfoDTO;
import com.scut.service.ArticleService;
import com.scut.service.AuthorService;
import com.yejh.bean.Article;
import com.yejh.bean.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: TODO
 **/

@Controller
@RequestMapping(value = "/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @RequestMapping(value = "/toList")
    public String toAuthorList(){
        return "author_list";
    }

    @RequestMapping(value = "/toRankList")
    public String toTopAuthorList(){
        return "top_author_list";
    }

    @ResponseBody
    @RequestMapping("/json")
    public InfoDTO getAuthorsToJson(@RequestParam(value = "tag", defaultValue = "a") String tag,
                                     @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                     Model model) {

        List<Author> authors = authorService.getPageAuthors(tag, pageNumber, pageSize);
        //System.out.println(articles);
        return InfoDTO.success().addData("pageNum", pageNumber).addData("pageAuthors", authors);
    }

    @PostMapping()
    @ResponseBody
    public InfoDTO getAuthorByNamePost(String authorName){
        Author author = authorService.getAuthorByName(authorName,false);
        if (author != null) {
            return InfoDTO.success().addData("author", author);
        } else {
            return InfoDTO.fail();
        }
    }

    @GetMapping(value = "top")
    @ResponseBody
    public InfoDTO getTopAuthors(){
        List<Author> topAuthors = authorService.getTopAuthors();
        if(topAuthors != null){
            return InfoDTO.success().addData("topAuthors", topAuthors);
        }else {
            return InfoDTO.fail();
        }
    }
}
