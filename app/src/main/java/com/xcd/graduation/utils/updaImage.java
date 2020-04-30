package com.xcd.graduation.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class updaImage {

    /**
     * 将Bitmap转化为byte[]型，并用base64加密
     * @param upbitmap
     * @return
     */
    public static String updata(Bitmap upbitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        upbitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] b = stream.toByteArray();
        /*
         * 将图片流以字符串形式存储下来,base64coder是一个常用的编码类， 经常用于各种网络数据传输时用来加密和解密
         * 用，用来确保数据的唯一性
         */
        String file = Base64Util.encode(b);
        return file;
    }

    /**
     * byte[]转Bitmap
     * @param b
     * @return
     */
    public static Bitmap BytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    /**
     * 剪切图片
     * @param bgimage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

}
