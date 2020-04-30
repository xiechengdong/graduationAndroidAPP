package com.xcd.graduation.activity.admin_activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.xcd.graduation.adapter.BookAdapter;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.updaImage;

import java.util.List;

public class Admin_book_MainActivity extends AppCompatActivity {

    private BookAdapter adapter;
    private ListView show_book;
    private List<Book> books;
    private EditText searchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__book__main);

        show_book = findViewById(R.id.Admin_books__lv);
        searchContent = findViewById(R.id.Admin_books_searchContent);

        //显示所有书籍
        showAllBook();

        //设置列表单击事件
        show_book.setOnItemClickListener(new ItemClikEvent());

    }

    public void click(View view) {
        switch (view.getId()){
            //跳转到学生管理
            case R.id.Admin_student:
                Intent intent = new Intent(this,Admin_Stu_MainActivity.class);
                startActivity(intent);
                finish();
                break;
            //跳转到教师管理
            case R.id.Admin_teacher:
                Intent intent1 = new Intent(this,Admin_Teacher_MainActivity.class);
                startActivity(intent1);
                finish();
                break;
                //搜索
            case R.id.Admin_books_Search:
                //如果没有输入内容，则查询所有
                if(searchContent.getText().toString().equals("")){
                    showAllBook();
                }else {
                    //根据输入内容查询
                    searchBook(searchContent.getText().toString());
                }
                break;
            //返回管理员首页
            case R.id.Admin_book_back:
                Intent intent2 = new Intent(this,Admin_Home_MainActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    //显示所有的书籍
    public void showAllBook(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.BOOK_FIND_ALL,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                books = JSON.parseArray(json, Book.class);
                adapter = new BookAdapter(this,books);
                show_book.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    //根据条件模糊查询
    public void searchBook(String condition){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.SEARCH_CONTENT_BOOK+"/"+condition,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                books = JSON.parseArray(json,Book.class);
                adapter = new BookAdapter(this,books);
                adapter.notifyDataSetChanged();
                show_book.setAdapter(adapter);
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    //设置列表单击事件
    class ItemClikEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取列表的内容
            ImageView cover = view.findViewById(R.id.book_face);
            TextView name = view.findViewById(R.id.book_name);
            TextView teacher_name = view.findViewById(R.id.book_teach_name);
            TextView b_id = view.findViewById(R.id.book_id);
            //将内容装入对象
            final Book book = new Book(b_id.getText().toString(),name.getText().toString(), updaImage.updata(((BitmapDrawable)cover.getDrawable()).getBitmap()),
                    teacher_name.getText().toString());
            //确认框
            AlertDialog alertDialog = new AlertDialog.Builder(Admin_book_MainActivity.this)
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
                            delBookByBookId(book.getId());
                        }
                    }).create();
            alertDialog.show();

        }
    }

    //删除课程
    public void delBookByBookId(String id){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.DELETE_BY_ID+"/"+id,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //删除成功
                showAllBook();
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
