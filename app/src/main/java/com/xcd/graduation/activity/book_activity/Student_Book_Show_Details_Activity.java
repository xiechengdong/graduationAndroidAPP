package com.xcd.graduation.activity.book_activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xcd.graduation.R;
import com.xcd.graduation.activity.message_activity.Message_Detail_Activity;
import com.xcd.graduation.activity.student_mainactivity.Stu_MainActivity_MyBooks;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_details;
import com.xcd.graduation.adapter.BookAdapter;
import com.xcd.graduation.adapter.MessageAdapter;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Message;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.FileUtils;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.updaImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class Student_Book_Show_Details_Activity extends AppCompatActivity {

    private Book book;
    private ImageView book_cover;
    private TextView book_name;
    private TextView teacher_name;
    private TextView book_content;
    private String fileName;// 课程内容文件命名
    private File file;
    private SharedPreferences sharedPreferences;
    private String content;
    private EditText messgae_content;
    private ListView show_message;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__book__show__details_);

        //获取Intent传过来的数据
        book = (Book) getIntent().getSerializableExtra("book");
        flag = (String)getIntent().getSerializableExtra("flag");
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        book_cover = findViewById(R.id.Student_Show_all_BookDetails_bookFace);
        book_name = findViewById(R.id.Student_Show_all_bookName);
        teacher_name = findViewById(R.id.Student_Show_all_TeacherName);
        book_content = findViewById(R.id.Student_Show_all_BookContent);
        messgae_content = findViewById(R.id.Student_Show_all_messageContent);
        show_message = findViewById(R.id.Student_Show_all_message_show);

        //留言单击事件
        show_message.setOnItemClickListener(new ItemClickEvent());

        //课程内容文件命名
        fileName = book.getName()+".doc";

        //显示信息
        showBookDetails();
        findMessageByBookId();
    }

    public void click(View view) {

        switch (view.getId()){
                //打开课件
            case R.id.Student_Show_all_openBookContent:
                //从服务器下载课件
                Toast.makeText(this,"下载中...",Toast.LENGTH_LONG).show();
                content = getContent();
                if(content != null){
                    //保存文课程内容文件到本地
                    //将String转为byte[]
                    try {
                        file = FileUtils.createFileWithByte(Base64Util.decode(content), fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(this,"下载完毕",Toast.LENGTH_LONG).show();
                FileUtils.openFile(file, this);
                break;
                //返回
            case R.id.Student_book_show_details_back:
                if(flag.equals("1")){
                    Intent intent = new Intent(this, Book_Show_Hot_All_Activity.class);
                    startActivity(intent);
                    finish();
                }else if (flag.equals("0")){
                    Intent intent = new Intent(this, Book_Show_All_Activity.class);
                    startActivity(intent);
                    finish();
                }
                break;
                //添加课程
            case R.id.Student_show_allBook_addBook:
                //判断是否已经天机过
                if (findBookByBookIdAndStuId().equals("未添加")) {
                    //弹出确认框是否添加
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle("添加课程")
                            .setMessage("确认添加?")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //取消
                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //添加课程到我的课程
                                    Stu_addBook();
                                }
                            }).create();
                    alertDialog.show();
                }
                break;
                //留言
            case R.id.Student_Show_all_messgae:
                addMessage();
                findMessageByBookId();
                messgae_content.setText("");
                break;
        }

    }

    /**
     * 留言
     */
    private void addMessage() {
        if (messgae_content.getText().toString().equals("")){
            Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            return;
        }
        //组建json数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("book_id",book.getId());
            jsonObject.put("username",sharedPreferences.getString("name",null));
            jsonObject.put("content",messgae_content.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //保存到后端
        String backmsg = NetService.RequestPost(ConfigCenter.STU_ADD_MESSAGE,jsonObject);
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

    //初始化信息
    public void showBookDetails(){
        //把数据展示在控件中
        if(book.getCover() != null){
            //设置头像
            byte[] cover1 = null;
            try {
                //将String转为byte[]
                cover1 = Base64Util.decode(book.getCover());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将byte[]转为Bitmap
            Bitmap bitmap =  updaImage.BytesToBimap(cover1);
            book_cover.setImageBitmap(bitmap);
        }
        book_name.setText(book.getName());
        teacher_name.setText(book.getTeacher_name());
        book_content.setText(fileName);
    }

    /**
     *从服务器获取课件
     * @return
     */
    private String getContent() {
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_CONTENT_BY_ID+"/"+book.getId(),arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                return json.toString();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
        return null;
    }

    //添加课程
    public void Stu_addBook(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.STU_BOOK_ADD_BOOK
                +"/"+sharedPreferences.getString("id",null) +"/"+book.getId(),arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 判断是否已经添加过
     */
    public String findBookByBookIdAndStuId(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_BOOK_BY_BOOKID_AND_STUID
                +"/"+sharedPreferences.getString("id",null) +"/"+book.getId(),arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000 && result.getData() == null){
                return "未添加";
            }else {
                Toast.makeText(this,"已添加过该课程",Toast.LENGTH_LONG).show();
                return "";
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
            return "";
        }
    }

    /**
     * 查找留言
     */
    public void findMessageByBookId(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_MESSAGE_BY_BOOKID+"/"+book.getId(),arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                messages = JSON.parseArray(json,Message.class);
                messageAdapter = new MessageAdapter(this,messages);
                messageAdapter.notifyDataSetChanged();
                show_message.setAdapter(messageAdapter);
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 设置留言单击事件
     */
    //设置列表单击事件
    class ItemClickEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //通过单击事件，获得单击View选项的内容
            TextView username = view.findViewById(R.id.message_username);
            TextView message_content = view.findViewById(R.id.message_content);
            TextView message_time = view.findViewById(R.id.message_time);
            TextView message_id = view.findViewById(R.id.message_id);
            //将内容装入对象
            Message message = new Message();
            message.setId(message_id.getText().toString());
            message.setUsername(username.getText().toString());
            message.setContent(message_content.getText().toString());
            message.setCreate_time(message_time.getText().toString());
            //跳转至留言详情页面
            Intent intent = new Intent(Student_Book_Show_Details_Activity.this, Message_Detail_Activity.class);
            intent.putExtra("message",message);
            intent.putExtra("book",book);
            intent.putExtra("flag",flag);
            startActivity(intent);
            finish();
        }
    }
}
