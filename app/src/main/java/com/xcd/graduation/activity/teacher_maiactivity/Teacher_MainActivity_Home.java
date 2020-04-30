package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xcd.graduation.R;
import com.xcd.graduation.activity.book_activity.Book_Show_All_Activity;
import com.xcd.graduation.activity.book_activity.Book_Show_Hot_All_Activity;
import com.xcd.graduation.activity.student_mainactivity.Stu_MainActivity_MyBooks;
import com.xcd.graduation.adapter.FlipperAdapter;
import com.xcd.graduation.adapter.GuanggaoAdapter;
import com.xcd.graduation.bean.Guanggao;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.bean.System_info;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.SpUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;

public class Teacher_MainActivity_Home extends AppCompatActivity {

    //轮播图片资源
//    private final int[] viewPager_images = {R.mipmap.guangao1,R.mipmap.guangao2,R.mipmap.guangao3,
//            R.mipmap.guangao4,R.mipmap.guangao5};
    private AdapterViewFlipper flipper;
    private SharedPreferences sharedPreferences;
    private List<Guanggao> guanggaos;
    private TextView phone;
    private List<System_info> system_infos;
    private TextView gonggao_time;
    private TextView gonggao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main__home);

        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        flipper = findViewById(R.id.Teacher_Home_flipper);
        phone = findViewById(R.id.Home_tel);
        gonggao_time = findViewById(R.id.gonggao_Time);
        gonggao = findViewById(R.id.gonggao);
        //显示广告
        show_guanggao_All();
        //显示系统信息
        getAndshowSystemInfo();
    }


    public void click(View view) {
        switch (view.getId()){
            case R.id.Teacher_Home_To_UserCenter:
                Intent intent = new Intent(this,Teacher_MainActivity_userCenter.class);
                startActivity(intent);
                finish();
                break;
            case R.id.Teacher_Look_All_Book:
                Intent intent1 = new Intent(this, Book_Show_All_Activity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.Teacher_Home_To_MyBooks:
                if(Pattern.matches(ConfigCenter.regex3,sharedPreferences.getString("number",null))) {
                    //教师点击
                    Intent intent2 = new Intent(this, Teacher_MainActivity_MyBooks.class);
                    startActivity(intent2);
                }else {
                    //学生点击
                    Intent intent2 = new Intent(this, Stu_MainActivity_MyBooks.class);
                    startActivity(intent2);
                }
                finish();
                break;
            case R.id.Teacher_Look_hot_Book:
                Intent intent2 = new Intent(this, Book_Show_Hot_All_Activity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.Home_tel:
                String number = phone.getText().toString();
                callPhone(number);
                break;
        }
    }

    //显示所有广告
    private void show_guanggao_All() {
        //从缓存里面获取  若没有则从服务器过去
        String  str = SpUtils.getInstance(this,"guanggaoInfo").getString("guanggaos","超时了");
        if(!str.equals("超时了")){
            Type type = new TypeToken<List<Guanggao>>() {}.getType();
            guanggaos = new Gson().fromJson(str,type);
            flipper.setAdapter(new FlipperAdapter(guanggaos,this));
            flipper.startFlipping();
            return;
        }
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.ADMIN_FINDALL_GUANGGAO,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                guanggaos = JSON.parseArray(json, Guanggao.class);
                //将广告信息村存储到缓存
                SpUtils.getInstance(this,"guanggaoInfo").setString("guanggaos", new Gson().toJson(guanggaos),60*60*12);
                flipper.setAdapter(new FlipperAdapter(guanggaos,this));
                flipper.startFlipping();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 获取系统信息并展示
     */
    public void getAndshowSystemInfo(){
        //从缓存里面获取  若没有则从服务器过去
        String  str = SpUtils.getInstance(this,"systeminfo").getString("system_infos","超时了");
        if(!str.equals("超时了")){
            Type type = new TypeToken<List<System_info>>() {}.getType();
            system_infos = new Gson().fromJson(str,type);
            //设置首页信息
            show_hoemInfo(system_infos.get(system_infos.size()-1));
            return;
        }
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FINDALL_SYSTEM_INFO,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                system_infos = JSON.parseArray(json, System_info.class);
                //将系统信息存储到缓存
                SpUtils.getInstance(this,"systeminfo").setString("system_infos", new Gson().toJson(system_infos),60*60*12);
                //设置首页信息
                show_hoemInfo(system_infos.get(system_infos.size()-1));
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }

    }

    //设置首页信息
    public void show_hoemInfo(System_info system_info){
        phone.setText(system_info.getPhone_tel());
        gonggao_time.setText(system_info.getGongao_time());
        gonggao.setText(system_info.getGongao_info());
    }

}
