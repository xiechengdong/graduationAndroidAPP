package com.xcd.graduation.activity.admin_activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xcd.graduation.R;
import com.xcd.graduation.adapter.BookAdapter;
import com.xcd.graduation.adapter.GuanggaoAdapter;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Guanggao;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.utils.JsonUtis;

import java.util.List;

public class Admin_guangao_MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Guanggao> guanggaos;
    private GuanggaoAdapter guanggaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_guangao__main);

        listView = findViewById(R.id.Admin_guanggao_lv);

        //显示所有广告
        show_All();

        listView.setOnItemClickListener(new ItemClikEvent());
    }

    //显示所有广告
    private void show_All() {
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.ADMIN_FINDALL_GUANGGAO,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                guanggaos = JSON.parseArray(json, Guanggao.class);
                guanggaoAdapter = new GuanggaoAdapter(this,guanggaos);
                listView.setAdapter(guanggaoAdapter);
                guanggaoAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    public void click(View view) {

        switch (view.getId()){
            //返回管理员首页
            case R.id.Admin_guanggao_back:
                Intent intent = new Intent(this,Admin_Home_MainActivity.class);
                startActivity(intent);
                finish();
                break;
            //添加广告
            case R.id.Admin_guangao_AddGuanggao:
                Intent intent1 = new Intent(this,Admin_Addguanggao_MainActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    //设置单击删除事件
    //设置列表单击事件
    class ItemClikEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取列表的内容
            final TextView guanggao_id = view.findViewById(R.id.Admin_guanggao_id);
            //确认框
            AlertDialog alertDialog = new AlertDialog.Builder(Admin_guangao_MainActivity.this)
                    .setTitle("删除广告")
                    .setMessage("确认删除?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消
                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除广告
                            delgunggaoById(guanggao_id.getText().toString());
                        }
                    }).create();
            alertDialog.show();

        }
    }

    //根据广告id删除
    public void delgunggaoById(String id){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.ADMIN_DEL_GUANGGAO+"/"+id,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //删除成功
                show_All();
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }else {
                //删除失败
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }
}
