package com.xcd.graduation.bean;

import java.io.Serializable;

public class Message_details implements Serializable {

    private String id;
    private String parent_id;
    private String username;
    private String content;
    private String create_time;

    public Message_details() {
    }

    public Message_details(String id, String parent_id, String username, String content) {
        this.id = id;
        this.parent_id = parent_id;
        this.username = username;
        this.content = content;
    }

    public Message_details(String id, String parent_id, String username, String content, String create_time) {
        this.id = id;
        this.parent_id = parent_id;
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

    public String getparent_id() {
        return parent_id;
    }

    public void setparent_id(String parent_id) {
        this.parent_id = parent_id;
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
                ", parent_id='" + parent_id + '\'' +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
