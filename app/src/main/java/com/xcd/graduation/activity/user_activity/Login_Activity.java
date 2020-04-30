package com.xcd.graduation.activity.user_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xcd.graduation.R;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_Home;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.bean.Student;
import com.xcd.graduation.bean.Teacher;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.MapTrunPojo;
import com.xcd.graduation.utils.updaImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Pattern;

public class Login_Activity extends AppCompatActivity {

    private EditText number;
    private EditText password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        //初始化sharedPreferences
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        number = findViewById(R.id.login_number);
        password = findViewById(R.id.login_password);
        imageView = findViewById(R.id.imageview1);

        /**
         * 取出存在sharedPreferences的数据
         */
        String number1 = sharedPreferences.getString("number",null);
        if( number1!= null){
            number.setText(number1);
        }

        //判断是否修改过头像，若修改过显示修改过后的头像
        String headimage = sharedPreferences.getString("headimage",null);
        if(headimage != null){
            //设置头像
            byte[] headimagebyte = null;
            try {
                //将String转为byte[]
                headimagebyte = Base64Util.decode(headimage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将byte[]转为Bitmap
            Bitmap bitmap =  updaImage.BytesToBimap(headimagebyte);
            imageView.setImageBitmap(bitmap);
        }

    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.GoToRegister:
                Intent intent = new Intent(this, Student_Register.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.login:
                if(number.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(this,R.string.NumberOrPasswordIsNull,Toast.LENGTH_LONG).show();
                    return;
                }
                //封装学号(编号)和密码
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("number",number.getText().toString());
                    jsonObject.put("password",password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //判断是学生登录还是老师登录
                if(Pattern.matches(ConfigCenter.regex1,number.getText().toString())){
                    //学生登录
                    String backMsg = NetService.RequestPost(ConfigCenter.Stu_Login,jsonObject);
                    if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
                        Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
                        if(result.getCode() == 20000){
                            //登录成功
                            Student student = (Student) MapTrunPojo.mapToObject((Map<String, Object>) result.getData(),Student.class);
                            //存入到本次，二次登录不在输入用户名和密码
                            addStuUser(student);
                            //跳转到学生主页面
                            Intent intent1 = new Intent(this, Teacher_MainActivity_Home.class);
                            startActivity(intent1);
                            this.finish();
                        }else {
                            Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                    }
                }else if(Pattern.matches(ConfigCenter.regex3,number.getText().toString())){
                    //教师登录
                    String backMsg = NetService.RequestPost(ConfigCenter.Teacher_Login,jsonObject);
                    if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
                        Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
                        if(result.getCode() == 20000){
                            //登录成功
                            Teacher teacher = (Teacher) MapTrunPojo.mapToObject((Map<String, Object>) result.getData(),Teacher.class);
                            //存入到本次，二次登录不在输入用户名和密码
                            addStuUser(teacher);
                            //跳转到教师主界面
                            Intent intent1 = new Intent(this, Teacher_MainActivity_Home.class);
                            startActivity(intent1);
                            this.finish();
                        }else {
                            Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,R.string.Fail_Register,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,R.string.NumberOrPasswordError,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.forget_password:
                Intent intent1 = new Intent(this, forget_password_MainActivity.class);
                startActivity(intent1);
                this.finish();
                break;
//            //管理员登录
//            case R.id.AdminLogin:
//                Intent intent2 = new Intent(this,Admin_Login_MainActivity.class);
//                startActivity(intent2);
//                finish();
//                break;
        }
    }

    //将用户信息存入到
    public void addStuUser(Student student){
        //存储用户信息
        editor.putBoolean("IsLogin",true);
        editor.putString("id",student.getId());
        editor.putString("number",student.getNumber()+"");
        editor.putString("major",student.getMajor());
        editor.putString("name",student.getName());
        editor.putString("phone",student.getPhone());
        editor.putString("headimage",student.getHeadimage());
        editor.commit();
    }

    //将用户信息存入到
    public void addStuUser(Teacher teacher){
        //存储用户信息
        editor.putBoolean("IsLogin",true);
        editor.putString("id",teacher.getId());
        editor.putString("number",teacher.getNumber()+"");
        editor.putString("major",teacher.getDepartment());
        editor.putString("name",teacher.getName());
        editor.putString("phone",teacher.getPhone());
        editor.putString("headimage",teacher.getHeadimage());
        editor.commit();
    }

    /**
     * 动态获取读写权限
     */
    @Override
    protected void onResume() {
        super.onResume();
        getPersimmion();
    }

    @TargetApi(23)
    private void getPersimmion() {
        // 如果应用没有获得对应权限
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //申请WRITE_EXTERNAL_STORAGE权限
            //第一个字符串列是预申请的权限，第三個int是本次请求的辨认编号
            requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 127);
        }
    }
}
