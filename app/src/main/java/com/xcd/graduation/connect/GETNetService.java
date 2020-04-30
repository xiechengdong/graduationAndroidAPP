package com.xcd.graduation.connect;

import android.util.Log;

import com.xcd.graduation.config.ConfigCenter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GETNetService {

    public static String RequestGet(String PathUrl,String ...kvs){
        if(kvs !=null){
            //获取参数
            StringBuffer paramsStr = new StringBuffer();
            for (int i = 0;i<kvs.length;i+=2){
                paramsStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
            }
            PathUrl = PathUrl+"?"+paramsStr.toString();
        }

        Log.i("URL:",PathUrl);

        //启动线程执行网络连接
        GetConnect getConnect = new GetConnect(PathUrl);
        Thread thread = new Thread(getConnect);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getConnect.getResult();
    }
}

class GetConnect implements Runnable{

    private String result; //服务器的返回值
    private String PathUrl;

    public GetConnect(String pathUrl) {
        this.PathUrl = pathUrl;
    }

    @Override
    public void run() {
        try{
            //新建一个URL对象
            URL url = new URL(PathUrl);
            //打开一个HttpURlConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //设置超时时间
            urlConn.setConnectTimeout(3*1000);
            //执行连接操作
            urlConn.connect();
            //若请求成功，通过读取返回的数据流来获取返回的数据
            if(urlConn.getResponseCode() == 200) {
                //读取服务器返回的内容
                BufferedInputStream bis = new BufferedInputStream(urlConn.getInputStream());
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
        }catch (Exception e){
            e.printStackTrace();
            result = ConfigCenter.Server_ERR;
        }
    }

    //获取返回值的方法
    public String getResult() {
        return result;
    }
}