package com.xcd.graduation.activity.student_mainactivity;

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
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_Home;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_MyBooks;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_details;
import com.xcd.graduation.activity.teacher_maiactivity.Teacher_MainActivity_userCenter;
import com.xcd.graduation.adapter.BookAdapter;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.updaImage;

import java.util.List;

public class Stu_MainActivity_MyBooks extends AppCompatActivity {

    private EditText search_condition;
    private SharedPreferences sharedPreferences;
    private List<Book> books;
    private ListView show_book;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main_my_books);

        search_condition = findViewById(R.id.Stu_MyBooks_searchContent);
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        show_book = findViewById(R.id.Stu_MyBooks_lv);

        //显示全部书籍
        findByStuId(sharedPreferences.getString("id",null));

        show_book.setOnItemClickListener(new ItemClickEvent());
    }

    //按照学生Id查找书籍
    public void findByStuId(String Id){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.STU_SHOW_MY_BOOK+"/"+Id,arg);
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

    public void click(View view) {

        switch (view.getId()) {
            case R.id.Stu_MyBooks_Home:
                Intent intent = new Intent(this, Teacher_MainActivity_Home.class);
                startActivity(intent);
                finish();
                break;
            case R.id.Stu_MyBooks_UserCenter:
                Intent intent1 = new Intent(this, Teacher_MainActivity_userCenter.class);
                startActivity(intent1);
                finish();
                break;
            //模糊查询
            case R.id.Stu_MyBooks_Search:
                //根据输入内容迷糊查询
                //如果没有输入内容，则查询所有
                if(search_condition.getText().toString().equals("")){
                    findByStuId(sharedPreferences.getString("id",null));
                }else {
                    SearchByCondition(sharedPreferences.getString("id", null), search_condition.getText().toString());
                }
                break;
        }
    }

    //根据输入内容迷糊查询
    public void SearchByCondition(String id,String SearchCondition){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.STU_SHOW_MY_BOOK_BY_CONDITION+"/"+id+"/"+SearchCondition,arg);
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

    //设置列表单击事件
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
            Intent intent = new Intent(Stu_MainActivity_MyBooks.this, Teacher_MainActivity_details.class);
            intent.putExtra("book",book);
            startActivity(intent);
            finish();
        }
    }
}
