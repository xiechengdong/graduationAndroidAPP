package com.xcd.graduation.activity.user_activity;

import androidx.appcompat.app.AppCompatActivity;

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

public class Modify_Password_Activity extends AppCompatActivity {

    EditText password1;
    EditText password2;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__password_);

        password1 = findViewById(R.id.modify_password1);
        password2 = findViewById(R.id.modify_password2);
        sharedPreferences = this.getSharedPreferences("forget_Password", Context.MODE_PRIVATE);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.modify_password_backLogin:
                Intent intent = new Intent(this, Login_Activity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.modify_password_Determine:
                //判断合法性及两次密码是否一致
                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();
                if(pass1.equals("") || pass2.equals("") || !pass1.equals(pass2)){
                    Toast.makeText(this,R.string.Pass1AndPass2Error,Toast.LENGTH_LONG).show();
                    return;
                }
                //封装json数据
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password",pass1);
                    jsonObject.put("number",sharedPreferences.getString("number",null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //请求数据
                String backMsg = NetService.RequestPost(ConfigCenter.MODIFY_PASSWORD,jsonObject);
                if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
                    Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
                    if(result.getCode() == 20000) {
                        Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
