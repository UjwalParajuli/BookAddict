package com.example.bookaddict.models;

import java.io.Serializable;

public class BookModel implements Serializable {
    private int book_id, pages;
    private String book_name, author, genre, published_date, file_path, cover_image;

    public BookModel(int book_id, int pages, String book_name, String author, String genre, String published_date, String file_path, String cover_image) {
        this.book_id = book_id;
        this.pages = pages;
        this.book_name = book_name;
        this.author = author;
        this.genre = genre;
        this.published_date = published_date;
        this.file_path = file_path;
        this.cover_image = cover_image;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }
}
