package com.xcd.graduation.activity.user_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcd.graduation.R;
import com.xcd.graduation.activity.user_activity.Login_Activity;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.JsonUtis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Student_Register extends AppCompatActivity {

    private EditText StuName;
    private EditText StuNumber;
    private EditText StuMajor;
    private EditText StuPassword1;
    private EditText StuPassword2;
    private EditText StuPhone;
    private EditText StuCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__register);

        //定义文本控件
        StuName = findViewById(R.id.StuName);
        StuNumber = findViewById(R.id.StuNumber);
        StuMajor = findViewById(R.id.StuMajor);
        StuPassword1 = findViewById(R.id.StuPassword1);
        StuPassword2 = findViewById(R.id.StuPassword2);
        StuPhone = findViewById(R.id.StuPhone);
        StuCode = findViewById(R.id.StuCode);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.Register:
                //调验证数据合法性的函数并得到返回结果
                String msg = CheckMsg();
                if(msg.equals(ConfigCenter.SUCCESS)){
                    //判断是否被注册过
                    if(isRegister()){
                        if(isPhoneRegister()){
                            //将数据封装json格式数据
                            JSONObject stu_json = new JSONObject();
                            try {
                                stu_json.put("name",StuName.getText().toString());
                                stu_json.put("number",StuNumber.getText().toString());
                                stu_json.put("major",StuMajor.getText().toString());
                                stu_json.put("password",StuPassword1.getText().toString());
                                stu_json.put("phone",StuPhone.getText().toString());
                                stu_json.put("code",StuCode.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //请求服务器并返回数据
                            String JSONStr =  NetService.RequestPost(ConfigCenter.Stu_RegisterURl+"/"+StuCode.getText().toString(),stu_json);
                            if(!JSONStr.equals(ConfigCenter.Server_ERR) && !JSONStr.equals("")){
                                Result result = JsonUtis.jsonToPojo(JSONStr, Result.class);
                                if(result.getCode() == 20000){
                                    Toast.makeText(this,R.string.Success_Register,Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(this,R.string.is_Register_phone,Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,R.string.is_Register_number,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.GetCode:
                if(!StuPhone.getText().toString().equals("") && Pattern.matches(ConfigCenter.regex2,StuPhone.getText().toString())){
                    //组建请求URL
                    String Url = ConfigCenter.GetCodeUrl+"/"+StuPhone.getText().toString();
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
            case R.id.back_Login:
                Intent intent = new Intent(this, Login_Activity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    /**
     * 验证输入合法性的方法
     */
    public String CheckMsg(){
        if(StuName.getText().toString().equals("")){
            return ConfigCenter.UserNameIsNull;
        }else if(StuNumber.getText().toString().equals("")){
            return ConfigCenter.UserNumberIsNull;
        }else if(StuMajor.getText().toString().equals("")){
            return ConfigCenter.UserMajorIsNull;
        }else if (StuPassword1.getText().toString().equals("")){
            return ConfigCenter.UserPassword1IsNull;
        }else if(StuPassword2.getText().toString().equals("")){
            return ConfigCenter.UserPassword2IsNull;
        }else if (StuPhone.getText().toString().equals("")){
            return ConfigCenter.UserPhoneIsNull;
        }else if(!StuPassword1.getText().toString().equals(StuPassword2.getText().toString())){
            return ConfigCenter.Password1_not_Password2;
        }else if(StuCode.getText().toString().equals("")){
            return ConfigCenter.CodeIsNull;
        }else if(!Pattern.matches(ConfigCenter.regex1,StuNumber.getText().toString()) && !Pattern.matches(ConfigCenter.regex3,StuNumber.getText().toString())){
            return ConfigCenter.Number_Error;
        }else if(!Pattern.matches(ConfigCenter.regex2,StuPhone.getText().toString())){
            return ConfigCenter.Phone_Error;
        }
        return ConfigCenter.SUCCESS;
    }

    /**
     * 验证该学号是否被注册过
     */
    public Boolean isRegister(){
        String[] strings = null;
        String backMsg =  GETNetService.RequestGet(ConfigCenter.IsRegister+"/"+StuNumber.getText().toString(),strings);
        if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")) {
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if (result.getData() == null) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
    /**
     * 验证该手机是否被注册过
     */
    public Boolean isPhoneRegister(){
        String[] strings = null;
        String backMsg =  GETNetService.RequestGet(ConfigCenter.IsRegister+"/"+StuPhone.getText().toString(),strings);
        if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")) {
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if (result.getData() == null) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
}
