package com.company;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class XiaZai {

    static OkHttpClient client = new OkHttpClient();

    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try ( Response response = client.newCall(request).execute() ) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {

        String response = run("https://image.baidu.com/search/acjson?tn=resultjson_com&catename=" +
                "pcindexhot&ipn=rj&ct=201326592&is=&fp=result&queryWord=&cl=2&lm=-1&ie=utf-8&oe=utf-" +
                "8&adpicid=&st=-1&z=&ic=0&word=pcindexhot&face=0&istype=2&qc=&nc=1&fr=&pn=0&rn=30");

        JSONObject jsStr = JSONObject.fromObject(response);

        JSONArray data = jsStr.getJSONArray("data");

        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            System.out.println("i = " + i);
            if (jsonObject.has("thumbURL")){
                String thumbURL = jsonObject.getString("thumbURL");
                System.out.println("thumbURL = " + thumbURL);
            }

            if (jsonObject.has("middleURL")){
                String middleURL = jsonObject.getString("middleURL");
                System.out.println("middleURL = " + middleURL);
                downLoadFromUrl(middleURL,i+".jpg", "D:/HCUser/Pictures/2017-12-7");
            }

            if (jsonObject.has("hoverURL")){
                String hoverURL = jsonObject.getString("hoverURL");
                System.out.println("hoverURL = " + hoverURL);
            }

            JSONObject userData = jsonObject.optJSONObject("userData");
            if (userData!=null) {
                String barUrl = userData.optString("BarUrl");
                System.out.println("barUrl = " + barUrl);
            }
            if (userData!=null) {
                String BarThumbnailUrl = userData.optString("BarThumbnailUrl");
                System.out.println("BarThumbnailUrl = " + BarThumbnailUrl);
            }



        }

    }



    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }

//        System.out.println("info:"+url+" download success");
    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
