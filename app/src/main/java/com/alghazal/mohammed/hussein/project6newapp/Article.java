package com.alghazal.mohammed.hussein.project6newapp;


public class Article  {

    private String sectionsName;
    private String Url;
    private String pubDate;
    private String webTitle;
    private String author;

    public Article(String webTitle, String sectionsName, String url, String pubDate, String author) {
        this.sectionsName = sectionsName;
        Url = url;
        this.pubDate = pubDate;
        this.webTitle=webTitle;
        this.author=author;
    }

    public String getSectionsName() {
        return sectionsName;
    }

    public void setSectionsName(String sectionsName) {
        this.sectionsName = sectionsName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
