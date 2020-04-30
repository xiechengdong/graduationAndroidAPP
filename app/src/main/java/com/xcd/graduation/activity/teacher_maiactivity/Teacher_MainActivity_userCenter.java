package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xcd.graduation.R;
import com.xcd.graduation.activity.student_mainactivity.Stu_MainActivity_MyBooks;
import com.xcd.graduation.activity.user_activity.Login_Activity;
import com.xcd.graduation.bean.System_info;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.SpUtils;
import com.xcd.graduation.utils.updaImage;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;


public class Teacher_MainActivity_userCenter extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView headImage;
    List<System_info> system_infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main_usercenter);

        //初始化sharedPreferences
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //初始化控件
        headImage = findViewById(R.id.headImage);
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
            headImage.setImageBitmap(bitmap);
        }
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.exit:
                /**
                 * 清除非第一次登录的标志
                 */
                editor.putBoolean("IsLogin",false);
                editor.commit();
                Intent intent1 = new Intent(this, Login_Activity.class);
                startActivity(intent1);
                this.finish();
                break;
            case R.id.teacher_user_center_userinfo:
                Intent intent2 = new Intent(this,Teacher_MainActivity_userInfo.class);
                startActivity(intent2);
                this.finish();
                break;
            case R.id.Teacher_UserCenter_To_TeacherHome:
                Intent intent = new Intent(this,Teacher_MainActivity_Home.class);
                startActivity(intent);
                finish();
                break;
            case R.id.Teacher_UserCenter_To_MyBooks:
                if(Pattern.matches(ConfigCenter.regex3,sharedPreferences.getString("number",null))) {
                    //教师点击
                    Intent intent3 = new Intent(this, Teacher_MainActivity_MyBooks.class);
                    startActivity(intent3);
                }else {
                    //学生点击
                    Intent intent3 = new Intent(this, Stu_MainActivity_MyBooks.class);
                    startActivity(intent3);
                }
                finish();
                break;
            case R.id.user_center_version_update:
                showExitDialog01("版本","已是最新版本");
                break;
            case R.id.user_center_about:
                String  str = SpUtils.getInstance(this,"systeminfo").getString("system_infos","超时了");
                Type type = new TypeToken<List<System_info>>() {}.getType();
                system_infos = new Gson().fromJson(str,type);
                showExitDialog01("关于",system_infos.get(system_infos.size()-1).getSystem_about());
                break;
        }
    }

    // 简单消息提示框
    private void showExitDialog01(String title,String info){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(info)
                .setPositiveButton("确定", null)
                .show();
    }
}
