package com.scut.controller;/**
 * @author yejh
 * @create 2020-02_19 14:39
 */

import com.scut.bean.CollaborativeRelation;
import com.scut.bean.InfoDTO;
import com.scut.service.ArticleService;
import com.scut.service.AuthorService;
import com.yejh.bean.Article;
import com.yejh.bean.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AuthorController用于接收与Author有关的请求
 **/

@Controller
@RequestMapping(value = "/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    /**
     * 跳转到author_list.jsp页面
     */
    @RequestMapping(value = "/toList")
    public String toAuthorList() {
        return "author_list";
    }

    /**
     * 跳转到top_author_list.jsp页面
     */
    @RequestMapping(value = "/toRankList")
    public String toTopAuthorList() {
        return "top_author_list";
    }

    /**
     * 跳转到author_relation.jsp页面
     */
    @RequestMapping(value = "/toRelation")
    public String toAuthorRelation() {
        return "author_relation";
    }

    /**
     * 从 author\\#{tag}.csv 中载入pageSize条作者记录
     *
     * @param tag        文件名标识
     * @param pageNumber 页号
     * @param pageSize   页大小
     */
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

    /**
     * 根据作者名搜索记录
     */
    @ResponseBody
    @PostMapping()
    public InfoDTO getAuthorByNamePost(String authorName) {
        Author author = authorService.getAuthorByName(authorName.trim(), false);
        if (author != null) {
            return InfoDTO.success().addData("author", author);
        } else {
            return InfoDTO.fail();
        }
    }

    /**
     * 根据作者名集合获取合作者（与任一作者有过合作）
     */
    @ResponseBody
    @PostMapping("/collaboration")
    public InfoDTO getCollaboratorsByAuthors(@RequestBody List<String> authorsName) {
        System.out.println("getCollaboratorsByAuthors: " + authorsName);
        List<CollaborativeRelation> collaborativeRelations = new ArrayList<>();
        for (String authorName : authorsName) {
            authorName = authorName.trim();
            Author author = authorService.getAuthorByName(authorName, false);
            if (author != null) {
                Map<String, List<String>> collaborators = author.getCollaborators();
                for (String articleName : collaborators.keySet()) {
                    List<String> cols = collaborators.get(articleName);
                    for (String col : cols){
                        if(!authorName.equals(col)){
                            collaborativeRelations.add(new CollaborativeRelation(authorName, col, articleName));
                        }
                    }
                }
            }
        }
        if (!collaborativeRelations.isEmpty()) {
            return InfoDTO.success().addData("collaborativeRelations", collaborativeRelations);
        } else {
            return InfoDTO.fail();
        }
    }

    /**
     * 获取发表数前100的作者
     */
    @GetMapping(value = "top")
    @ResponseBody
    public InfoDTO getTopAuthors() {
        List<Author> topAuthors = authorService.getTopAuthors();
        if (topAuthors != null) {
            return InfoDTO.success().addData("topAuthors", topAuthors);
        } else {
            return InfoDTO.fail();
        }
    }

}
