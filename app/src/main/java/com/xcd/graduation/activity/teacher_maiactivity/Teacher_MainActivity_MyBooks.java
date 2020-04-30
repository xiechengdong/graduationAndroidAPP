package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Teacher_MainActivity_MyBooks extends AppCompatActivity {

    private BookAdapter adapter;
    private List<Book> books;
    private SharedPreferences sharedPreferences;
    private ListView show_book;
    private EditText searchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__my_books);

        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        show_book = findViewById(R.id.Teacher_My_Books_lv);
        searchContent = findViewById(R.id.Teacher_My_Books_searchContent);
        findByTeachId(sharedPreferences.getString("id",null));

        //列表单击事件
        show_book.setOnItemClickListener(new ItemClickEvent());
    }


    public void click(View view) {
        switch (view.getId()){
            case R.id.Teacher_My_Books_TeacherHome:
                Intent intent = new Intent(this,Teacher_MainActivity_Home.class);
                startActivity(intent);
                finish();
                break;
            case R.id.Teacher_My_Books_To_UserCenter:
                Intent intent1 = new Intent(this,Teacher_MainActivity_userCenter.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.Teacher_My_Books_Search:
                //如果没有输入内容，则查询所有
                if(searchContent.getText().toString().equals("")){
                    findByTeachId(sharedPreferences.getString("id",null));
                }else {
                    //根据输入内容查询
                    searchBook(searchContent.getText().toString(),sharedPreferences.getString("id",null));
                }
                break;
            //跳转到添加课程页面
            case R.id.Teacher_My_Books_AddBook:
                Intent intent2 = new Intent(this,Teacher_MainActivity_AddBook.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    /**
     * 根据输入内容查询
     */
    private void searchBook(String condition,String id) {
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_BOOKS_BY_TEACHER_ID_AND_SEARCH_CONTENT+"/"+condition+"/"+id,arg);
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

    //按照教师Id查找书籍
    public void findByTeachId(String Id){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_BOOKS_BY_TEACHER_ID+"/"+Id,arg);
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

    //设置列表的单击事件
    class ItemClickEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //通过单击事件，获得单击View选项的内容
            ImageView cover = view.findViewById(R.id.book_face);
            TextView name = view.findViewById(R.id.book_name);
            TextView teacher_name = view.findViewById(R.id.book_teach_name);
            TextView b_id = view.findViewById(R.id.book_id);
            //将内容装入对象
            Book book = new Book(b_id.getText().toString(),name.getText().toString(), updaImage.updata(((BitmapDrawable)cover.getDrawable()).getBitmap()),
                    teacher_name.getText().toString());
            //跳转
            Intent intent = new Intent(Teacher_MainActivity_MyBooks.this,Teacher_MainActivity_details.class);
            intent.putExtra("book",book);
            startActivity(intent);
            finish();
        }
    }
}
