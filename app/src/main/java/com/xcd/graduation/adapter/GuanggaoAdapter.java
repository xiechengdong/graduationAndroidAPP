package com.xcd.graduation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcd.graduation.R;
import com.xcd.graduation.bean.Guanggao;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.updaImage;

import java.util.List;

public class GuanggaoAdapter extends BaseAdapter {

    private List<Guanggao> guanggaos;
    private Context context;

    public GuanggaoAdapter( Context context,List<Guanggao> guanggaos) {
        this.guanggaos = guanggaos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return guanggaos.size();
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
        View view = View.inflate(context, R.layout.show_guanggao,null);

        //获取组件
        ImageView content = view.findViewById(R.id.Admin_guanggao_face);
        TextView name = view.findViewById(R.id.Admin_guanggao_name);
        TextView start = view.findViewById(R.id.Admin_guanggao_start);
        TextView end = view.findViewById(R.id.Admin_guanggao_end);
        TextView id = view.findViewById(R.id.Admin_guanggao_id);

        //获取对象
        Guanggao guanggao = guanggaos.get(position);

        //填充组件
        if(guanggao.getContent() != null){
            //设置头像
            byte[] cover1 = null;
            try {
                //将String转为byte[]
                cover1 = Base64Util.decode(guanggao.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将byte[]转为Bitmap
            Bitmap bitmap =  updaImage.BytesToBimap(cover1);
            content.setImageBitmap(bitmap);
        }
        name.setText(guanggao.getName());
        start.setText(guanggao.getStart());
        end.setText(guanggao.getOver());
        id.setText(guanggao.getId());

        //返回视图
        return view;
    }
}
