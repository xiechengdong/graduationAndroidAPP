package com.xcd.graduation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcd.graduation.R;
import com.xcd.graduation.bean.Teacher;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.updaImage;

import java.util.List;

public class TeacherAdapter extends BaseAdapter {

    private Context context;
    private List<Teacher> teachers;

    public TeacherAdapter(Context context, List<Teacher> teachers) {
        this.context = context;
        this.teachers = teachers;
    }

    @Override
    public int getCount() {
        return teachers.size();
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
        Teacher teacher = teachers.get(position);

        //填充数据
        //把数据展示在控件中
        if(teacher.getHeadimage() != null){
            //设置头像
            byte[] cover1 = null;
            try {
                //将String转为byte[]
                cover1 = Base64Util.decode(teacher.getHeadimage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将byte[]转为Bitmap
            Bitmap bitmap =  updaImage.BytesToBimap(cover1);
            headImage.setImageBitmap(bitmap);
        }
        number.setText(teacher.getNumber()+"");
        name.setText(teacher.getName());
        id.setText(teacher.getId());

        //返回视图
        return view;
    }
}
