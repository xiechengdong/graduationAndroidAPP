package com.xcd.graduation.activity.user_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.xcd.graduation.R;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_Home;
import com.xcd.graduation.config.ConfigCenter;

import java.util.regex.Pattern;

public class Welcom_Activity extends AppCompatActivity {

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom_);

        SharedPreferences sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);



//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("IsLogin",true);
//        editor.commit();


        Boolean b =  sharedPreferences.getBoolean("IsLogin",false);

        if(!b){
            //第一次登录跳转到登录页面
            intent.setClass(this, Login_Activity.class);
        }else if(Pattern.matches(ConfigCenter.regex1,sharedPreferences.getString("number",null)) ){
            //非第一登录直接跳转到学生主界面
            intent.setClass(this, Teacher_MainActivity_Home.class);
        }else {
            //非第一登录直接跳转到教师主界面
            intent.setClass(this, Teacher_MainActivity_Home.class);
        }
        stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                close();
            }
        }).start();
    }

    public void stop(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close(){
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.finish();
    }
}
