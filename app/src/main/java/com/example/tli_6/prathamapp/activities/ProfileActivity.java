package com.example.tli_6.prathamapp.activities;

import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;

public class ProfileActivity extends AppCompatActivity {
    TextView name_txt, number_txt, unit_txt, sub_unit_txt, state_txt, center_txt, location_txt;
    TextView coordinator_txt, position_txt, apponit_date_txt, reg_date_txt;
    String name = "", number = "", unit = "", sub_unit = "", state = "", center = "", loc = "", coordinator = "", pos = "", appoin_date = "", reg_date = "";
TextView reg_tit_txt,appoint_tit_txt,pos_tit_txt,coor_tit_txt,loc_tit_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        App.getInstance().createDirectory();
        name_txt = (TextView) findViewById(R.id.name_txt);
        number_txt = (TextView) findViewById(R.id.mobile_number_txt);
        unit_txt = (TextView) findViewById(R.id.unit_txt);
        sub_unit_txt = (TextView) findViewById(R.id.sub_unit_txt);
        state_txt = (TextView) findViewById(R.id.state_txt);
        center_txt = (TextView) findViewById(R.id.mob_center_txt);
        location_txt = (TextView) findViewById(R.id.loc_txt);
        coordinator_txt = (TextView) findViewById(R.id.coordinator_txt);
        position_txt = (TextView) findViewById(R.id.position_txt);
        apponit_date_txt = (TextView) findViewById(R.id.app_date_txt);
        reg_date_txt = (TextView) findViewById(R.id.reg_date_txt);

        reg_tit_txt=(TextView)findViewById(R.id.reg_tit);
        appoint_tit_txt=(TextView)findViewById(R.id.appoint_tit);
        pos_tit_txt=(TextView)findViewById(R.id.pos_tit);
        coor_tit_txt=(TextView)findViewById(R.id.coord_tit);
        loc_tit_txt=(TextView)findViewById(R.id.loc_tit);


        populateView();
        ActionBar actionBar=getSupportActionBar();

        actionBar.setTitle(getString(R.string.app_name)+" : "+getString(R.string.profile));
    }

    void populateView() {
        Cursor cursor = AppDatabase.getUserDatabaseInstance().query(Constants.USER_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex("mobiliser_name"));
            if (name == null) {
                name_txt.setVisibility(View.GONE);
                name="";
            }
            number = cursor.getString(cursor.getColumnIndex("mobile_no"));
            if (number == null) {
                number_txt.setVisibility(View.GONE);
                number="";
            }
            unit = cursor.getString(cursor.getColumnIndex("unit"));
            if (unit == null) {
                unit_txt.setVisibility(View.GONE);
                unit="";
            }
            sub_unit = cursor.getString(cursor.getColumnIndex("sub_unit"));
            if (sub_unit == null) {
                sub_unit_txt.setVisibility(View.GONE);
                sub_unit="";
            }
            state = cursor.getString(cursor.getColumnIndex("state"));
            if (state == null) {
                state_txt.setVisibility(View.GONE);
                state="";
            }
            center = cursor.getString(cursor.getColumnIndex("mobilisation_center"));
            if (center == null) {
                center_txt.setVisibility(View.GONE);
                center="";
            }
            coordinator = cursor.getString(cursor.getColumnIndex("coordinator"));
            if (coordinator == null) {
                coordinator_txt.setVisibility(View.GONE);
                coor_tit_txt.setVisibility(View.GONE);
                coordinator="";
            }
            pos = cursor.getString(cursor.getColumnIndex("position"));
            if (pos == null){
                position_txt.setVisibility(View.GONE);
                pos_tit_txt.setVisibility(View.GONE);
            pos="";
            }
            appoin_date = cursor.getString(cursor.getColumnIndex("appointment_date"));

            if (appoin_date == null||(appoin_date!=null&&appoin_date.startsWith("0000"))) {
                apponit_date_txt.setVisibility(View.GONE);
                appoint_tit_txt.setVisibility(View.GONE);
                appoin_date="";
            }
            reg_date = cursor.getString(cursor.getColumnIndex("registeration_date"));

            if (reg_date == null||(reg_date!=null&&reg_date.startsWith("0000"))) {
                reg_date_txt.setVisibility(View.GONE);
                reg_tit_txt.setVisibility(View.GONE);
                reg_date="";
            }
            loc=cursor.getString(cursor.getColumnIndex("location"));
            if(loc==null) {
                location_txt.setVisibility(View.GONE);
                loc_tit_txt.setVisibility(View.GONE);
                loc="";
            }
        }
        if (cursor != null)
            cursor.close();
        name_txt.setText(name);
        number_txt.setText(number);
        unit_txt.setText(unit);
        sub_unit_txt.setText(sub_unit);
        state_txt.setText(state);
        center_txt.setText(center);
        coordinator_txt.setText(coordinator);
        position_txt.setText(pos);
        apponit_date_txt.setText(appoin_date);
        reg_date_txt.setText(reg_date);
        location_txt.setText(loc);
    }
}
