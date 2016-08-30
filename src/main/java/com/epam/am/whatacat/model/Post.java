package com.epam.am.whatacat.model;

import java.util.Date;
import java.util.List;

public class Post extends BaseModel {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_PHOTO = 2;
    public static final int TYPE_VIDEO = 3;

    private User author;
    private int type;
    private String content;
    private Date publicationDate;
    private long rating;
    private List<Comment> commentList;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}