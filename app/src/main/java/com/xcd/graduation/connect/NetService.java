package com.xcd.graduation.connect;

import android.util.Log;

import com.xcd.graduation.config.ConfigCenter;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class   NetService {

    public static String RequestPost(String PathUrl,JSONObject jsonObject){
        Log.i("URL:",PathUrl);
        Connect connect = new Connect(PathUrl,jsonObject);
        Thread thread = new Thread(connect);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return connect.getResult();
    }
}

class Connect implements Runnable{

    private String result; //服务器的返回值
    private String PathUrl;
    private JSONObject jsonObject;

    public Connect(String pathUrl, JSONObject jsonObject) {
        this.PathUrl = pathUrl;
        this.jsonObject = jsonObject;
    }

    @Override
    public void run() {
        try {
            //新建一个URL
            URL url = new URL(PathUrl);
            //打开一个HttpURlConnection连接
            final HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
            //设置超时时间
            urlconn.setConnectTimeout(3*1000);
            //设置允许输出
            urlconn.setDoOutput(true);
            //设置不能缓存
            urlconn.setUseCaches(false);
            //设置请求为Post
            urlconn.setRequestMethod("POST");
            urlconn.setInstanceFollowRedirects(true);
            urlconn.setRequestProperty("Content-Type","application/json");
            //执行连接操作
            urlconn.connect();
            //发送请求操作
            Log.i("sendmsg",String.valueOf(jsonObject));
            BufferedOutputStream dos = new BufferedOutputStream(urlconn.getOutputStream());
            dos.write(String.valueOf(jsonObject).getBytes());
            dos.flush();
            dos.close();
            //若请求成功，通过读取返回的数据流来获取返回的数据
            if(urlconn.getResponseCode() == 200) {
                //读取服务器返回的内容
                BufferedInputStream bis = new BufferedInputStream(urlconn.getInputStream());
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = bis.read(buf)) != -1) {
                    bout.write(buf, 0, len);
                }
                bis.close();
                Log.i("backmsg", bout.toString());
                result = bout.toString();
            }else {
                result = ConfigCenter.Server_ERR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ConfigCenter.Server_ERR;
        }
    }

    //获取返回值的方法
    public String getResult() {
        return result;
    }
}