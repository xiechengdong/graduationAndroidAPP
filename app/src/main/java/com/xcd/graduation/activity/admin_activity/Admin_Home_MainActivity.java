package com.xcd.graduation.activity.admin_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xcd.graduation.R;
import com.xcd.graduation.activity.user_activity.Login_Activity;

public class Admin_Home_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home__main);


    }

    public void clik(View view) {

        switch (view.getId()){
            //课程、学生、教师管理
            case R.id.Admin_Home_To_User:
                Intent intent = new Intent(this,Admin_book_MainActivity.class);
                startActivity(intent);
                finish();
                break;
            //广告管理
            case R.id.Admin_Home_To_Advertisement:
                Intent intent2 = new Intent(this,Admin_guangao_MainActivity.class);
                startActivity(intent2);
                finish();
                break;
            //退出登录
            case R.id.Admin_Home_To_exit:
                Intent intent1 = new Intent(this, Login_Activity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
