package com.xcd.graduation.activity.user_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcd.graduation.R;
import com.xcd.graduation.activity.admin_activity.Admin_Home_MainActivity;
import com.xcd.graduation.activity.admin_activity.Admin_book_MainActivity;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.JsonUtis;

import org.json.JSONException;
import org.json.JSONObject;

public class Admin_Login_MainActivity extends AppCompatActivity {

    EditText adminName;
    EditText adminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login__main);

        adminName = findViewById(R.id.admin_login_name);
        adminPassword = findViewById(R.id.admin_login_password);
    }

    public void click(View view) {

        switch (view.getId()){
            case R.id.Admin_Login_back:
                //返回
                Intent intent = new Intent(this,Login_Activity.class);
                startActivity(intent);
                finish();
                break;
                //管理员登录
            case R.id.admin_login:
                AdminLogin();
                break;

        }
    }

    //管理员登录
    public void AdminLogin(){
        //判断密码是否为空
        if(adminPassword.getText().toString().equals("")){
            Toast.makeText(this,"密码为空",Toast.LENGTH_LONG).show();
            return;
        }
        //组装数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",adminName.getText().toString());
            jsonObject.put("password",adminPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //向后端发送数据验证
        //学生登录
        String backMsg = NetService.RequestPost(ConfigCenter.ADMIN_LOGIN,jsonObject);
        if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //登录成功
                //跳转管理员主页面
                Intent intent1 = new Intent(this, Admin_Home_MainActivity.class);
                startActivity(intent1);
                this.finish();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
        }
    }
}
