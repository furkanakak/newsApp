package com.example.newsapp;

public class model {

    private String title;
    private String link;
    private String pubDate;
    private String image;
    private String description;

    public model() {

    }

    public model(String title, String link, String pubDate, String image, String description) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.image = image;
        this.description = description;
    }

    public model(String title, String link, String pubDate, String image) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
