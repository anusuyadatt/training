package com.example.tli_6.prathamapp.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static com.example.tli_6.prathamapp.application.App.appDatabase;

public class VillageInformationActivity extends AppCompatActivity {
    EditText vill_name,vill_pincode,dist_from_taluka,dist_from_center,sarpanch_name,sarpanch_contact,aganwari_name,aganwari_contact,shg_person_name,
            shg_contact_no,gramsevak_name,gramsevak_contact_no,nehru_yuva_kendra_person_name,nehru_yuva_kendra_person_contact_no,vill_representative,
            contact_no, ngo_details,village_population,no_of_households,estimated_no_of_youths;
    Button  submit_village;
    CheckBox farming_chk,industry_chk,migrants_chk,govt_job,business,other_chk,
            pre_primary,first_to_5th,sixth_to_10th, eleventh_to_12th,college_poly,college_iti,other_vtp;

    private Model_TLI_API_Response model_TLI_api_response;
    ContentValues cv,cv1;
    String  checkBoxChoices_emp = "";   String  checkBoxChoices_edu = "";
    String TAG = "VillageInformationActivity";
    ProgressDialog progressDialog;
    long row = 0;
    AlertDialog.Builder alertDialog ;
    public  String   mobilePattern  = "^[0-9]{2}[0-9]{8}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final App globalVariable = (App) getApplicationContext();
        AppDatabase dbhelper = AppDatabase.getInstance(VillageInformationActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        submit_village = (Button)findViewById(R.id.sbmt_village);
        vill_name = (EditText)findViewById(R.id.your_vill_name);
        vill_pincode = (EditText)findViewById(R.id.your_pincode);
        dist_from_taluka = (EditText)findViewById(R.id.dist_from_taluka);
        dist_from_center = (EditText)findViewById(R.id.dist_from_center);
        sarpanch_name = (EditText)findViewById(R.id.your_sarpanch_name);
        sarpanch_contact = (EditText)findViewById(R.id.sarpanch_contact_no);
        aganwari_name = (EditText)findViewById(R.id.anganwadi_worker_name);
        aganwari_contact = (EditText)findViewById(R.id.anganwadi_contact_no);
        shg_person_name = (EditText)findViewById(R.id.shg_person_name);
        shg_contact_no = (EditText)findViewById(R.id.shg_contact_no);
        gramsevak_name = (EditText)findViewById(R.id.gramsevak_name);
        gramsevak_contact_no = (EditText)findViewById(R.id.gramsevak_contact_no);
        nehru_yuva_kendra_person_name = (EditText)findViewById(R.id.nehru_yuva_kendra_person_name);
        nehru_yuva_kendra_person_contact_no = (EditText)findViewById(R.id.nehru_yuva_kendra_person_contact_no);
        vill_representative = (EditText)findViewById(R.id.representative_person_name);
        contact_no = (EditText)findViewById(R.id.your_contact_no);
        ngo_details = (EditText)findViewById(R.id.ngo_detail);
        village_population = (EditText)findViewById(R.id.village_population);
        no_of_households = (EditText)findViewById(R.id.no_of_households);
        estimated_no_of_youths = (EditText)findViewById(R.id.estimated_no_of_youths);

        farming_chk = (CheckBox)findViewById(R.id.farming_chk);
        industry_chk = (CheckBox)findViewById(R.id.industry_chk);
        migrants_chk = (CheckBox)findViewById(R.id.migrants_chk);
        govt_job = (CheckBox)findViewById(R.id.govt_job);
        business = (CheckBox)findViewById(R.id.business);
        other_chk = (CheckBox)findViewById(R.id.other_chk);


        pre_primary = (CheckBox)findViewById(R.id.pre_primary);
        first_to_5th = (CheckBox)findViewById(R.id.first_to_5th);
        sixth_to_10th = (CheckBox)findViewById(R.id.sixth_to_10th);
        eleventh_to_12th = (CheckBox)findViewById(R.id.eleventh_to_12th);
        college_poly = (CheckBox)findViewById(R.id.college_poly);
        college_iti = (CheckBox)findViewById(R.id.college_iti);
        other_vtp = (CheckBox)findViewById(R.id.other_vtp);

        submit_village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (farming_chk.isChecked()) {
                    checkBoxChoices_emp += farming_chk.getText().toString();

                    checkBoxChoices_emp = checkBoxChoices_emp + ",";
                }
                if (industry_chk.isChecked()) {
                    checkBoxChoices_emp += industry_chk.getText().toString();
                    checkBoxChoices_emp = checkBoxChoices_emp + ",";
                }
                if (migrants_chk.isChecked()) {
                    checkBoxChoices_emp += migrants_chk.getText().toString();
                    checkBoxChoices_emp = checkBoxChoices_emp + ",";
                }

                if (govt_job.isChecked()) {
                    checkBoxChoices_emp += govt_job.getText().toString();
                    checkBoxChoices_emp = checkBoxChoices_emp + ",";
                }
                if (business.isChecked()) {
                    checkBoxChoices_emp += business.getText().toString();
                    checkBoxChoices_emp = checkBoxChoices_emp + ",";
                }
                if (other_chk.isChecked()) {
                    checkBoxChoices_emp += other_chk.getText().toString();
                }

                ////////////////////////////////////
                if (pre_primary.isChecked()) {
                    checkBoxChoices_edu += pre_primary.getText().toString();
                    checkBoxChoices_edu = checkBoxChoices_edu + ",";
                }
                if (first_to_5th.isChecked()) {
                    checkBoxChoices_edu += first_to_5th.getText().toString();
                    checkBoxChoices_edu = checkBoxChoices_edu + ",";
                }
                if (sixth_to_10th.isChecked()) {
                    checkBoxChoices_edu += sixth_to_10th.getText().toString();
                    checkBoxChoices_edu = checkBoxChoices_edu + ",";
                }

                if (eleventh_to_12th.isChecked()) {
                    checkBoxChoices_edu += eleventh_to_12th.getText().toString();
                    checkBoxChoices_edu = checkBoxChoices_edu + ",";
                }
                if (college_poly.isChecked()) {
                    checkBoxChoices_edu += college_poly.getText().toString();
                    checkBoxChoices_edu = checkBoxChoices_edu + ",";
                }
                if (college_iti.isChecked()) {
                    checkBoxChoices_edu += college_iti.getText().toString();
                    checkBoxChoices_edu = checkBoxChoices_edu + ",";
                }
                if (other_vtp.isChecked()) {
                    checkBoxChoices_edu += other_vtp.getText().toString();
                }
                /////////////////////////Validation////////////////////////////


                if (ValidateForm() == true) {
                    ///////////////////////////////////////Saving data in json Format////////////////////////


                    new MyAsncTask().execute();
                }
            }
        });






    }

    private void savingData() {
        final App globalVariable = (App) getApplicationContext();
        final JSONObject jsonDataObj1 = new JSONObject();
        try {
            jsonDataObj1.put("form_type",1);
            jsonDataObj1.put("vill_name", vill_name.getText().toString());
            jsonDataObj1.put("vill_pin_code", vill_pincode.getText().toString());
            jsonDataObj1.put("dist_from_taluka", dist_from_taluka.getText().toString());
            jsonDataObj1.put("dist_from_center", dist_from_center.getText().toString());
            jsonDataObj1.put("vill_sarpanch_name", sarpanch_name.getText().toString());
            jsonDataObj1.put("sarpanch_contact", sarpanch_contact.getText().toString());
            jsonDataObj1.put("aganwadi_worker_name", aganwari_name.getText().toString());
            jsonDataObj1.put("aganwadi_worker_contact", aganwari_contact.getText().toString());
            jsonDataObj1.put("shg_person_name", shg_person_name.getText().toString());
            jsonDataObj1.put("shg_contact_no", shg_contact_no.getText().toString());
            jsonDataObj1.put("gramsevak_name", gramsevak_name.getText().toString());
            jsonDataObj1.put("gramsevak_contact_no", gramsevak_contact_no.getText().toString());

            jsonDataObj1.put("nehru_yuva_kendra_person_name", nehru_yuva_kendra_person_name.getText().toString());
            jsonDataObj1.put("nehru_yuva_kendra_person_contact_no", nehru_yuva_kendra_person_contact_no.getText().toString());
            jsonDataObj1.put("vill_representative", vill_representative.getText().toString());
            jsonDataObj1.put("contact_no", contact_no.getText().toString());
            jsonDataObj1.put("ngo_detail", vill_representative.getText().toString());
            jsonDataObj1.put("vill_population", contact_no.getText().toString());
            jsonDataObj1.put("no_of_households", no_of_households.getText().toString());
            jsonDataObj1.put("estimated_no_of_youths", estimated_no_of_youths.getText().toString());
            jsonDataObj1.put("emp_opport_nearby_vill", checkBoxChoices_emp);
            jsonDataObj1.put("edu_facility_nearby_vill", checkBoxChoices_edu);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        globalVariable.MY_UUID_SECURE = UUID.fromString(UUID.randomUUID().toString());;

        cv = new ContentValues();

        cv.put("formID", 1);
        cv.put("UID", String.valueOf(globalVariable.MY_UUID_SECURE));
        cv.put("formVersion", 1);
        globalVariable.VillageName= vill_name.getText().toString();
        cv.put("villageName",  vill_name.getText().toString());
        cv.put("familyOwner",  "" );
        cv.put("numOfYouths",0);

        cv.put("formCompletionStatus",0);
        cv.put("formData",jsonDataObj1.toString());

        new Service_Call(getApplicationContext(), "/form/save", jsonDataObj1, false, new MyServiceListener() {
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

                        Log.d("test--", TAG + "--registrationvillagestatus1--" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            jsonObject.getString("message");

                            Log.d("test--", TAG + "--registrationvillagetatus2--" +   jsonObject.getString("status"));
                            Log.d("test--", TAG + "--registrationvillagemessage- --" +   jsonObject.getString("message"));
                            //////////////////inserting registration data/////////////////
                            cv.put("syncStatus",1);
                            row = appDatabase.insert("pratham_app_form", null, cv);
                        }



                        else{
                            cv.put("syncStatus",0);
                            row = appDatabase.insert("pratham_app_form", null, cv);}

                    }
                    else if ( model_TLI_api_response.response_code==409){
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(VillageInformationActivity.this);
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
                        cv.put("syncStatus", 0);
                        row = appDatabase.insert("pratham_app_form", null, cv);
                    }
                    if (row > 0) {
                        Toast.makeText(getApplicationContext(), "Village Information Saved Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Village Information Not Saved Successfully", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                }

            }
        });

        cv1 = new ContentValues();
        cv1.put("villageName",  vill_name.getText().toString());
        cv1.put("UID",  String.valueOf(globalVariable.MY_UUID_SECURE));
        appDatabase.update("user_login", cv1, "status = 1", null);

        finish();
    }


    private class MyAsncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            savingData();
            return null;
        }


        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(VillageInformationActivity.this);
            progressDialog.setMessage("Please wait........");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            progressDialog.dismiss();

        }
    }



    private boolean ValidateForm() {
        //  Toast.makeText(VillageInformationActivity.this,"getCheckedRadioButtonId."+radio_population.getCheckedRadioButtonId(),Toast.LENGTH_LONG).show();
        alertDialog = new AlertDialog.Builder(VillageInformationActivity.this);
        if(vill_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  Fill  Village Name");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    vill_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(vill_pincode.getText().toString().equalsIgnoreCase("")||vill_pincode.getText().toString().length()<6){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  Fill  Valid   Pincode");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    vill_pincode.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(dist_from_taluka.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Village Distance From Taluka");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    dist_from_taluka.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(dist_from_center.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Village Distance From Center.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    dist_from_center.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(sarpanch_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Sarpanch Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    sarpanch_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(sarpanch_contact.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  FILL  Valid  Sarpanch Contact  Number");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    sarpanch_contact.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(aganwari_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Anganwari Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    aganwari_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(aganwari_contact.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  FILL  Valid  Anganwari  Contact  Number");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    aganwari_contact.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(shg_person_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill SHG Person Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    shg_person_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(shg_contact_no.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  FILL  VALID SHG  CONTACT NUMBER");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    shg_contact_no.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(gramsevak_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Gramsevak Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    gramsevak_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(gramsevak_contact_no.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  FILL  VALID Gramsevak Contact Number");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    gramsevak_contact_no.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(nehru_yuva_kendra_person_name.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Nehru Yuva Kendra Person Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    nehru_yuva_kendra_person_name.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(nehru_yuva_kendra_person_contact_no.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  FILL  VALID  Nehru Yuva Kendra Person Contact Number.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    nehru_yuva_kendra_person_contact_no.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(vill_representative.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Village Representative Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    vill_representative.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }


        else if(contact_no.getText().toString().matches(mobilePattern) == false){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("PLEASE  FILL  VALID  Contact Number.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    contact_no.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(ngo_details.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill NGO Details.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    ngo_details.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(village_population.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Village Population.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    village_population.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(no_of_households.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Number Of House Holds.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    no_of_households.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(estimated_no_of_youths.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill Estimated Number Of Youths.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    estimated_no_of_youths.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(checkBoxChoices_emp.equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Check Employment Opportunity.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    farming_chk.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(checkBoxChoices_edu.equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please  Check Education Facility.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    pre_primary.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else {
            return true;
        }
    }
}
