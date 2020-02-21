package com.yejh.bean;/**
 * @author yejh
 * @create 2020-02_07 13:23
 */

import com.yejh.utils.TxtUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: TODO
 **/
public class Author {
    private String name;

    private List<Long> locations;

    private List<String> articles;

    private Set<String> collaborators;

    public Author() {
    }

    public Author(String name, List<Long> locations) {
        this.name = name;
        this.locations = locations;
    }

    public static Author initAuthor(String line){
        String[] split = line.split(", ");
        String authorName = TxtUtil.pairTrim(split[0], '\"').trim();
        List<Long> longs = new ArrayList<>();
        for(int i = 1; i < split.length; ++i){
            longs.add(Long.valueOf(split[i]));
        }
        return new Author(authorName, longs);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArticleNumber(){
        return locations.size();
    }

    public List<Long> getLocations() {
        return locations;
    }

    public void setLocations(List<Long> locations) {
        this.locations = locations;
    }

    public List<String> getArticles() {
        return articles;
    }

    public void setArticles(List<String> articles) {
        this.articles = articles;
    }

    public Set<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Set<String> collaborators) {
        this.collaborators = collaborators;
    }

    public void addCollaborators(Set<String> collaborators){
        if(collaborators == null){
            return;
        }
        if(this.collaborators == null){
            this.collaborators = new HashSet<>(collaborators.size());
        }
        this.collaborators.addAll(collaborators);
        //System.out.println("debug: " + collaborators);
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", locations=" + locations +
                '}';
    }
}
