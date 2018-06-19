package com.example.tli_6.prathamapp.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.interfaces.MyServiceListener;
import com.example.tli_6.prathamapp.listener.HttpCommunicationListener;
import com.example.tli_6.prathamapp.logic.HttpCommunicationLayer;
import com.example.tli_6.prathamapp.logic.ProcessResponse;
import com.example.tli_6.prathamapp.model.DailyActivityModel;
import com.example.tli_6.prathamapp.model.GPSTracker;
import com.example.tli_6.prathamapp.model.HttpResponseModel;
import com.example.tli_6.prathamapp.model.Model_TLI_API_Response;
import com.example.tli_6.prathamapp.service.Service_Call;
import com.example.tli_6.prathamapp.utilities.AppUtills;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.READ_CONTACTS;
import static com.example.tli_6.prathamapp.R.color.blueBackground;
import static com.example.tli_6.prathamapp.application.App.appDatabase;
import static com.example.tli_6.prathamapp.application.App.appID;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterationActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, HttpCommunicationListener {
    Calendar calander;
    SimpleDateFormat simpledateformat;


    ContentValues cv, cv1,cvLog;
    HttpCommunicationLayer httpCommunicationLayer;
    ProgressDialog mProgressDialog;
    final int REGISTER = 0;
    private String TAG = "RegisterationActivity";
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private Model_TLI_API_Response model_TLI_api_response;
    public static ProgressDialog progressDialog = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Button registeButton, submitButton;
    TextInputLayout textInputLayout, textInputLayout_code, mob_textlayout;
    BroadcastReceiver smsSentReciver, smsReceivedReceiver;
    BroadcastReceiver OTPReceived;
    EditText mobile_no_edt;
    GPSTracker gps;
    String PhoneModel = "", AndroidVersion = "", version = "", deviceManufacturer = "";
    long row_log = 0;
    int sync_status =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        final App globalVariable = (App) getApplicationContext();
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        globalVariable.current_date = simpledateformat.format(calander.getTime());
        AppDatabase dbhelper = AppDatabase.getInstance(RegisterationActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();

        mProgressDialog = new ProgressDialog(this);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.mcode);
        textInputLayout = (TextInputLayout) findViewById(R.id.pass_wid);
        textInputLayout_code = (TextInputLayout) findViewById(R.id.code_wid);
        mob_textlayout = (TextInputLayout) findViewById(R.id.mob_wid);
        progressDialog = new ProgressDialog(RegisterationActivity.this);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.otp);
        registeButton = (Button) findViewById(R.id.register_button);
        registeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
if(AppUtills.isNetworkAvailable(getApplicationContext())){
                register();}
                else {

    Toast.makeText(getApplicationContext(),"No Internet Available",Toast.LENGTH_LONG).show();
}

            }
        });
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        mobile_no_edt = (EditText) findViewById(R.id.mobile_no);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(getString(R.string.app_name) + " : " + getString(R.string.register_title));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
  // App.getInstance().createDirectory();


    }

    void register() {
        registeButton.setEnabled(false);
        final App globalVariable = (App) getApplicationContext();
        Log.d("test--", "  register" + globalVariable.imei_no);
        gps = new GPSTracker(RegisterationActivity.this);
        if (gps.canGetLocation()) {
            gps = new GPSTracker(RegisterationActivity.this);
            globalVariable.latitude = gps.getLatitude();
            globalVariable.longitude = gps.getLongitude();
            Log.d("test--", TAG + "latitudeIN" +  globalVariable.latitude + "longitudeIN" + globalVariable.longitude  );


        } else {
            Log.d("test--", TAG + "latitudeNone" +  globalVariable.latitude + "longitudeNone" + globalVariable.longitude  );
            gps.showSettingsAlert();
            registeButton.setEnabled(true);
            return;
        }



        try {
            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Please provide Telephone Permission.Then try Again.", Toast.LENGTH_LONG).show();
                registeButton.setEnabled(true);
                return;
            }

            globalVariable.imei_no = mngr.getDeviceId();
            Log.d("test--", "  globalVariable.imei_no1" + globalVariable.imei_no);
            PhoneModel = android.os.Build.MODEL;
            AndroidVersion = android.os.Build.VERSION.RELEASE;
            deviceManufacturer = android.os.Build.MANUFACTURER;

            PackageManager manager = getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            registeButton.setEnabled(true);
            Log.d("test--", "Exception in get package and version info--" + e);
            e.printStackTrace();
        }
        ///////////////////////////My CODE /////////////////////////////////
        final String mcode = mEmailView.getText().toString();
        final String mobile_no = mobile_no_edt.getText().toString();
        AppDatabase dbhelper = AppDatabase.getInstance(RegisterationActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        Cursor packcur =  appDatabase.rawQuery("Select  user_login_id from user_login where 1", null);

        Log.d("test--", " user_login-" + packcur.getCount());


        if (packcur.getCount() == 0) {
            Log.d("test--", " user_loginIF-" + packcur.getCount());
            //////////////////////////call Api /////////////////////////////////////////////

            final JSONObject jsonDataObj1 = new JSONObject();
            try {

                jsonDataObj1.put("mobilizer_code", mcode);
                jsonDataObj1.put("mobilizer_number", mobile_no_edt.getText().toString());
                jsonDataObj1.put("form_type", "0");
                Log.d("test--", "  globalVariable.imei_no2" + globalVariable.imei_no);
                jsonDataObj1.put("imei_no", globalVariable.imei_no);
                jsonDataObj1.put("os_type", "Android");
                jsonDataObj1.put("manufacturer_name", deviceManufacturer + " " + PhoneModel);
                jsonDataObj1.put("os_version", AndroidVersion);
                jsonDataObj1.put("firebase_token", "0");
                jsonDataObj1.put("app_version", version);
                jsonDataObj1.put("confirm_overwrite", "0");


            } catch (JSONException e) {
                registeButton.setEnabled(true);
                e.printStackTrace();
            }

            new Service_Call(getApplicationContext(), "/mobilizer/register", jsonDataObj1, true, new MyServiceListener() {
                @Override
                public void getServiceData(Model_TLI_API_Response api_response) {
                    String result = api_response.data;
                    model_TLI_api_response = api_response;

                    Log.d("test--", TAG + "--result data1 --" + result);

                    Log.d("test--", TAG + "--responsecode1" + model_TLI_api_response.response_code);
                    Log.d("test--", TAG + "--responsecode1HttpsURLConnection.HTTP_OK" + HttpsURLConnection.HTTP_OK);
                    try {
                        if (model_TLI_api_response.response_code == HttpsURLConnection.HTTP_OK) {

                            JSONObject jsonObject = null;
                            JSONObject jsonObjectProfile = null;
                            jsonObject = new JSONObject(result);


                            Log.d("test--", TAG + "--registration from data-status--" + jsonObject.getString("status"));
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                                jsonObjectProfile = jsonObject.getJSONObject("mobilizer_profile");
                                globalVariable.appID = jsonObject.getString("app_id");
                                Log.d("test--", "jsonObjectProfileOverrite" + jsonObjectProfile);
                                //////////////////inserting registration data/////////////////
                                cv1 = new ContentValues();
                                cv1.put("mobilizer_code", jsonObject.getString("mobilizer_code"));
                                cv1.put("mobilizer_number", jsonObject.getString("mobilizer_number"));
                                cv1.put("villageName", "");
                                cv1.put("UID", "");
                                cv1.put("app_id", jsonObject.getString("app_id"));
                                Log.d("test--", "  globalVariable.imei_no3" + jsonDataObj1.getString("imei_no"));
                                globalVariable.imei_no = jsonDataObj1.getString("imei_no");
                                Log.d("test--", "  globalVariable.imei_no5" + jsonDataObj1.getString("imei_no"));
                                cv1.put("imei_no", globalVariable.imei_no);
                                cv1.put("os_type", "Android");
                                cv1.put("manufacturer_name", deviceManufacturer + " " + PhoneModel);
                                cv1.put("os_version", AndroidVersion);
                                cv1.put("firebase_token", "0");
                                cv1.put("app_version", version);
                                cv1.put("message", jsonObject.getString("message"));
                                cv1.put("status", jsonObject.getString("status"));
                                cv1.put("mobilizer_name", jsonObjectProfile.getString("mobilizer_name"));
                                cv1.put("coordinator", jsonObjectProfile.getString("coordinator"));
                                cv1.put("unit", jsonObjectProfile.getString("unit"));
                                cv1.put("center", jsonObjectProfile.getString("center"));
                                cv1.put("vertical", jsonObjectProfile.getString("vertical"));
                                cv1.put("mobilizer_state", jsonObjectProfile.getString("mobilizer_state"));
                                cv1.put("location", jsonObjectProfile.getString("location"));
                                cv1.put("appointment_date", jsonObjectProfile.getString("appointment_date"));
                                cv1.put("position", jsonObjectProfile.getString("position"));
                                cv1.put("final_status", jsonObjectProfile.getString("final_status"));
                                cv1.put("resignation_date", jsonObjectProfile.getString("resignation_date"));
                                long row =       appDatabase.insert("user_login", null, cv1);

                                if (row > 0) {
                                    // Toast.makeText(getApplicationContext(), "Registered Successfully.", Toast.LENGTH_LONG).show();
                                    new MyAsncTask().execute();
                                    registeButton.setEnabled(true);
                                } else {
                                    registeButton.setEnabled(true);
                                    Toast.makeText(getApplicationContext(), "Not Registered Successfully.Please Try Again.", Toast.LENGTH_LONG).show();
                                }


                            }
                            else if  (jsonObject.getString("status").equalsIgnoreCase("0")){
                                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(RegisterationActivity.this);
                                alertDialog.setTitle("Confirm");
                                alertDialog.setMessage(jsonObject.getString("message"));
                                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
              ////////////////////////////////////////////////////////sending data/////////////////////

                                        final JSONObject jsonDataObj1 = new JSONObject();
                                        try {

                                            jsonDataObj1.put("mobilizer_code", mcode);
                                            jsonDataObj1.put("mobilizer_number", mobile_no_edt.getText().toString());
                                            jsonDataObj1.put("form_type", "0");
                                            Log.d("test--", "  globalVariable.imei_no2" + globalVariable.imei_no);
                                            jsonDataObj1.put("imei_no", globalVariable.imei_no);
                                            jsonDataObj1.put("os_type", "Android");
                                            jsonDataObj1.put("manufacturer_name", deviceManufacturer + " " + PhoneModel);
                                            jsonDataObj1.put("os_version", AndroidVersion);
                                            jsonDataObj1.put("firebase_token", "0");
                                            jsonDataObj1.put("app_version", version);
                                            jsonDataObj1.put("confirm_overwrite", "1");


                                        } catch (JSONException e) {
                                            registeButton.setEnabled(true);
                                            e.printStackTrace();
                                        }

                                        new Service_Call(getApplicationContext(), "/mobilizer/register", jsonDataObj1, true, new MyServiceListener() {
                                            @Override
                                            public void getServiceData(Model_TLI_API_Response api_response) {
                                                String result = api_response.data;
                                                model_TLI_api_response = api_response;

                                                Log.d("test--", TAG + "--result data1 --" + result);

                                                Log.d("test--", TAG + "--responsecode1" + model_TLI_api_response.response_code);
                                                Log.d("test--", TAG + "--responsecode1HttpsURLConnection.HTTP_OK" + HttpsURLConnection.HTTP_OK);
                                                try {
                                                    if (model_TLI_api_response.response_code == HttpsURLConnection.HTTP_OK) {

                                                        JSONObject jsonObject = null;
                                                        JSONObject jsonObjectProfile = null;

                                                        jsonObject = new JSONObject(result);

                                                        Log.d("test--", TAG + "--registration from data-status--" + jsonObject.getString("status"));
                                                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                                                            jsonObjectProfile = jsonObject.getJSONObject("mobilizer_profile");
                                                            globalVariable.appID = jsonObject.getString("app_id");

                                                            //////////////////inserting registration data/////////////////
                                                            cv1 = new ContentValues();
                                                            cv1.put("mobilizer_code", jsonObject.getString("mobilizer_code"));
                                                            cv1.put("mobilizer_number", jsonObject.getString("mobilizer_number"));
                                                            cv1.put("villageName", "");
                                                            cv1.put("UID", "");
                                                            cv1.put("app_id", jsonObject.getString("app_id"));
                                                            Log.d("test--", "  globalVariable.imei_no3" + jsonDataObj1.getString("imei_no"));
                                                            globalVariable.imei_no = jsonDataObj1.getString("imei_no");
                                                            Log.d("test--", "  globalVariable.imei_no5" + jsonDataObj1.getString("imei_no"));
                                                            cv1.put("imei_no", globalVariable.imei_no);
                                                            cv1.put("os_type", "Android");
                                                            cv1.put("manufacturer_name", deviceManufacturer + " " + PhoneModel);
                                                            cv1.put("os_version", AndroidVersion);
                                                            cv1.put("firebase_token", "0");
                                                            cv1.put("app_version", version);
                                                            cv1.put("message", jsonObject.getString("message"));
                                                            cv1.put("status", jsonObject.getString("status"));
                                                            cv1.put("mobilizer_name", jsonObjectProfile.getString("mobilizer_name"));
                                                            cv1.put("coordinator", jsonObjectProfile.getString("coordinator"));
                                                            cv1.put("unit", jsonObjectProfile.getString("unit"));
                                                            cv1.put("center", jsonObjectProfile.getString("center"));
                                                            cv1.put("vertical", jsonObjectProfile.getString("vertical"));
                                                            cv1.put("mobilizer_state", jsonObjectProfile.getString("mobilizer_state"));
                                                            cv1.put("location", jsonObjectProfile.getString("location"));
                                                            cv1.put("appointment_date", jsonObjectProfile.getString("appointment_date"));
                                                            cv1.put("position", jsonObjectProfile.getString("position"));
                                                            cv1.put("final_status", jsonObjectProfile.getString("final_status"));
                                                            cv1.put("resignation_date", jsonObjectProfile.getString("resignation_date"));
                                                            long row =       appDatabase.insert("user_login", null, cv1);

                                                            if (row > 0) {
                                                                // Toast.makeText(getApplicationContext(), "Registered Successfully.", Toast.LENGTH_LONG).show();
                                                                new MyAsncTask().execute();
                                                                registeButton.setEnabled(true);
                                                            } else {
                                                                registeButton.setEnabled(true);
                                                                Toast.makeText(getApplicationContext(), "Not Registered Successfully.Please Try Again.", Toast.LENGTH_LONG).show();
                                                            }


                                                        }

                                                        else {    registeButton.setEnabled(true);}
                                                    }
                                                    else {    registeButton.setEnabled(true);}
                                                } catch (Exception e) {
                                                    registeButton.setEnabled(true);
                                                    e.printStackTrace();
                                                }
                                            }
                                        });





                                        ////////////////////////////////////////////////////////sending data/////////////////////
                                        registeButton.setEnabled(true);

                                    }
                                });

                                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(getApplicationContext(), "You are not able to Login.", Toast.LENGTH_LONG).show();
                                        registeButton.setEnabled(true);
                                    }
                                });

                                alertDialog.show();



                            }

                            else {    registeButton.setEnabled(true);}
                        }
                        else {    registeButton.setEnabled(true);}
                    } catch (Exception e) {
                        registeButton.setEnabled(true);
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Log.d("test--", " user_loginelse-" + packcur.getCount());
            new MyAsncTask().execute();

        }

    }

    private void attemptLoginLog() {


        final App globalVariable = (App) getApplicationContext();
        Cursor cursor = appDatabase.rawQuery("Select   *  from user_login where mobilizer_code= '" + mEmailView.getText().toString() + "' and mobilizer_number= '" + mobile_no_edt.getText().toString() + "' ", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                //  Toast.makeText(getApplicationContext(), "Login Successfully.", Toast.LENGTH_LONG).show();
                globalVariable.appID = cursor.getString(cursor.getColumnIndex("app_id"));
                globalVariable.imei_no = cursor.getString(cursor.getColumnIndex("imei_no"));
                globalVariable.VillageName = cursor.getString(cursor.getColumnIndex("villageName"));
                globalVariable.mobilizerName = cursor.getString(cursor.getColumnIndex("mobilizer_name"));

                if(cursor.getString(cursor.getColumnIndex("UID")).equalsIgnoreCase("")) {


                }
                else {
                    globalVariable.MY_UUID_SECURE = UUID.fromString(cursor.getString(cursor.getColumnIndex("UID")));

                }
////////////////////////////////////////////Call Api////////////////////////////////
                JSONObject jsonObjLog = new JSONObject();
                try {
                    jsonObjLog.put("mobilizer_code", mEmailView.getText().toString() );
                    jsonObjLog.put("mobilizer_number", mobile_no_edt.getText().toString());
                    jsonObjLog.put("form_type", "0");
                    jsonObjLog.put("latitude", globalVariable.latitude);
                    jsonObjLog.put("longitude", globalVariable.longitude);
                    jsonObjLog.put("time", globalVariable.current_date);
                    jsonObjLog.put("status", 1);

                    Log.d("test--", TAG + "--registrationlogin-jsonObjLog-" + jsonObjLog);


                } catch (JSONException e) {
                    registeButton.setEnabled(true);
                    e.printStackTrace();
                }
                cvLog = new ContentValues();
                cvLog.put("mobilizer_code", mEmailView.getText().toString());
                cvLog.put("mobilizer_number", mobile_no_edt.getText().toString());
                cvLog.put("current_latitude", globalVariable.latitude);
                cvLog.put("current_longitude", globalVariable.longitude);
                cvLog.put("current_login_time", globalVariable.current_date);
                cvLog.put("status", 1);

                new Service_Call(getApplicationContext(), "/mobilizer/log", jsonObjLog, false, new MyServiceListener() {
                    @Override
                    public void getServiceData(Model_TLI_API_Response api_response) {
                        String result = api_response.data;
                        model_TLI_api_response = api_response;

                        Log.d("test--", TAG + "--result data1 --" + result);

                        Log.d("test--", TAG + "--responsecode1" + model_TLI_api_response.response_code);
                        Log.d("test--", TAG + "--responsecode1HttpsURLConnection.HTTP_OK" + HttpsURLConnection.HTTP_OK);
                        try {
                            if (model_TLI_api_response.response_code == HttpsURLConnection.HTTP_OK) {

                                JSONObject jsonObject = null;
                                jsonObject = new JSONObject(result);

                                Log.d("test--", TAG + "--registrationLoginstatus1--" + jsonObject.getString("status"));
                                if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                    jsonObject.getString("message");

                                    Log.d("test--", TAG + "--registrationLoginstatus2--" + jsonObject.getString("status"));
                                    Log.d("test--", TAG + "--registrationLoginmessage- --" + jsonObject.getString("message"));
                                    Log.d("test--", TAG + "--sync_statusIn- --"+ sync_status);
                                    cvLog.put("syncStatus", 1);
                                    row_log = appDatabase.insert("user_login_log", null, cvLog);

                                    //////////////////inserting registration data/////////////////
                                }
                                else {
                                    registeButton.setEnabled(true);
                                    cvLog.put("syncStatus", 0);
                                    row_log = appDatabase.insert("user_login_log", null, cvLog);

                                    Log.d("test--", TAG + "--sync_statusOut- --"+ sync_status);
                                }

                            }

                            else if ( model_TLI_api_response.response_code==409){
                                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(RegisterationActivity.this);
                                alertDialog.setTitle("VALIDATION");
                                alertDialog.setMessage("Conflict, user sending data from a disabled device");

                                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {

                                        return;
                                    }
                                });


                                alertDialog.show();


                            }

                            else {
                                registeButton.setEnabled(true);
                                cvLog.put("syncStatus", 0);
                                row_log = appDatabase.insert("user_login_log", null, cvLog);

                            }
                            if (row_log > 0) {
                                registeButton.setEnabled(true);
                                final App globalVariable = (App) getApplicationContext();
                                globalVariable.mobilizer_code= mEmailView.getText().toString();
                                globalVariable.mobilizer_number = mobile_no_edt.getText().toString();
                                Toast.makeText(RegisterationActivity.this, "Login  Successfully", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterationActivity.this, MeetingYouthActivity.class);
                                startActivity(intent);


                            } else {
                                registeButton.setEnabled(true);
                                Toast.makeText(RegisterationActivity.this, "Not Login Succefully.Please Try Again.", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }

                        } catch (Exception e) {
                            registeButton.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }


        }   else {
            runOnUiThread(new Runnable() {
                public void run() {

                    registeButton.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Wrong Username or Password.Please Try Again", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });

        }

    }




    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterationActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */


    public void onReceivingResponse(String response, int responseCode) {
       /* mProgressView.setVisibility(false ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(false ? View.GONE : View.VISIBLE);*/

        if (responseCode != 0) {
            App.getInstance().setHttpResponseData(response);
            ProcessResponse processResponse = new ProcessResponse();
            String number = processResponse.getMobileNumber(response);

            //by passing otp here
            HttpResponseModel responseModel = new ProcessResponse().processRegisterationResponse();
            if (responseModel.getResposneCode() != 0) {
                Toast toast;
                toast = Toast.makeText(this, responseModel.getResposne(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                toast.show();
                startActivity(new Intent(this, MainMenuActivity.class));
                mProgressDialog.dismiss();
                finish();
            } else {
                mProgressDialog.dismiss();
                showAlertDialog("Error ", responseModel.getResposne());
            }
            //bt passing otp end
       /*     if (number == null)
                showAlertDialog("Error", "Mobile Number not registered");
         *//*   else if(processResponse.getOTP(response)==null)
                showAlertDialog("Error","OTP is not received");*//*
            else {
                mPasswordView.setVisibility(View.VISIBLE);
                textInputLayout.setVisibility(View.VISIBLE);
                mEmailView.setVisibility(View.GONE);
                textInputLayout_code.setVisibility(View.GONE);
                mob_textlayout.setVisibility(View.GONE);
                mobile_no_edt.setVisibility(View.GONE);
                registeButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
                App.getInstance().setHttpResponseData(response);
               // generateOTP(number);
            }*/

        } else {
            mProgressDialog.dismiss();
            showAlertDialog("Error", response);
        }
    }

    void generateOTP(String phoneNum) {
        Random random = new Random();
        final String otp = random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10);
        App.getInstance().setOTP(otp);
        String phoneNumber = phoneNum;
        String smsBody = "Registeration OTP is " + otp;
        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

// For when the SMS has been sent
/*        smsSentReciver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast toast;
                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                       toast= Toast.makeText(context, "OTP  sent on registered mobile", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                        toast.show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                       toast= Toast.makeText(context, "OTP can not be send", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                        toast.show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                       toast= Toast.makeText(context, "SIM not unavailable for OTP", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                        toast.show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                      toast=  Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                        toast.show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                      toast=  Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                        toast.show();
                        break;
                }
            }
        };
        registerReceiver(smsSentReciver, new IntentFilter(SMS_SENT));

// For when the SMS has been delivered
        smsReceivedReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                       Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_LONG).show();
                        Thread t=new Thread(){
                            int count=0;
                            @Override
                            public void run(){
                                super.run();
                                try {
                                    while (count < 3) {
                                        count++;
                                        sleep(1000);
                                    }
                                }
                                catch (InterruptedException e){

                                }
                                deleteSMS();
                            }
                        };
                        t.start();
                      break;
                    case Activity.RESULT_CANCELED:
                      Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
/*        registerReceiver(smsReceivedReceiver, new IntentFilter(SMS_DELIVERED));*//*
        final EditText motp=mPasswordView;
         OTPReceived=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle=intent.getExtras();
                String str="";
                if(bundle!=null){
                    SmsMessage[] smsMessages;
                    Object[] objects=(Object[]) bundle.get("pdus");
                    smsMessages=new SmsMessage[objects.length];
                    String otpBody="";
                    boolean flag=false;
                    for(int i=0;i<objects.length;i++){
                        smsMessages[i]=SmsMessage.createFromPdu((byte[]) objects[i]);
                       String address=smsMessages[i].getOriginatingAddress();
                        Log.d("otp address",otp);
                        if(address.equalsIgnoreCase("")) {
                            Log.d("otp", "body " + smsMessages[i].getMessageBody().toString());
                            str = smsMessages[i].getMessageBody().toString();
                            StringTokenizer st = new StringTokenizer(str, " ");
                            int tokenCount = 0;
                            while (st.hasMoreTokens()) {
                                tokenCount++;
                                String val = st.nextToken().toString();
                                if (tokenCount == 4) {
                                    otpBody = val;
                                    break;
                                }

                            }
                        }
                        if(otp.equals(otpBody)) {
                            flag=true;
                            break;
                        }
                    }
                    if(flag)
                    motp.setText(otp+"");
                }
            }
        };
        registerReceiver(OTPReceived,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

// Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
// Send a text based SMS
        smsManager.sendTextMessage(phoneNumber, null, smsBody,null,null);*/
    }

    void deleteSMS() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, new String[]{"_id", "address"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String add = cursor.getString(1);
                if (add.equals("8860013133")) {
                    getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
                }
            }
        }
    }

    void showAlertDialog(String title, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.alt);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
             /*   System.out.println("OK cloded");
                Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        "com.tli6.pratham.pace");
                startActivity(intent);*/

                dialog.cancel();
            }
        });
     /*   alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("negative clicked");
                dialogInterface.cancel();

            }
        });
*/
        alertDialog.show();
    }

    boolean isMobileNumberValid() {
        boolean flag = false;

        String regex = "[6789]\\d{9}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobile_no_edt.getText().toString());

        if (matcher.matches())
            flag = true;
        return flag;
    }

    void submit() {
        String otp_string = mPasswordView.getText().toString();
        if (otp_string.isEmpty()) {
            Toast toast = Toast.makeText(this, "enter  otp ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.show();
        } else {
            try {
                Toast toast;
                mProgressView.setVisibility(true ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(true ? View.GONE : View.VISIBLE);
                if (App.getInstance().getOTP().equals(otp_string)) {
                    HttpResponseModel responseModel = new ProcessResponse().processRegisterationResponse();
                    if (responseModel.getResposneCode() != 0) {
                        toast = Toast.makeText(this, responseModel.getResposne(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                        toast.show();
                        startActivity(new Intent(this, MainMenuActivity.class));
                        finish();
                    } else
                        showAlertDialog("Error ", responseModel.getResposne());
                } else {
                    toast = Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                    toast.show();
                }
                mProgressView.setVisibility(false ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(false ? View.GONE : View.VISIBLE);
            } catch (NumberFormatException ne) {
                showAlertDialog("OTP Error", "enter only 4 digit number ");
            }
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Constants.REGISTER:


                DialogInterface.OnClickListener loadingButtonListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                httpCommunicationLayer.cancelTask();
                                dialog.dismiss();

                            }
                        };
                mProgressDialog.setTitle("Connecting to server");
                mProgressDialog.setMessage("Please wait,it will take some time");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setButton(getString(R.string.cancel), loadingButtonListener);
                return mProgressDialog;


        }

        return null;
    }

    private class MyAsncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            attemptLoginLog();
            return null;
        }


        @Override
        protected void onPreExecute() {
           // progressDialog =new ProgressDialog(RegisterationActivity.this);
            progressDialog.setMessage("Please wait........");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
           // progressDialog.dismiss();
            super.onPostExecute(o);


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
