package com.example.tli_6.prathamapp.activities;

import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.adapter.MobiliserAdapter;
import com.example.tli_6.prathamapp.adapter.StudentAdapter;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.model.DailyActivityModel;
import com.example.tli_6.prathamapp.model.StudentModel;
import com.example.tli_6.prathamapp.model.UserModel;
import com.example.tli_6.prathamapp.utilities.WebUtils;

import java.util.ArrayList;

public class MobiliserHistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
ListView listView;
    Button prvs_butt, next_butt;
    Spinner date_spnr;
    int current_date_pos;
    ArrayAdapter arrayAdapter;
    int count = 0;
    ArrayList<DailyActivityModel> list;
    MobiliserAdapter mobiliserAdapter;
    ArrayList<String> nonModeifiedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobiliser_history);
        App.getInstance().createDirectory();
        nonModeifiedList = new ArrayList<String>();
        listView=(ListView)findViewById(R.id.listview);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name)+" : "+getString(R.string.mob_hostory_title));

        prvs_butt = (Button) findViewById(R.id.prvs_butt);
        next_butt = (Button) findViewById(R.id.next_butt);
        date_spnr = (Spinner) findViewById(R.id.date_spr);


        next_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        prvs_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous();
            }
        });
        populateNonModeifiedList();
        adjustScreen();

        arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, nonModeifiedList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        date_spnr.setAdapter(arrayAdapter);
        date_spnr.setOnItemSelectedListener(this);

         list = getAdadterData(0);
         mobiliserAdapter=new MobiliserAdapter(this,R.layout.adapter_layout,list);
        listView.setAdapter(mobiliserAdapter);
    }
    ArrayList<DailyActivityModel> getAdadterData(int pos){
        ArrayList<DailyActivityModel> models=new ArrayList<DailyActivityModel>();
        if(nonModeifiedList.size()>0) {
            String where = "activtity_date=?";
            String[] args = {nonModeifiedList.get(pos)};

            Cursor cursor = AppDatabase.getDatabaseInstance().query(Constants.MOBILISER_DAILY_ACTIVITY_TABLE, null,where,args, null, null, "activity_datetime DESC");
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DailyActivityModel model = new DailyActivityModel();
                    model.setVillage(cursor.getString(cursor.getColumnIndex("village_name")));
                    model.setStudentsRached(cursor.getString(cursor.getColumnIndex("students_reached")));
                    model.setStudentsAsessed(cursor.getString(cursor.getColumnIndex("students_assessed")));
                    model.setStudentsReadyToJoin(cursor.getString(cursor.getColumnIndex("students_ready_to_join")));
                    model.setOriginalSender(cursor.getString(cursor.getColumnIndex("sender")));
                    String tmp = cursor.getString(cursor.getColumnIndex("activity_datetime"));
                    model.setMobileTimestamp(WebUtils.convertStmapToDate(tmp, "yyyy-MM-dd hh:mm"));
                    model.setOriginalServerResposne(cursor.getString(cursor.getColumnIndex("server_response")));
                    models.add(model);

                }

            }
            if (cursor != null)
                cursor.close();
        }
        return  models;
    }
    void adjustScreen(){
        if(nonModeifiedList.size()>0) {
            prvs_butt.setVisibility(View.VISIBLE);
            next_butt.setVisibility(View.VISIBLE);
            date_spnr.setVisibility(View.VISIBLE);

        }
    }
    void next(){
        if(nonModeifiedList.size()>0&&count<nonModeifiedList.size()-1){
            count++;
            resetScreen(count);
        }
    }
    void previous(){
        if(nonModeifiedList.size()>0&&count>0){
            count--;
            resetScreen(count);
        }
    }
    public void populateNonModeifiedList() {
        String[] colums = {"activtity_date"};

        Cursor cursor = AppDatabase.getDatabaseInstance().query(true, Constants.MOBILISER_DAILY_ACTIVITY_TABLE, colums, null, null, null, null, null,null);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext())
                nonModeifiedList.add(cursor.getString(0));
        }
        if (cursor != null)
            cursor.close();

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        current_date_pos = pos;
        count=pos;

        resetScreen(current_date_pos);

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    void resetScreen(int pos) {
        list.clear();
        if (nonModeifiedList.size() > 0) {

            list = getAdadterData(pos);

           mobiliserAdapter.refreshList(list);
            // ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }
}
