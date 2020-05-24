package com.scut.bean;/**
 * @author yejh
 * @create 2020-05_24 14:05
 */

import java.util.Objects;

/**
 * CollaborativeRelation类用于封装了合作关系
 **/
public class CollaborativeRelation {
    private String authorName1;

    private String authorName2;

    private String articleName;

    public CollaborativeRelation() {
    }

    public CollaborativeRelation(String authorName1, String authorName2, String articleName) {
        this.authorName1 = authorName1;
        this.authorName2 = authorName2;
        this.articleName = articleName;
    }

    public String getAuthorName1() {
        return authorName1;
    }

    public void setAuthorName1(String authorName1) {
        this.authorName1 = authorName1;
    }

    public String getAuthorName2() {
        return authorName2;
    }

    public void setAuthorName2(String authorName2) {
        this.authorName2 = authorName2;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollaborativeRelation that = (CollaborativeRelation) o;
        return Objects.equals(authorName1, that.authorName1) &&
                Objects.equals(authorName2, that.authorName2) &&
                Objects.equals(articleName, that.articleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName1, authorName2, articleName);
    }
}
