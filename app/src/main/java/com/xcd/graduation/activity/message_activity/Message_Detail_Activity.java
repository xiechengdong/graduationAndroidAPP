package com.xcd.graduation.activity.message_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xcd.graduation.R;
import com.xcd.graduation.activity.book_activity.Book_Show_details_Activity;
import com.xcd.graduation.activity.book_activity.Student_Book_Show_Details_Activity;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_details;
import com.xcd.graduation.adapter.Message_detailsAdapter;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Message;
import com.xcd.graduation.bean.Message_details;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.JsonUtis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

public class Message_Detail_Activity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Message message;
    private Book book;
    private TextView message_time;
    private TextView username;
    private TextView content;
    private String flag;
    private List<Message_details> messages;
    private EditText message_replycontent;
    private Message_detailsAdapter message_deAdapter;
    private ListView show_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail_activity);

        //初始化
        //从Intent获取参值
        message = (Message) getIntent().getSerializableExtra("message");
        book = (Book) getIntent().getSerializableExtra("book");
        flag = (String) getIntent().getSerializableExtra("flag");
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        //绑定控件
        message_time = findViewById(R.id.message_detail_time);
        username  = findViewById(R.id.message_detail_username);
        content = findViewById(R.id.message_detail_content);
        message_replycontent = findViewById(R.id.message_details_replycontent);
        show_message = findViewById(R.id.message_reply_show);

        //初始化控件
        showParentMessage();
        findMessagesByPrentId(); //初始化列表
    }

    //显示父级留言
    public void showParentMessage(){
        message_time.setText(message.getCreate_time());
        username.setText(message.getUsername());
        content.setText(message.getContent());
    }

    public void click(View view) {

        switch (view.getId()){
            //返回
            case R.id.message_reply_back:
                if(flag.equals("2")){
                    Intent intent = new Intent(this, Teacher_MainActivity_details.class);
                    intent.putExtra("book",book);
                    startActivity(intent);
                    finish();
                }else if(!flag.equals("2") && Pattern.matches(ConfigCenter.regex3,sharedPreferences.getString("number",null))){
                    //跳转到教师所有课程查看详情的页面
                    Intent intent = new Intent(this, Book_Show_details_Activity.class);
                    intent.putExtra("book",book);
                    intent.putExtra("flag",flag);
                    startActivity(intent);
                    finish();
                }else {
                    //跳转带学生所有课程查看详情页面
                    Intent intent = new Intent(this, Student_Book_Show_Details_Activity.class);
                    intent.putExtra("book",book);
                    intent.putExtra("flag",flag);
                    startActivity(intent);
                    finish();
                }

                break;
            //回复留言
            case R.id.message_detail_send:
                replyMessage();
                findMessagesByPrentId();
                message_replycontent.setText("");
                break;

        }
    }

    //回复
    public void replyMessage(){
        if (message_replycontent.getText().toString().equals("")){
            Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            return;
        }
        //组装JSON数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parent_id",message.getId());
            jsonObject.put("username",sharedPreferences.getString("name",null));
            jsonObject.put("content",message_replycontent.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //保存到后端
        String backmsg = NetService.RequestPost(ConfigCenter.ADD_REPLY,jsonObject);
        if (!backmsg.equals(ConfigCenter.Server_ERR) && !backmsg.equals("")) {
            Result result = JsonUtis.jsonToPojo(backmsg, Result.class);
            if(result.getCode() == 20000){
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.Fail_Register, Toast.LENGTH_LONG).show();
        }
    }

    //根据留言的父id查找回复显示在列表里面
    public void findMessagesByPrentId(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_MESSAGEA_BY_PARENT_ID+"/"+message.getId(),arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                messages = JSON.parseArray(json,Message_details.class);
                message_deAdapter = new Message_detailsAdapter(this,messages);
                message_deAdapter.notifyDataSetChanged();
                show_message.setAdapter(message_deAdapter);
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }
}
