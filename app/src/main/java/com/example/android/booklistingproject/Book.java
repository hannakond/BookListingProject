package com.example.android.booklistingproject;

public class Book {
    private String author;
    private String title;
    private String link;
    public Book(String author, String title, String link) {
        this.author = author;
        this.title = title;
        this.link = link;
    }
    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public String getLink() {
        return link;
    }
}
