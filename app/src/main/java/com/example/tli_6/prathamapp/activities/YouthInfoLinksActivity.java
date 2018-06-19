package com.example.tli_6.prathamapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.adapter.DoorServeyLinksAdapter;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.model.MyDividerItemDecoration;
import com.example.tli_6.prathamapp.model.RecyclerTouchListener;

import java.util.ArrayList;

import static com.example.tli_6.prathamapp.application.App.appDatabase;

public class YouthInfoLinksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    private DoorServeyLinksAdapter doorServeyLinksAdapter;
    int i =1;
    int youth_position = 0;
    SQLiteDatabase appDatabase;
    int number_of_youths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youth_info_links);
        App globalVariable = (App)getApplicationContext();
        AppDatabase dbhelper = AppDatabase.getInstance(YouthInfoLinksActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ///////////////////////////Fetching Data //////////////////////////////
        Cursor packcur = appDatabase.rawQuery("Select * from pratham_app_form where UID= '"+String.valueOf(globalVariable.MY_UUID_SECURE)+"' and formID = 2 and formCompletionStatus = 0  ", null);
        if(packcur.getCount()>0){
            while (packcur.moveToNext()) {
                number_of_youths = packcur.getInt(packcur.getColumnIndex("numOfYouths"));

            }
        }
        ///////////////////////////Fetching Data //////////////////////////////
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL, 21));
        for(i =1;i<= number_of_youths;i++){ arrayList.add("Add Youth Information_"+i); }

        doorServeyLinksAdapter = new DoorServeyLinksAdapter(arrayList,youth_position);
        recyclerView.setAdapter(doorServeyLinksAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                App globalVariable = (App)getApplicationContext();
              //  Toast.makeText(YouthInfoLinksActivity.this, "pos"+arrayList.get(position), Toast.LENGTH_LONG).show();
                Log.d("test--", "globalVariablesubmitted_forms" +  globalVariable.submitted_forms);
                Log.d("test--", "globalVariablenumberOfYouths" +  number_of_youths);

                if(globalVariable.submitted_forms==0&&position>0){
                    Toast.makeText(YouthInfoLinksActivity.this, "Please First Add/Update First Youth Information", Toast.LENGTH_LONG).show();
                    return ;
                }

                Intent intent = new Intent(YouthInfoLinksActivity.this,YouthInformationActivity.class);
                intent.putExtra("youth_position",position);
                intent.putExtra("number_of_youths",number_of_youths);
                intent.putExtra("DoorformData",getIntent().getStringExtra("DoorformData"));

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
            alertDialog.setTitle("Confirm");
            alertDialog.setMessage("Do you want to close");

            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    finish();
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
        }
        return false;

    }

}
