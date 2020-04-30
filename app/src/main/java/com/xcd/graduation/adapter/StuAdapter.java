package com.xcd.graduation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcd.graduation.R;
import com.xcd.graduation.bean.Book;
import com.xcd.graduation.bean.Student;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.updaImage;

import java.util.List;

public class StuAdapter extends BaseAdapter {

    private Context context;
    private List<Student> students;

    public StuAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取视图
        View view = View.inflate(context, R.layout.show_student,null);
        //获取视图的控件
        ImageView headImage = view.findViewById(R.id.stu_face);
        TextView number = view.findViewById(R.id.stu_number);
        TextView name = view.findViewById(R.id.stu_name);
        TextView id = view.findViewById(R.id.stu_id);

        //获取数据值
        Student student = students.get(position);

        //填充数据
        //把数据展示在控件中
        if(student.getHeadimage() != null){
            //设置头像
            byte[] cover1 = null;
            try {
                //将String转为byte[]
                cover1 = Base64Util.decode(student.getHeadimage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将byte[]转为Bitmap
            Bitmap bitmap =  updaImage.BytesToBimap(cover1);
            headImage.setImageBitmap(bitmap);
        }
        number.setText(student.getNumber()+"");
        name.setText(student.getName());
        id.setText(student.getId());

        //返回视图
        return view;
    }
}
