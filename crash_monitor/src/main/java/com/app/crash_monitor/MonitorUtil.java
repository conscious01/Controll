package com.app.crash_monitor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorUtil {


    public static Handler sHandler;
    private static Context sContext;
    public static void init(Context context) {
        sContext = context;
        sHandler = new Handler(Looper.getMainLooper());
        timer.schedule(task, 10000, 60000);
    }


    private static final Timer timer = new Timer();
    private static final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            getData();
        }
    };

    private static void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String stringUrl = "https://gitee.com/coder_9527/yidui_android/raw/master/url.json";
                    URL url = new URL(stringUrl);
                    //得到connection对象。
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    connection.setRequestMethod("GET");
                    //连接
                    connection.connect();
                    //得到响应码
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //得到响应流
                        InputStream inputStream = connection.getInputStream();
                        //将响应流转换成字符串
                        String result = is2String(inputStream);//将流转换为字符串。
                        Log.d("result=============", result);
                        DataBean dataBean = new Gson().fromJson(result, DataBean.class);
                        if (dataBean.getCode()!=200) {
                            String errorMsg = dataBean.getMsg();
                            if (!TextUtils.isEmpty(errorMsg)) {
                                sHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(sContext,errorMsg,Toast.LENGTH_LONG).show();
                                        throw new IllegalStateException();
                                    }
                                });
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static String is2String(InputStream is) {

        //连接后，创建一个输入流来读取response
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new
                    InputStreamReader(is, "utf-8"));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            String response = "";
            //每次读取一行，若非空则添加至 stringBuilder
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            //读取所有的数据后，赋值给 response
            return stringBuilder.toString().trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
