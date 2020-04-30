package com.xcd.graduation.activity.admin_activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xcd.graduation.R;
import com.xcd.graduation.adapter.StuAdapter;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.bean.Student;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.GETNetService;
import com.xcd.graduation.utils.JsonUtis;

import java.util.List;

public class Admin_Stu_MainActivity extends AppCompatActivity {

    private StuAdapter adapter;
    private ListView show_stu;
    private List<Student> students;
    private EditText searchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__stu__main);

        //初始化控件
        show_stu = findViewById(R.id.Admin_stu__lv);
        searchContent = findViewById(R.id.Admin_stu_searchContent);

        //查找所有学生
        findAllStudent();

        //设置单击事件
        show_stu.setOnItemClickListener(new ItemClikEvent());
    }

    public void click(View view) {

        switch (view.getId()){
            //跳转到课程管理
            case R.id.Admin_books:
                Intent intent = new Intent(this, Admin_book_MainActivity.class);
                startActivity(intent);
                finish();
                break;
            //跳转到教师管理
            case R.id.Admin_teacher:
                Intent intent1 = new Intent(this,Admin_Teacher_MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            //模糊查询
            case R.id.Admin_stu_Search:
                //如果没有输入内容，则查询所有
                if(searchContent.getText().toString().equals("")){
                    findAllStudent();
                }else {
                    //根据输入内容查询
                    findStuBySearchContent();
                }
                break;
            //返回管理员首页
            case R.id.Admin_stu_back:
                Intent intent2 = new Intent(this,Admin_Home_MainActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    //查找所有学生
    private void findAllStudent() {
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_ALL_STUDENT,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                students = JSON.parseArray(json, Student.class);
                adapter = new StuAdapter(this,students);
                show_stu.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    //设置单击事件
    //设置列表单击事件
    class ItemClikEvent implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取列表的内容
            final TextView stu_id = view.findViewById(R.id.stu_id);
            //确认框
            AlertDialog alertDialog = new AlertDialog.Builder(Admin_Stu_MainActivity.this)
                    .setTitle("注销学生用户")
                    .setMessage("确认注销?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消
                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除学生
                            delStudentByStuId(stu_id.getText().toString());
                        }
                    }).create();
            alertDialog.show();

        }
    }

    //根据学生id删除
    public void delStudentByStuId(String id){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.DELSTU_BY_STUID+"/"+id,arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //删除成功
                findAllStudent();
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }else {
                //删除失败
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

    //模糊查询
    public void findStuBySearchContent(){
        String[] arg = null;
        String backMsg = GETNetService.RequestGet(ConfigCenter.FIND_STU_BY_SEARCH_CONTENT
                +"/"+searchContent.getText().toString(),arg);
        if(!backMsg.equals(ConfigCenter.Server_ERR)){
            Result result = JsonUtis.jsonToPojo(backMsg, Result.class);
            if(result.getCode() == 20000){
                //后台返回的json格式（内部是jackjson技术支持）的List需要进行转换，内部实现机制不一样
                String json = JSONArray.toJSONString(result.getData());
                students = JSON.parseArray(json,Student.class);
                adapter = new StuAdapter(this,students);
                adapter.notifyDataSetChanged();
                show_stu.setAdapter(adapter);
            }else {
                Toast.makeText(this,result.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,R.string.Fail_Register_HeadImage_Error,Toast.LENGTH_LONG).show();
        }
    }

}
