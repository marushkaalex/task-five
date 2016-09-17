package com.epam.am.whatacat.model;

import java.util.Date;

public class PostRating extends BaseModel {
    private long postId;
    private long userId;
    private int ratingDelta;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRatingDelta() {
        return ratingDelta;
    }

    public void setRatingDelta(int ratingDelta) {
        this.ratingDelta = ratingDelta;
    }

    @Override
    public String toString() {
        return "PostRating{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", ratingDelta=" + ratingDelta +
                '}';
    }
}
