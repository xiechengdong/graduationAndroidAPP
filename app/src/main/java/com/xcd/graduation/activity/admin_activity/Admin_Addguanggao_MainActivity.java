package com.xcd.graduation.activity.admin_activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xcd.graduation.R;
import com.xcd.graduation.bean.Guanggao;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.updaImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Admin_Addguanggao_MainActivity extends AppCompatActivity {

    private ImageView guanggao_face;
    private EditText guanggao_name;
    private EditText guanggao_start;
    private EditText guanggao_end;
    private Guanggao guanggao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__addguanggao__main);

        guanggao_face = findViewById(R.id.Admin_Add_guanggao_face);
        guanggao_name = findViewById(R.id.Admin_Add_guangao_name);
        guanggao_start = findViewById(R.id.Admin_Add_guangao_start);
        guanggao_end = findViewById(R.id.Admin_Add_guangao_end);
        guanggao = new Guanggao();

    }

    public void click(View view) {

        switch (view.getId()){
            //返回广告列表页
            case R.id.Admin_Add_guanggao_back:
                Intent intent = new Intent(this,Admin_guangao_MainActivity.class);
                startActivity(intent);
                finish();
                break;
            //从本地获取广告图片
            case R.id.Admin_Add_guanggao_face:
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                break;
            //添加上传到服务器
            case R.id.Admin_Add_guangao_add:
                String s = IsNull();
                if (s != null){
                    Toast.makeText(this,s,Toast.LENGTH_LONG).show();
                    return;
                }
                guanggao.setName(guanggao_name.getText().toString());
                guanggao.setStart(guanggao_start.getText().toString());
                guanggao.setOver(guanggao_end.getText().toString());
                AddGuanggao(guanggao);
                break;
        }
    }

    //添加广告
    private void AddGuanggao(Guanggao guanggao) {
        //准备JSON格式的数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",guanggao.getName());
            jsonObject.put("start",guanggao.getStart());
            jsonObject.put("over",guanggao.getOver());
            jsonObject.put("content",guanggao.getContent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String backMsg = NetService.RequestPost(ConfigCenter.ADMIN_ADD_GUANGGAO, jsonObject);
        if (!backMsg.equals(ConfigCenter.Server_ERR) && !backMsg.equals("")) {
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.Fail_Register, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        点击取消结束程序
         */
        if (data == null) {
            Toast.makeText(this, R.string.cancle, Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case 1:
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = updaImage.zoomImage(bitmap, 1000, 250);
                guanggao_face.setImageBitmap(bitmap);
                String cover = updaImage.updata(bitmap);
                guanggao.setContent(cover);
                break;
        }
    }

    //判断控件内容是否为空
    public String IsNull(){
        if (guanggao_name.getText().toString().equals("")){
            return "广告商名称为空";
        }else if(guanggao_start.getText().toString().equals("")){
            return "开始时间为空";
        }else if (guanggao_end.getText().toString().equals("")){
            return"结束时间为空";
        }else {
            return null;
        }
    }
}
