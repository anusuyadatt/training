package com.example.tli_6.prathamapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.utilities.WebUtils;

import java.util.Date;

public class MainMenuActivity extends AppCompatActivity {
    Button  mobActButton, studentButton, mobHisButton, studentHisButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        App.getInstance().createDirectory();
        mobActButton = (Button) findViewById(R.id.mob_act_butt);
        mobActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailyActivity();
            }
        });
        studentButton = (Button) findViewById(R.id.student_act_butt);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentActivity();
            }
        });
        mobHisButton = (Button) findViewById(R.id.mob_his_butt);
        mobHisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobHistory();
            }
        });
        studentHisButton = (Button) findViewById(R.id.std_his_butt);
        studentHisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentHistory();
            }
        });

    }

    @Override
    protected void onResume() {
        App.getInstance().createDirectory();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void dailyActivity() {
        Intent intent = new Intent(this, DailyActivity.class);
        startActivity(intent);

    }

    void studentActivity() {
        Intent intent = new Intent(this, StudentDetailsActivity.class);
        startActivity(intent);
    }

    void mobHistory() {
        Intent intent = new Intent(this, MobiliserHistoryActivity.class);
        startActivity(intent);
    }

    void studentHistory() {
        Intent intent = new Intent(this, StudentHistoryActivity.class);
        startActivity(intent);
    }
}
