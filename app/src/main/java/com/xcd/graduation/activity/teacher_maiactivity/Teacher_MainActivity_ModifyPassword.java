package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcd.graduation.R;
import com.xcd.graduation.activity.user_activity.Login_Activity;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.JsonUtis;

import org.json.JSONException;
import org.json.JSONObject;

public class Teacher_MainActivity_ModifyPassword extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword1;
    private EditText newPassword2;
    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main__modify_password);

        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        oldPassword = findViewById(R.id.Teacher_modify_oldPassword);
        newPassword1 = findViewById(R.id.Teacher_modify_password1);
        newPassword2 = findViewById(R.id.Teacher_modify_password2);

    }

    public void click(View view){
        switch (view.getId()){
            case R.id.Teach_modify_password_backUserInfo:
                intent =new Intent(this,Teacher_MainActivity_userInfo.class);
                startActivity(intent);
                finish();
                break;
            case R.id.Teacher_modify_password_Determine:
                //判断密码合法性
                String msg = checkPassword();
                if(msg.equals(ConfigCenter.SUCCESS)){
                    //连接服务器，判断旧密码是否正确
                    ModifyPasswordPost();
                }else {
                    Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public String checkPassword(){
        if(oldPassword.getText().toString().equals("")){
            return ConfigCenter.OldPasswordIsNull;
        }else if(newPassword1.getText().toString().equals("")){
            return ConfigCenter.NewPasswordIsNull;
        }else if(newPassword2.getText().toString().equals("")){
            return ConfigCenter.UserPassword2IsNull;
        }else if(!newPassword1.getText().toString().equals(newPassword2.getText().toString())){
            return ConfigCenter.NewPassword1IsNotNewPassword2;
        }else if(oldPassword.getText().toString().equals(newPassword1.getText().toString())){
            return ConfigCenter.OldPasswordISNewPassword;
        } else {
            return ConfigCenter.SUCCESS;
        }
    }

    public void ModifyPasswordPost(){
        //组装数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",sharedPreferences.getString("id",null));
            jsonObject.put("password",newPassword1.getText().toString());
            jsonObject.put("number",sharedPreferences.getString("number",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String backMsg = NetService.RequestPost(ConfigCenter.USER_MODIFY_PASSWORD+"/"+oldPassword.getText().toString(),jsonObject);
        if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
            Result result = JsonUtis.jsonToPojo(backMsg,Result.class);
            if(result.getCode() == 20000){
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
                intent = new Intent(this, Login_Activity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
        }
    }
}
