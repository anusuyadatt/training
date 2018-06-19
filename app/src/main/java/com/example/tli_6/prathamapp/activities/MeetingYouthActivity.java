package com.example.tli_6.prathamapp.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.interfaces.MyServiceListener;
import com.example.tli_6.prathamapp.model.Model_TLI_API_Response;
import com.example.tli_6.prathamapp.service.Service_Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import static com.example.tli_6.prathamapp.application.App.appDatabase;

public class MeetingYouthActivity extends AppCompatActivity {
    TextView mobilezer_name;
    Button meet_report, door_to_door, add_village, add_plan;
    private Model_TLI_API_Response model_TLI_api_response;
    String TAG = "MeetingYouthActivity";

    ContentValues cvLog,cv;
    Long row_log;
    int sync_status = 0;
    String DoorformData = "";
    int row;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_youth);
        final App globalVariable = (App) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (globalVariable.VillageName.equalsIgnoreCase("")) {
            toolbar.setTitle("VILLAGE : ABC ");
        }
        else {

            toolbar.setTitle("VILLAGE : " + globalVariable.VillageName);
        }
        setSupportActionBar(toolbar);
        AppDatabase dbhelper = AppDatabase.getInstance(MeetingYouthActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        mobilezer_name = (TextView) findViewById(R.id.mobilezer_name);
        meet_report = (Button) findViewById(R.id.meet_report);
        door_to_door = (Button) findViewById(R.id.door_to_door);
        add_village = (Button) findViewById(R.id.add_village);
        add_plan = (Button) findViewById(R.id.add_plan);

        if (globalVariable.mobilizerName.equalsIgnoreCase("")) {
            mobilezer_name.setText("Welcome : XYZ" );

        } else {
            mobilezer_name.setText("Welcome :" + globalVariable.mobilizerName);
        }

        Log.d("test--", TAG + "-MY_UUID_SECURE" + String.valueOf(globalVariable.MY_UUID_SECURE));
        add_village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingYouthActivity.this, VillageInformationActivity.class);

                startActivity(intent);
            }
        });
        meet_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingYouthActivity.this, MeetingReportActivity.class);
                if (globalVariable.VillageName.equalsIgnoreCase("")) {
                    Toast.makeText(MeetingYouthActivity.this, "Please First Add Village Information", Toast.LENGTH_LONG).show();

                } else {
                    startActivity(intent);
                }


            }
        });
        door_to_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingYouthActivity.this, DoorToDoorServeyDetail.class);
                if (globalVariable.VillageName.equalsIgnoreCase("")) {
                    Toast.makeText(MeetingYouthActivity.this, "Please First Add Village Information", Toast.LENGTH_LONG).show();

                } else {
                    startActivity(intent);
                }


            }
        });
        add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingYouthActivity.this, PlanningSheetActivity.class);
                if (globalVariable.VillageName.equalsIgnoreCase("")) {
                    Toast.makeText(MeetingYouthActivity.this, "Please First Add Village Information", Toast.LENGTH_LONG).show();

                } else {
                    startActivity(intent);
                }


             /*   if(globalVariable.VillageName != ""){   startActivity(intent); } else {
                    Toast.makeText(MeetingYouthActivity.this,"Please First Add Village Information",Toast.LENGTH_LONG).show();}
*/
            }
        });


      /*  log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToSerevr();
            }
        });*/

    }





    /*   private void sendDataToSerevr(){
          sync.setEnabled(false);
          sync.setText("Syncing...");

          final App globalVariable = (App)getApplicationContext();
          Cursor packcur = appDatabase.rawQuery("Select * from pratham_app_form where UID= '"+String.valueOf(globalVariable.MY_UUID_SECURE)+"' and syncStatus = 0 ", null);
          if(packcur.getCount()>0){
              while (packcur.moveToNext()) {
                  DoorformData = packcur.getString(packcur.getColumnIndex("formData"));
               final int   autoId =  packcur.getInt(packcur.getColumnIndex("auto_id"));
                  Log.d("test--", TAG + "--rowUpdateELSEautoId--" +autoId);
                  try {
                      JSONObject jsonObject = new JSONObject(DoorformData);
                      new Service_Call(getApplicationContext(), "/form/save", jsonObject, false, new MyServiceListener() {
                          @Override
                          public void getServiceData(Model_TLI_API_Response api_response) {
                              String result = api_response.data;
                              model_TLI_api_response = api_response;

                              Log.d("test--", TAG + "--result data1 --" + result);

                              Log.d("test--", TAG + "--responsecode1" + model_TLI_api_response.response_code);
                              Log.d("test--", TAG + "--responsecode1HttpsURLConnection.HTTP_OK" + HttpsURLConnection.HTTP_OK);
                              try {
                                  if (model_TLI_api_response.response_code == HttpsURLConnection.HTTP_OK) {
                                      cv = new ContentValues();
                                      JSONObject jsonObject = null;
                                      jsonObject = new JSONObject(result);

                                      Log.d("test--", TAG + "--registrationvillagestatus1--" + jsonObject.getString("status"));
                                      if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                          jsonObject.getString("message");
                                          cv.put("syncStatus",1);
                                          Log.d("test--", TAG + "--registrationvillagetatus2--" +   jsonObject.getString("status"));
                                          Log.d("test--", TAG + "--registrationvillagemessage- --" +   jsonObject.getString("message"));
                                          //////////////////inserting registration data/////////////////
                                          row=appDatabase.update("pratham_app_form", cv, "auto_id="+autoId, null);
                                          Log.d("test--", TAG + "--rowUpdateIF--" +row);
                                          sync.setEnabled(true);
                                          sync.setText("Sync");
                                      }
                                      else{
                                          cv.put("syncStatus",0);
                                          row=appDatabase.update("pratham_app_form", cv, "auto_id="+autoId, null);
                                          Log.d("test--", TAG + "--rowUpdateELSE--" +row);
                                          sync.setEnabled(true);
                                          sync.setText("Sync");
                                          }

                                  }
                                  else {
                                      sync.setEnabled(true);
                                      sync.setText("Sync");
                                      }

                                  if (row> 0) {
                                      sync.setEnabled(true);
                                      sync.setText("Sync");
                                      Toast.makeText(getApplicationContext(), "Data Sync Completed Successfully", Toast.LENGTH_LONG).show();

                                  } else {
                                      sync.setEnabled(true);
                                      sync.setText("Sync");
                                      Toast.makeText(getApplicationContext(), "Data Sync Not  Completed   Successfully", Toast.LENGTH_LONG).show();

                                 }

                              } catch (Exception e) {
                                  Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                                  sync.setEnabled(true);
                                  sync.setText("Sync");
                              }

                          }
                      });
                  } catch (JSONException e) {
                      e.printStackTrace();
                      sync.setEnabled(true);
                      sync.setText("Sync");
                  }
                  /////////////////////////////

              ///////////////////////////////





                  }
              }
  else {
              sync.setEnabled(true);
              sync.setText("Sync");

              Toast.makeText(getApplicationContext(), "All Data Is Already Synced", Toast.LENGTH_LONG).show();

          }

      }*/
    private void sendDataToSerevr() {

        final App globalVariable = (App)getApplicationContext();
        Cursor packcur = appDatabase.rawQuery("Select * from pratham_app_form where UID= '"+String.valueOf(globalVariable.MY_UUID_SECURE)+"' and syncStatus = 0 ", null);
        if(packcur.getCount()>0){
            while (packcur.moveToNext()) {
                DoorformData = packcur.getString(packcur.getColumnIndex("formData"));
                final int   autoId =  packcur.getInt(packcur.getColumnIndex("auto_id"));
                Log.d("test--", TAG + "--rowUpdateELSEautoId--" +autoId);
                try {
                    JSONObject jsonObject = new JSONObject(DoorformData);
                    new Service_Call(getApplicationContext(), "/form/save", jsonObject, false, new MyServiceListener() {
                        @Override
                        public void getServiceData(Model_TLI_API_Response api_response) {
                            String result = api_response.data;
                            model_TLI_api_response = api_response;

                            Log.d("test--", TAG + "--result data1 --" + result);

                            Log.d("test--", TAG + "--responsecode1" + model_TLI_api_response.response_code);
                            Log.d("test--", TAG + "--responsecode1HttpsURLConnection.HTTP_OK" + HttpsURLConnection.HTTP_OK);
                            try {
                                if (model_TLI_api_response.response_code == HttpsURLConnection.HTTP_OK) {
                                    cv = new ContentValues();
                                    JSONObject jsonObject = null;
                                    jsonObject = new JSONObject(result);

                                    Log.d("test--", TAG + "--registrationvillagestatus1--" + jsonObject.getString("status"));
                                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                                        jsonObject.getString("message");
                                        cv.put("syncStatus",1);
                                        Log.d("test--", TAG + "--registrationvillagetatus2--" +   jsonObject.getString("status"));
                                        Log.d("test--", TAG + "--registrationvillagemessage- --" +   jsonObject.getString("message"));
                                        //////////////////inserting registration data/////////////////
                                        row=appDatabase.update("pratham_app_form", cv, "auto_id="+autoId, null);
                                        Log.d("test--", TAG + "--rowUpdateIF--" +row);

                                    }
                                    else{
                                        cv.put("syncStatus",0);
                                        row=appDatabase.update("pratham_app_form", cv, "auto_id="+autoId, null);
                                        Log.d("test--", TAG + "--rowUpdateELSE--" +row);

                                    }

                                }
                                else {

                                }

                                if (row> 0) {

                                    Toast.makeText(getApplicationContext(), "Data Sync Completed Successfully", Toast.LENGTH_LONG).show();

                                } else {

                                    Toast.makeText(getApplicationContext(), "Data Sync Not  Completed   Successfully", Toast.LENGTH_LONG).show();

                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                /////////////////////////////

                ///////////////////////////////





            }
        }
        else {


            Toast.makeText(getApplicationContext(), "All Data Is Already Synced", Toast.LENGTH_LONG).show();

        }

    }

    private void logOut() {
        final App globalVariable = (App) getApplicationContext();

        ////////////////////////////Call Api////////////////////////
        JSONObject jsonObjLog = new JSONObject();
        try {
            jsonObjLog.put("form_type", "0");
            jsonObjLog.put("mobilizer_code", globalVariable.mobilizer_code);
            jsonObjLog.put("mobilizer_number", globalVariable.mobilizer_number);
            jsonObjLog.put("latitude", globalVariable.latitude);
            jsonObjLog.put("longitude", globalVariable.longitude);
            jsonObjLog.put("time", globalVariable.current_date);
            jsonObjLog.put("status", 0);
            Log.d("test--", TAG + "--registrationlogout-jsonObjLog-" + jsonObjLog);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        cvLog = new ContentValues();
        cvLog.put("mobilizer_code", globalVariable.mobilizer_code);
        cvLog.put("mobilizer_number", globalVariable.mobilizer_number);
        cvLog.put("current_latitude", globalVariable.latitude);
        cvLog.put("current_longitude", globalVariable.longitude);
        cvLog.put("current_login_time", globalVariable.current_date);
        cvLog.put("status", 0);

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

                        Log.d("test--", TAG + "--registrationlogoutstatus1--" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            jsonObject.getString("message");
                            sync_status = 1;

                            Log.d("test--", TAG + "--registrationlogoutstatus2--" + jsonObject.getString("status"));
                            Log.d("test--", TAG + "--registrationlogoutmessage- --" + jsonObject.getString("message"));
                            //////////////////inserting registration data/////////////////
                            cvLog.put("syncStatus", 1);
                            row_log = appDatabase.insert("user_login_log", null, cvLog);
                        } else {
                            cvLog.put("syncStatus", 0);
                            row_log = appDatabase.insert("user_login_log", null, cvLog);
                        }


                    } else {
                        cvLog.put("syncStatus", 0);
                        row_log = appDatabase.insert("user_login_log", null, cvLog);

                    }
                    if (row_log > 0) {
                        Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

                        globalVariable.mobilizer_code = "";
                        globalVariable.mobilizer_number = "";

                        globalVariable.VillageName = "";
                        globalVariable.mobilizerName = "";
                        globalVariable.VillagePincode = 0;
                        globalVariable.family_owner_name = "";
                        globalVariable.number_of_youths = 0;
                        globalVariable.MY_UUID_SECURE = null;
                        globalVariable.submitted_forms = 0;
                        Intent intent = new Intent(getApplicationContext(), RegisterationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                }
            }
        });
        ////////////////////////////Call Api////////////////////////


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_sync:
                sendDataToSerevr();
                return true;

            case R.id.menu_logOut:
                logOut();

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
