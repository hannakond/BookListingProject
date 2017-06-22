package com.example.android.booklistingproject;

public class Book {

    private String mAuthor;

    private String mTitle;

    private String mLink;

    public Book(String author, String title, String link) {
        mAuthor = author;
        mTitle = title;
        mLink = link;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }
}
