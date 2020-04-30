package com.xcd.graduation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xcd.graduation.bean.Guanggao;
import com.xcd.graduation.utils.Base64Util;
import com.xcd.graduation.utils.updaImage;

import java.util.List;

/**
 * 轮播图适配器
 */
public class FlipperAdapter extends BaseAdapter {

    private List<Guanggao> guanggaos;
    private Context context;

    public FlipperAdapter(List<Guanggao> guanggaos,Context context){
        this.guanggaos = guanggaos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return guanggaos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建一个ImageView
        ImageView imageView = new ImageView(context);
        //imageView.setBackgroundResource(images[position]);
        Guanggao guanggao = guanggaos.get(position);
        if(guanggao.getContent() != null ){
            imageView.setImageBitmap(change(guanggao.getContent()));
        }
        //设置ImageView的缩放类型
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //为ImageView设置布局参数
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    //将bit转化为bitmap
    public Bitmap change(String content){
        byte[] cover1 = null;
        try {
            //将String转为byte[]
            cover1 = Base64Util.decode(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将byte[]转为Bitmap
        Bitmap bitmap =  updaImage.BytesToBimap(cover1);
        return bitmap;
    }
}
