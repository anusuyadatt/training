package com.example.tli_6.prathamapp.application;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.database.AppDatabase;

import java.io.File;
import java.util.UUID;

/**
 * Created by tli-6 on 5/17/17.
 */
public class App extends Application {
    static private App singleton;
    String resposneData;
    String otp;
    boolean flag=false;
    public  static String cachePath;
    public static final String APP_ROOT = Environment.getExternalStorageDirectory() + File.separator + "pratham";
    ConnectivityManager connectivityManager;
    public  static  String VillageName = "";
    public  static  int VillagePincode = 0;
    public  static  String   family_owner_name = "";
    public  static  int number_of_youths = 0;
    public  static  int door_transaction_id = 0;
    public  static UUID MY_UUID_SECURE ;;
    //  public  static UUID MY_UUID_SECURE = UUID.fromString(UUID.randomUUID().toString());;
    public  static  int completion_flag = 0;
    public  static   String imei_no = "", PhoneModel = "", AndroidVersion = "", version = "", deviceManufacturer = "",appID = "";
    public  static    double latitude = 0;
    public  static   double longitude = 0;
    public  static  String current_date;
    public  String mobilizer_code="",mobilizer_number="",mobilizerName = "";
    public    int submitted_forms = 0;
    public static   SQLiteDatabase appDatabase;

    @Override
    public void onCreate() {
        singleton = this;
        super.onCreate();
        cachePath=getExternalCacheDir().getAbsolutePath();
        connectivityManager = (ConnectivityManager) singleton.getSystemService(CONNECTIVITY_SERVICE);


    }

    public void createDirectory() throws RuntimeException {
        File f = new File(APP_ROOT);
        if (!f.exists()) {
            if (!f.mkdir()) {
                RuntimeException e = new RuntimeException("pratham directory could  not  be made");
                throw e;
            }
        } else {
            if (!f.isDirectory()) {
                RuntimeException e = new RuntimeException("existing pratham is not a directory");
                throw e;
            }
        }
        new AppDatabase();
    }

    public void setHttpResponseData(String data) {
        resposneData = data;
    }

    public String getResposneData() {
        return resposneData;
    }

    public static App getInstance() {
        return singleton;
    }

    public boolean checkConnectivity() {
        boolean isConnected = false;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            isConnected = true;
        return isConnected;
    }
    public void setOTP(String otp){
        this.otp=otp;
    }
    public String getOTP(){
        return  otp;
    }
    public String getMCode(){
        String code="";
        Cursor cursor= AppDatabase.getUserDatabaseInstance().query(Constants.USER_TABLE,null,null,null,null,null,null);
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            code=cursor.getString(cursor.getColumnIndex("mobiliser_code"));
        }
        if(cursor!=null)
            cursor.close();
        return  code;
    }
    public boolean isBackgroungProcessRunning(){
        return  flag;
    }
    public void setBckFlag(boolean f){
        flag=f;
    }

}
