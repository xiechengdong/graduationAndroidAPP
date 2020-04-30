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
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.updaImage;

import java.util.List;

public class BookAdapter extends BaseAdapter {

    private Context context;
    private List<Book> books;

    public BookAdapter(Context context, List<Book> book1s) {
        this.context = context;
        this.books = book1s;
    }

    @Override
    public int getCount() {
        return books.size();
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
        //1.填充视图
        View view = View.inflate(context, R.layout.show_books,null);

        //1.使用视图对象通过id查找控件
        ImageView cover = view.findViewById(R.id.book_face);
        TextView name = view.findViewById(R.id.book_name);
        TextView teacher_name = view.findViewById(R.id.book_teach_name);
        TextView id = view.findViewById(R.id.book_id);

        //获取数据值
        Book book1 = books.get(position);

        //把数据展示在控件中
        if(book1.getCover() != null){
            //设置头像
            byte[] cover1 = null;
            try {
                //将String转为byte[]
                cover1 = Base64Util.decode(book1.getCover());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将byte[]转为Bitmap
            Bitmap bitmap =  updaImage.BytesToBimap(cover1);
            cover.setImageBitmap(bitmap);
        }
        name.setText(book1.getName());
        teacher_name.setText(book1.getTeacher_name());
        id.setText(book1.getId());

        //返回视图
        return view;
    }
}
