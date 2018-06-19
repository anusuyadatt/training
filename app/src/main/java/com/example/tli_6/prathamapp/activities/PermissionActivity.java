package com.example.tli_6.prathamapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PermissionActivity extends AppCompatActivity {
    final String t="PermissionActicity";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    boolean flag = true;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_permission);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
      //  findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        int cur_ver = android.os.Build.VERSION.SDK_INT;
        Log.d(t," version "+cur_ver);
        if (cur_ver <23)

        {
            //App.getInstance().createDirectory();
            boolean flag=false;
            Cursor cursor = AppDatabase.getUserDatabaseInstance().query(Constants.USER_TABLE, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
            flag=true;
            }
            if (cursor != null)
                cursor.close();
            if(flag)
            startActivity(new Intent(this,MainMenuActivity.class));
            else
            startActivity(new Intent(this,RegisterationActivity.class));
            finish();
        }

         else
            requestPermission();
    }
    private void requestPermission() {
        Log.d(t,"req per 1");
        // Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,
        String[] permissions = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_CONTACTS
        ,Manifest.permission.GET_ACCOUNTS};

        for (String pems : permissions) {
            Log.d(t,"req per 2");
            if (ContextCompat.checkSelfPermission(getApplicationContext(), pems) != PackageManager.PERMISSION_GRANTED)
                flag = false;
            Log.d(t," req flag  "+flag);
        }

        if (!flag)
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        else {
          //  App.getInstance().createDirectory();
            boolean flag=false;
            Cursor cursor = AppDatabase.getUserDatabaseInstance().query(Constants.USER_TABLE, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                flag=true;
            }
            Log.d(t," cursor flag "+flag);
            if (cursor != null)
                cursor.close();
            if(flag)
                startActivity(new Intent(this,MainMenuActivity.class));
            else
                startActivity(new Intent(this,RegisterationActivity.class));
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        flag = true;


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int grant : grantResults) {
                    Log.d(t," total permission "+grantResults.length);
                    if (grantResults.length > 0 && grant == PackageManager.PERMISSION_GRANTED){
                        Log.d(t,"permission if "+grant);
                        flag=true;
                    }
                        /*Snackbar.make(this,"Permission Granted, Now this app can work smothly.",Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(this, "Permission Granted, Now this app can work smothly.", Toast.LENGTH_SHORT).show();*/
                   else {
                        Log.d(t,"permission else ");
                        flag = false;
                       break;
                      //  Toast.makeText(this, "Permission Denied .", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        if (flag) {
            Log.d(t,"activity calling flag "+flag);
            boolean flag=false;
            App.getInstance().createDirectory();
            Cursor cursor = AppDatabase.getUserDatabaseInstance().query(Constants.USER_TABLE, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                flag=true;
            }
            if (cursor != null)
                cursor.close();
            if(flag)
                startActivity(new Intent(this,MainMenuActivity.class));
            else
                startActivity(new Intent(this,RegisterationActivity.class));
            finish();
        } else {
          //  Toast.makeText(this, "Please accept permissions", Toast.LENGTH_LONG).show();
            requestPermission();
        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
