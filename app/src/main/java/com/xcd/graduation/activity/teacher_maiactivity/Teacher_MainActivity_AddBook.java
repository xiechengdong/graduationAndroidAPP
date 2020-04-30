package com.xcd.graduation.activity.teacher_maiactivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xcd.graduation.R;
import com.xcd.graduation.adapter.BookAdapter;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Result;
import com.xcd.graduation.config.ConfigCenter;
import com.xcd.graduation.connect.NetService;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.FileUtils;
import com.xcd.graduation.utils.JsonUtis;
import com.xcd.graduation.utils.updaImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.xcd.graduation.utils.FileUtils.fileConvertToByteArray;

public class Teacher_MainActivity_AddBook extends AppCompatActivity {

    private ImageView bookFace;
    private EditText bookContentPath;
    private SharedPreferences sharedPreferences;
    private Book book;
    private String fileUrl;
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main__add_book);

        //初始化控件
        bookFace = findViewById(R.id.Teacher_addBook_bookFace);
        bookContentPath = findViewById(R.id.Teacher_addBook_bookContent);
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        book = new Book();
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.Teacher_addBook_back:
                Intent intent = new Intent(this,Teacher_MainActivity_MyBooks.class);
                startActivity(intent);
                finish();
                break;
            //选择课程封面
            case R.id.Teacher_addBook_bookFace:
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                break;
            //选择课程内容
            case R.id.Teacher_addBook_bt_path:
                Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
                intent3.setType("application/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent3.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent3, 2);
                break;
            //阅览课程内容
            case R.id.Teacher_addBook_previewBookContent:
                if (fileUrl != null) {
                    try {
                        File file = new File(fileUrl);
                        FileUtils.openFile(file,this);
                    } catch (Exception e) {
                        //没有安装第三方的软件会提示
                        Toast toast = Toast.makeText(this, "没有找到打开该文件的应用程序", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast.makeText(this, "请选择或输入文件路径", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            //添加课程
            case R.id.Teacher_addBook_add_book:
                if(bookContentPath.getText().toString().equals("")){
                    Toast.makeText(this,"路径为空",Toast.LENGTH_LONG).show();
                    return;
                }
                //将课程信息上传至服务器
                addBook();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        点击取消结束程序
         */
        if(data == null){
            Toast.makeText(this,R.string.cancle,Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode){
            case 1:
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = updaImage.zoomImage(bitmap,250,250);
                bookFace.setImageBitmap(bitmap);
                String cover = updaImage.updata(bitmap);
                book.setCover(cover);
                break;
            case 2:
                Uri uri1 = data.getData();
                //获取文件的绝对路径
                String path = FileUtils.getPath(this,uri1);

                //将文件转换为字节数组
                File file =  new File(path);

                //将文件转换为字节数组
                bytes = FileUtils.fileConvertToByteArray(file);
                String file1 =  Base64Util.encode(bytes);
                //将字节数组放入book对象
                book.setContent(file1);

                //获取课程名称
                String[] arg = file.getName().split("\\.");
                //将路径文件名称显示在控件
                bookContentPath.setText(arg[0]);
                book.setName(arg[0]);

                fileUrl = uri1.getPath();
                break;
        }
    }

    //将课程长传到服务器
    public void addBook() {
        //组装JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", book.getName());
            jsonObject.put("cover", book.getCover());
            jsonObject.put("content", book.getContent());
            jsonObject.put("teacher_name", sharedPreferences.getString("name", null));
            jsonObject.put("teacher_id", sharedPreferences.getString("id", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String backMsg = NetService.RequestPost(ConfigCenter.TEACHER_ADD_BOOK, jsonObject);
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

}
