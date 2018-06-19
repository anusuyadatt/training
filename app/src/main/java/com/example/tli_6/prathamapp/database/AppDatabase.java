package com.example.tli_6.prathamapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tli_6.prathamapp.application.App;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tli-6 on 5/17/17.
 */
public class AppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase databaseInsatnce;
    private static SQLiteDatabase userdb;
    private Context context;
    private static AppDatabase db;
    boolean flag=true;
    final  String t="AppDatabase";
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(t,"oncreate() calling");
        if (flag) {
            sqLiteDatabase.execSQL("create table if not exists user_login_log(auto_id integer primary key autoincrement,mobilizer_code  STRING,mobilizer_number STRING,current_latitude DOUBLE ,current_longitude DOUBLE,current_login_time STRING,status STRING,syncStatus INTEGER )");
            sqLiteDatabase.execSQL("create table if not exists user_login(user_login_id integer primary key autoincrement,mobilizer_code  STRING,mobilizer_number STRING,villageName  STRING,UID STRING,app_id STRING,imei_no STRING,os_type STRING,manufacturer_name STRING,os_version STRING,firebase_token STRING,app_version STRING,confirm_overwrite STRING," +
                    "status STRING,message STRING,mobilizer_name STRING,coordinator STRING,unit STRING,vertical STRING,mobilizer_state STRING,center STRING,location STRING" +
                    ",appointment_date STRING,position STRING,final_status STRING,resignation_date STRING)");
            sqLiteDatabase.execSQL("create table if not exists pratham_app_form(auto_id integer primary key autoincrement,formID INTEGER,formVersion INTEGER,UID STRING,syncStatus INTEGER,villageName STRING,familyOwner STRING,numOfYouths INTEGER,formCompletionStatus INTEGER,formData STRING ,youth_submitted_forms INTEGER)");

        }
        else
            userdb.execSQL("create table if not exists mobiliser_details(ID integer primary key autoincrement,mobiliser_code varchar(20),mobiliser_name varchar(30),mobile_no varchar(12),unit varchar(20),sub_unit varchar(20),state varchar(20),mobilisation_center text,location text,coordinator varchar(20),position varchar(20),appointment_date datetime,registeration_date datetime,device_id text)");

    }
    public static SQLiteDatabase getDatabaseInstance() {
        return databaseInsatnce;
    }

    public AppDatabase(Context context) {
        //  super(context, name, factory, version);
        //super(context, Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }
    public static AppDatabase getInstance(Context context) {
        if (db == null)
            db = new AppDatabase(context);
        return (db);
    }

   /* public AppDatabase() {


        super(App.getInstance(), DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(t,"constructor calling");
        databaseInsatnce = SQLiteDatabase.openOrCreateDatabase(App.APP_ROOT + File.separator + DATABASE_NAME, null);
        userdb=SQLiteDatabase.openOrCreateDatabase(App.cachePath+File.separator+DATABASE_NAME,null);
        onCreate(databaseInsatnce);
        flag=false;
        onCreate(userdb);

    }*/
    public AppDatabase() {


        super(App.getInstance(), DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(t,"constructor calling");
        databaseInsatnce = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
        userdb=SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME,null);
        onCreate(databaseInsatnce);
        flag=false;
        onCreate(userdb);

    }

    public static SQLiteDatabase getUserDatabaseInstance() {
        return userdb;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

