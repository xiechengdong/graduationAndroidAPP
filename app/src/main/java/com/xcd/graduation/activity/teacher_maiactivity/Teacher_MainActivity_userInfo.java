package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xcd.graduation.R;
import com.xcd.graduation.activity.user_activity.Modify_Password_Activity;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.bean.Teacher;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.updaImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class Teacher_MainActivity_userInfo extends AppCompatActivity {

    private ImageView HeadImage;
    private EditText Name;
    private EditText Number;
    private EditText Major;
    private EditText Phone;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Teacher teacher;
    private String headimage = null;
    private int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main_user_info);

        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        teacher = new Teacher();

        /*
        初始化控件
         */
        HeadImage = findViewById(R.id.Teacher_userInfo_headImage);
        Name = findViewById(R.id.Teacher_userInfo_Name);
        Number = findViewById(R.id.Teacher_userInfo_Number);
        Major = findViewById(R.id.Teacher_userInfo_Major);
        Phone = findViewById(R.id.Teacher_userInfo_Phone);

        /**
         * 给控件设置默认值
         */
        headimage = sharedPreferences.getString("headimage",null);
        if(headimage != null){
            byte[] headimagebyte = null;
            try {
                headimagebyte = Base64Util.decode(headimage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HeadImage.setImageBitmap(updaImage.BytesToBimap(headimagebyte));
        }
        Name.setText(sharedPreferences.getString("name",null));
        Number.setText(sharedPreferences.getString("number",null));
        Number.setEnabled(false);
        Major.setText(sharedPreferences.getString("major",null));
        Phone.setText(sharedPreferences.getString("phone",null));
        Phone.setEnabled(false);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.Teacher_userInfo_headImage:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.Teacher_userInfo_back_userCenter:
                if(flag == 1){
                    //表示没有点击修改，需还原本地头像数据
                    editor.putString("headimage",headimage);
                    editor.commit();
                }
                Intent intent1 = new Intent(this,Teacher_MainActivity_userCenter.class);
                startActivity(intent1);
                this.finish();
                break;
            case R.id.Teacher_userInfo_Modify:
                //教师修改信息
                flag = 2;
                teacher.setId(sharedPreferences.getString("id", null));
                if (!Name.getText().toString().equals("")) {
                    teacher.setName(Name.getText().toString());
                } else {
                    Toast.makeText(this, ConfigCenter.UserNameIsNull, Toast.LENGTH_LONG).show();
                    return;
                }
                teacher.setNumber(Long.valueOf(Number.getText().toString()));
                if (!Major.getText().toString().equals("")) {
                    teacher.setDepartment(Major.getText().toString());
                } else {
                    Toast.makeText(this, ConfigCenter.UserMajorIsNull, Toast.LENGTH_LONG).show();
                    return;
                }
                teacher.setPhone(Phone.getText().toString());
                teacher.setHeadimage(sharedPreferences.getString("headimage", null));
                postModifyInfo(teacher);
                break;
            case R.id.Teacher_userInfo_ModifyPassword:
                Intent intent2 = new Intent(this, Teacher_MainActivity_ModifyPassword.class);
                startActivity(intent2);
                this.finish();
                break;
            case R.id.Teacher_userInfo_chang_phone:
                Intent intent3 = new Intent(this,Teacher_MainActivity_changePhone.class);
                startActivity(intent3);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        点击取消结束程序
         */
        if(data == null || requestCode != 1){
            Toast.makeText(this,R.string.cancle,Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode){
            case 1:
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = updaImage.zoomImage(bitmap,250,250);
                HeadImage.setImageBitmap(bitmap);
                teacher.setHeadimage(updaImage.updata(bitmap));
                editor.putString("headimage",updaImage.updata(bitmap));
                editor.commit();
                break;
        }
    }

    private void postModifyInfo(Teacher teacher) {
        //组装JSON数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",teacher.getId());
            jsonObject.put("major",teacher.getDepartment());
            jsonObject.put("name",teacher.getName());
            jsonObject.put("headimage",teacher.getHeadimage());
            jsonObject.put("number",teacher.getNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String backMsg = NetService.RequestPost(ConfigCenter.ADD_HEAD_IMAGE,jsonObject);
        //调用POST服务
        if(!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                Toast.makeText(this,R.string.ModifyInfoSuccess,Toast.LENGTH_LONG).show();
                //更新SharedPreferences的信息
                editor.putString("headimage",teacher.getHeadimage());
                editor.putString("major",teacher.getDepartment());
                editor.putString("name",teacher.getName());
                editor.commit();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
                //还原头像数据
                editor.putString("headimage",headimage);
                editor.commit();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
            //还原头像数据
            editor.putString("headimage",headimage);
            editor.commit();
        }
    }
}
