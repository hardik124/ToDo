package com.todo.todo.models;

/**
 * Created by f390 on 18/4/17.
 */

public class userDetailsModel {

    private String imageUrl, name, desc;

    public userDetailsModel() {
    }

    public userDetailsModel(String imageUrl, String name, String desc) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.desc = desc;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
