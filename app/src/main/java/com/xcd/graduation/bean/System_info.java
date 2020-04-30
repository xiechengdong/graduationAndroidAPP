package com.xcd.graduation.bean;


import java.io.Serializable;

/**
 * @author XieChengdong
 * @title
 * @2020/3/1 18:50
 */
public class System_info implements Serializable {

    private String id;

    private String phone_tel;

    private String gongao_time;

    private String gongao_info;

    private String system_about;

    public System_info() {
    }

    public System_info(String id, String phone_tel, String gongao_time, String gongao_info, String system_about) {
        this.id = id;
        this.phone_tel = phone_tel;
        this.gongao_time = gongao_time;
        this.gongao_info = gongao_info;
        this.system_about = system_about;
    }

    @Override
    public String toString() {
        return "System_info{" +
                "id='" + id + '\'' +
                ", phone_tel='" + phone_tel + '\'' +
                ", gongao_time='" + gongao_time + '\'' +
                ", gongao_info='" + gongao_info + '\'' +
                ", system_about='" + system_about + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone_tel() {
        return phone_tel;
    }

    public void setPhone_tel(String phone_tel) {
        this.phone_tel = phone_tel;
    }

    public String getGongao_time() {
        return gongao_time;
    }

    public void setGongao_time(String gongao_time) {
        this.gongao_time = gongao_time;
    }

    public String getGongao_info() {
        return gongao_info;
    }

    public void setGongao_info(String gongao_info) {
        this.gongao_info = gongao_info;
    }

    public String getSystem_about() {
        return system_about;
    }

    public void setSystem_about(String system_about) {
        this.system_about = system_about;
    }
}
