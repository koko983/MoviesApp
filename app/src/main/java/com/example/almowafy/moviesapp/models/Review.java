package com.example.almowafy.moviesapp.models;

/**
 * Created by AlMowafy on 9/3/2016.
 */
public class Review {
    private String reviewer;
    private String content;

    public Review(String reviewer, String content) {
        this.reviewer = reviewer;
        this.content = content;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
