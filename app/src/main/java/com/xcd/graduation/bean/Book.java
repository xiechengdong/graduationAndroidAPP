package com.xcd.graduation.bean;

import java.io.Serializable;
import java.util.Arrays;

public class Book implements Serializable {

    private String id;
    private String name;
    private String cover;
    private String teacher_name;
    private String content;
    private String teacher_id;

    public Book() {
    }

    public Book(String id, String name, String cover, String teacher_name, String content, String teacher_id) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.teacher_name = teacher_name;
        this.content = content;
        this.teacher_id = teacher_id;
    }

    public Book(String id, String name, String cover, String teacher_name) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.teacher_name = teacher_name;
    }


    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                ", content='" + content + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                '}';
    }
}
