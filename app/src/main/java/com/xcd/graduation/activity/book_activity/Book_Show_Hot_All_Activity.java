package com.xcd.graduation.activity.book_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xcd.graduation.R;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_Home;
import com.xcd.graduation.adapter.BookAdapter;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.SpUtils;
import com.xcd.graduation.utils.updaImage;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;

public class Book_Show_Hot_All_Activity extends AppCompatActivity {

    private BookAdapter adapter;
    private ListView show_book;
    private List<Book> books;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__show__hot__all_);

        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        show_book = findViewById(R.id.book_show_hot_all_lv);

        showAllBook();

        //设置列表单击事件
        show_book.setOnItemClickListener(new Book_Show_Hot_All_Activity.ItemClickEvent());
    }

    public void click(View view) {
        switch (view.getId()){
            //返回
            case R.id.book_show_hot_hall_back:
                Intent intent = new Intent(this, Teacher_MainActivity_Home.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    //显示所有的书籍
    public void showAllBook(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.SHOW_HOT_BOOKS,arg);
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
            if(Pattern.matches(ConfigCenter.regex3,sharedPreferences.getString("number",null))){
                //教师点击
                Intent intent = new Intent(Book_Show_Hot_All_Activity.this, Book_Show_details_Activity.class);
                intent.putExtra("book",book);
                intent.putExtra("flag","1");
                startActivity(intent);
            }else {
                //学生点击
                Intent intent = new Intent(Book_Show_Hot_All_Activity.this,Student_Book_Show_Details_Activity.class);
                intent.putExtra("book",book);
                intent.putExtra("flag","1");
                startActivity(intent);
            }
            finish();
        }
    }
}
