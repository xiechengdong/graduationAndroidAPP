package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import java.util.regex.Pattern;

public class Teacher_MainActivity_details extends AppCompatActivity {

    private Book book;
    private ImageView book_cover;
    private TextView book_name;
    private TextView teacher_name;
    private TextView book_content;
    private String fileName;// 课程内容文件命名
    private File file;
    private SharedPreferences sharedPreferences;
    private EditText message_content;
    private ListView message_lv;
    private List<Message> messages;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main_details);

        //获取Intent传过来的数据
        book = (Book) getIntent().getSerializableExtra("book");
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        book_cover = findViewById(R.id.Teacher_BookDetails_bookFace);
        book_name = findViewById(R.id.Teacher_BookDetails_bookName);
        teacher_name = findViewById(R.id.Teacher_BookDetails_TeacherName);
        book_content = findViewById(R.id.Teacher_BookDetails_BookContent);
        message_content = findViewById(R.id.Teacher_BookDetails_messageContent);
        message_lv = findViewById(R.id.Teacher_BookDetails_message_show);

        //课程内容文件命名
        fileName = book.getName()+".doc";

        //显示信息
        showBookDetails();
        //显示留言
        findMessageByBookId();

        //设置留言单击事件
        message_lv.setOnItemClickListener(new ItemClickEvent());
    }

    public void click(View view) {

        switch (view.getId()){
                //返回
            case R.id.book_show_details_back:
                if(Pattern.matches(ConfigCenter.regex3,sharedPreferences.getString("number",null))) {
                    //跳转到教师我的课程
                    Intent intent = new Intent(this, Teacher_MainActivity_MyBooks.class);
                    startActivity(intent);
                    finish();
                }else {
                    //跳转到学生我的课程
                    Intent intent = new Intent(this, Stu_MainActivity_MyBooks.class);
                    startActivity(intent);
                    finish();
                }
                break;
                //打开课程内容
            case R.id.Teacher_BookDetails_openBookContent:
                //从服务器下载课件
                Toast.makeText(this,"下载中...",Toast.LENGTH_LONG).show();
                String content = getContent();
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
                //删除课程
            case R.id.Teacher_My_Books_delBook:
                //判断是是教师还是学生，教师为删除课程，学生为移除课程
                if(Pattern.matches(ConfigCenter.regex3,sharedPreferences.getString("number",null))){
                    //弹出确认框是否删除
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle("删除课程")
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
                                    //删除课程
                                    deleteByBookId();
                                    //删除学生收藏的课程
                                    delStuBookByBookId();
                                    //删除后返回
                                    back_My_book();
                                }
                            }).create();
                    alertDialog.show();
                }else {
                    //学生操作
                    //弹出确认框是否移除
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle("移除课程")
                            .setMessage("确认移除?")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //取消
                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //移除课程
                                    reMove();
                                    //删除后返回
                                    Stu_Back_My_Book();
                                }
                            }).create();
                    alertDialog.show();
                }
                break;
                //留言
            case R.id.Teacher_BookDetails_make_messgae:
                addMessage();
                findMessageByBookId();
                message_content.setText("");
                break;
        }
    }

    /**
     * 留言
     */
    private void addMessage() {
        if (message_content.getText().toString().equals("")){
            Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            return;
        }
        //组建json数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("book_id",book.getId());
            jsonObject.put("username",sharedPreferences.getString("name",null));
            jsonObject.put("content",message_content.getText().toString());
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

    //根据id删除课程
    public void deleteByBookId(){
        String[] arg = null;
        String backmsg = GETNetService.RequestGet(ConfigCenter.DELETE_BY_ID+"/"+book.getId(),arg);
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

    //教师返回我的课程
    public void back_My_book(){
        Intent intent = new Intent(this,Teacher_MainActivity_MyBooks.class);
        startActivity(intent);
        finish();
    }
    //学生返回我的课程
    public void Stu_Back_My_Book(){
        Intent intent = new Intent(this, Stu_MainActivity_MyBooks.class);
        startActivity(intent);
        finish();
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

    /**
     * 学生根据课程id移除课程
     */
    public void reMove(){
        String[] arg = null;
        String backmsg = GETNetService.RequestGet(ConfigCenter.STU_REMOVE_BOOK
                        +"/"+sharedPreferences.getString("id",null)+"/"+book.getId(),arg);
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
                message_lv.setAdapter(messageAdapter);
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 根据书籍id删除学生收藏
     */
    public void delStuBookByBookId(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.DEL_BYBOOK_ID+"/"+book.getId(),arg);
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
            //跳转到留言详情页面
            Intent intent = new Intent(Teacher_MainActivity_details.this, Message_Detail_Activity.class);
            intent.putExtra("message",message);
            intent.putExtra("book",book);
            intent.putExtra("flag","2");
            startActivity(intent);
            finish();
        }
    }
}
