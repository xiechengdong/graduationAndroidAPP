package com.xcd.graduation.bean;

import java.io.Serializable;

public class Guanggao implements Serializable {

    private String id;
    private String name;
    private String start;
    private String over;
    private String content;

    public Guanggao(String id, String name, String start, String over, String content) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.over = over;
        this.content = content;
    }

    public Guanggao() {
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Guanggao{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", start='" + start + '\'' +
                ", over='" + over + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
