package com.example.hyc.movieshow.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class IOUtils {

        /** 关闭流 */
        public static boolean close(Closeable io) {
            if (io != null) {
                try {
                    io.close();
                } catch (IOException e) {
                    //LogUtils.e(e);
                }
            }
            return true;
        }

    /**
     * 根据url获取bitmap
     * @param u
     * @return
     */
    public static Bitmap getBitmap(String u) {
        Bitmap mBitmap = null;
        try {
            java.net.URL url;
            url = new java.net.URL(u);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            InputStream is;
            is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }
}