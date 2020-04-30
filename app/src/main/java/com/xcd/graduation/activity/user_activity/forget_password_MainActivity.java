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
import com.xcd.graduation.activity.user_activity.Modify_Password_Activity;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.bean.Student;
import com.xcd.graduation.bean.Teacher;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.MapTrunPojo;

import java.util.Map;
import java.util.regex.Pattern;

public class forget_password_MainActivity extends AppCompatActivity {

    private EditText phone;
    private EditText code;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password__main);

        phone = findViewById(R.id.forget_password_Phone);
        code = findViewById(R.id.forget_password_Code);
        sharedPreferences = this.getSharedPreferences("forget_Password", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.forget_password_backLogin:
                Intent intent = new Intent(this, Login_Activity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.forget_password_GetCode:
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
            case R.id.forget_password_Determine:
                Intent intent1 = new Intent(this, Modify_Password_Activity.class);
                if(!code.getText().toString().equals("") && !phone.getText().toString().equals("") && Pattern.matches(ConfigCenter.regex2,phone.getText().toString())){
                    //验证验证码是否正确并返回结果
                    //封装URL
                    String url = ConfigCenter.FORGET_PASSWORD+"/"+phone.getText().toString()+"/"+code.getText().toString();
                    //请求数据
                    String [] strings = null;
                    String backMsg =  GETNetService.RequestGet(url,strings);
                    if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
                        Result result = (Result) JsonUtis.jsonToPojo(backMsg, Result.class);
                        if(result.getCode() == 20000){
                            //判断该手机号是否被注册过
                            if(result.getData() != null){
                                //判断查找的数据是学生还是教师
                                if(result.getMessage().equals("查找成功1")){
                                    //查找的为学生，存入到SharedPreferences
                                    Student student =(Student) MapTrunPojo.mapToObject( (Map<String,Object>) result.getData(), Student.class);
                                    addStuUser(student);
                                    //跳转到重设密码的界面
                                    startActivity(intent1);
                                    finish();
                                }else {
                                    //查找的为教师，存入到SharedPreferences
                                    Teacher teacher =(Teacher) MapTrunPojo.mapToObject( (Map<String,Object>) result.getData(), Teacher.class);
                                    addStuUser(teacher);
                                    //跳转到重设密码的界面
                                    startActivity(intent1);
                                    finish();
                                }
                            }else {
                                Toast.makeText(this,R.string.PhoneIsNotRegister,Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,ConfigCenter.UserCodesOrPhoneNull,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //将用户信息存入到sharedPreferences
    public void addStuUser(Student student){
        //存储用户信息
        editor.putString("number",student.getNumber()+"");
        editor.putString("major",student.getMajor());
        editor.putString("name",student.getName());
        editor.putString("phone",student.getPhone());
        editor.commit();
    }

    //将用户信息存入到
    public void addStuUser(Teacher teacher){
        //存储用户信息
        editor.putString("number",teacher.getNumber()+"");
        editor.putString("major",teacher.getDepartment());
        editor.putString("name",teacher.getName());
        editor.putString("phone",teacher.getPhone());
        editor.commit();
    }
}
