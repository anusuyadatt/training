package com.example.tli_6.prathamapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.adapter.DoorToDoorAdapter;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.model.DoorToDoorModel;
import com.example.tli_6.prathamapp.model.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.example.tli_6.prathamapp.application.App.appDatabase;

public class DoorToDoorServeyDetail extends AppCompatActivity {
    Button fab;
    private RecyclerView recyclerView;
    private List<DoorToDoorModel> doorList = new ArrayList<>();
    private DoorToDoorAdapter doorToDoorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_to_door_servey_detail);
        final App globalVariable = (App)getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (globalVariable.VillageName.equalsIgnoreCase("")) {
            toolbar.setTitle("VILLAGE : ABC ");
        }
        else {

            toolbar.setTitle("VILLAGE : " + globalVariable.VillageName);
        }

        setSupportActionBar(toolbar);
        fab = (Button) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        AppDatabase dbhelper = AppDatabase.getInstance(DoorToDoorServeyDetail.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoorToDoorServeyDetail.this,DoorToDoorServeyActivity.class);

                startActivity(intent);

            }
        });
        doorToDoorAdapter = new DoorToDoorAdapter(doorList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL, 21));
        fetchDoorData();
        recyclerView.setAdapter(doorToDoorAdapter);

    }

    public void fetchDoorData() {
        final App globalVariable = (App)getApplicationContext();
        doorList.clear();
        DoorToDoorModel doorToDoorModel = null;

        Cursor packcur = appDatabase.rawQuery("Select * from pratham_app_form where UID= '"+String.valueOf(globalVariable.MY_UUID_SECURE)+"' and formID = 2  ", null);
        while (packcur.moveToNext()){
            doorToDoorModel = new DoorToDoorModel();

            globalVariable.submitted_forms = packcur.getInt(packcur.getColumnIndex("youth_submitted_forms"));
            if(packcur.getString(packcur.getColumnIndex("numOfYouths")) != "0"){
                doorToDoorModel.setNumOfYouths(packcur.getString(packcur.getColumnIndex("numOfYouths")));
            }
            if(packcur.getString(packcur.getColumnIndex("familyOwner")).length()  > 0){
                doorToDoorModel.setFamilyOwnerName(packcur.getString(packcur.getColumnIndex("familyOwner")));
                doorList.add(doorToDoorModel);}

        }
        //  packcur.close();

    }


}
