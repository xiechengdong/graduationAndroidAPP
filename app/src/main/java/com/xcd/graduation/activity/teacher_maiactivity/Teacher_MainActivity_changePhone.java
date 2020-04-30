package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcd.graduation.R;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.JsonUtis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Teacher_MainActivity_changePhone extends AppCompatActivity {

    private EditText phone;
    private EditText code;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main_change_phone);

        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        phone = findViewById(R.id.Teacher_user_chagngePhone);
        code = findViewById(R.id.Teacher_user__Code);
    }

    public void click(View view){
        switch (view.getId()){
            case R.id.Teacher_user_backUserInfo:
                Intent intent = new Intent(this,Teacher_MainActivity_userInfo.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.Teacher_user__GetCode:
                if(!phone.getText().toString().equals("") && Pattern.matches(ConfigCenter.regex2,phone.getText().toString())){
                    //组建请求URL
                    String Url = ConfigCenter.GetCodeUrl+"/"+phone.getText().toString();
                    //请求服务器并返回数据
                    String backInfo = NetService.RequestPost(Url,null);
                    if(!backInfo.equals(ConfigCenter.Server_ERR) && !backInfo.equals("")){
                        Result result = (Result) JsonUtis.jsonToPojo(backInfo, Result.class);
                        if(result.getCode() == 20000){
                            Toast.makeText(this,R.string.Success_GetCode,Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,ConfigCenter.Phone_Error,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.Teacher_user_Determine:
                if(!phone.getText().toString().equals("") && !code.getText().toString().equals("") ){
                    if(Pattern.matches(ConfigCenter.regex2,phone.getText().toString())){
                        String msg = isPhoneRegister();
                        if(msg.equals(R.string.PhoneIsNotRegister+"")){
                            //封装json格式数据
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("id",sharedPreferences.getString("id",null));
                                jsonObject.put("number",sharedPreferences.getString("number",null));
                                jsonObject.put("phone",phone.getText().toString());
                                //传送数据到服务器
                                changePhone(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if(msg.equals(R.string.is_use_phone+"")){
                            Toast.makeText(this,R.string.is_use_phone,Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(this,ConfigCenter.Server_ERR,Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,ConfigCenter.Phone_Error,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,ConfigCenter.UserCodesOrPhoneNull,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * 验证该手机是否被注册过
     */
    public String isPhoneRegister(){
        String[] strings = null;
        String backMsg =  GETNetService.RequestGet(ConfigCenter.IsRegister+"/"+phone.getText().toString(),strings);
        if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")) {
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if (result.getData() == null) {
                return R.string.PhoneIsNotRegister+"";
            } else {
                return R.string.is_use_phone+"";
            }
        }else {
            return ConfigCenter.Server_ERR;
        }
    }
    /**
     * 修改手机号
     */
    public void changePhone(JSONObject jsonObject){
        String backMsg = NetService.RequestPost(ConfigCenter.CHNAGE_PHONE+"/"+code.getText().toString(),jsonObject);
        if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
                editor.putString("phone",phone.getText().toString());
                editor.commit();
            }else {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
        }
    }
}
