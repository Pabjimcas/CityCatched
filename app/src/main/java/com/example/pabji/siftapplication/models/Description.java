package com.example.pabji.siftapplication.models;

/**
 * Created by pabji on 30/05/2016.
 */
public class Description {
    String title;
    String content;

    public Description(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
