package com.xcd.graduation.bean;

import java.io.Serializable;

public class Message implements Serializable {

    private String id;
    private String book_id;
    private String username;
    private String content;
    private String create_time;

    public Message() {
    }

    public Message(String id, String book_id, String username, String content) {
        this.id = id;
        this.book_id = book_id;
        this.username = username;
        this.content = content;
    }

    public Message(String id, String book_id, String username, String content, String create_time) {
        this.id = id;
        this.book_id = book_id;
        this.username = username;
        this.content = content;
        this.create_time = create_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", book_id='" + book_id + '\'' +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
