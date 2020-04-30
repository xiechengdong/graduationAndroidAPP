package com.xcd.graduation.bean;

import java.io.Serializable;

public class Teacher implements Serializable {

    private String id;
    private String name;
    private long number;
    private String department;
    private String password;
    private String phone;
    private String headimage;

    public Teacher(String id, String name, long number, String department, String password, String phone, String headimage) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.department = department;
        this.password = password;
        this.phone = phone;
        this.headimage = headimage;
    }

    public Teacher() {
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public Teacher(String id, String name, long number, String department, String password, String phone) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.department = department;
        this.password = password;
        this.phone = phone;
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

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", department='" + department + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", headimage='" + headimage + '\'' +
                '}';
    }
}
