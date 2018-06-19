package com.example.tli_6.prathamapp.utilities;


import android.database.Cursor;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.database.AppDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by tli-6 on 17/6/16.
 */
public class WebUtils{

    public static HttpURLConnection getHttpContext(URL url){

        HttpURLConnection conn=null;
        try {

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
          /*  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

           */
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (!url.getHost().equals(conn.getURL().getHost())){

            }

        }
        catch (IOException io) {

            return null;
        }
        return conn;
    }
    public static String convertStmapToDate(String val,String format){
        String date="";
        try {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            Long time=Long.parseLong(val);
            calendar.setTimeInMillis(time);
           date= android.text.format.DateFormat.format(format,calendar).toString();
        }
        catch (NumberFormatException ne){
            date="number format exception";
        }
        return date;
    }
    public static  String getString(InputStream in) {


        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(in));
        String line = "";
        String result = "";
        try {
            while ((line = bufferedReader.readLine()) != null)
                result += line;

        } catch (IOException io) {
            return null;
        } finally {
            if (in != null) {
                try {
                    // ensure stream is consumed...
                    final long count = 1024L;
                    while (in.skip(count) == count) ;
                    in.close();
                } catch (Exception e) {
                    return null;
                }

            }
        }
        return result;
    }
    public static String getDeviceId() {
        String deviceId = null;
        Cursor cursor = AppDatabase.getUserDatabaseInstance().query(Constants.USER_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            deviceId = cursor.getString(cursor.getColumnIndex("device_id"));
        }
        if (cursor != null)
            cursor.close();
        return deviceId;
    }
    public  static   String getTimeStamp() {
        Long stamp=  new Date().getTime();

        return stamp+"";
    }
    public static String getDate(String val){
        String date;
        date=convertStmapToDate(val,"yyyy-MM-dd");
        return date;
    }
    public  static String getMessageID(){
        String msgid;
        UUID uuid=UUID.randomUUID();
      msgid=  uuid.toString();
        return  msgid;
    }
}



