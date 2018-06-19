package com.example.tli_6.prathamapp.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.interfaces.MyServiceListener;
import com.example.tli_6.prathamapp.model.Model_TLI_API_Response;
import com.example.tli_6.prathamapp.service.Service_Call;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.example.tli_6.prathamapp.application.App.appDatabase;

public class MeetingReportActivity extends AppCompatActivity {
    TextView type_of_meeting,photo_view;
    EditText vill_representative,no_of_participants,outcome_of_meeting;
    RadioGroup radio_meeting;
    RadioButton radio_meetingButton;
    Button sbmt_meeting;

    private Model_TLI_API_Response model_TLI_api_response;
    ContentValues cv;
    String imageURL = "";
    ImageView  imageView;
    String TAG = "MeetingReportActivity";
    ProgressDialog progressDialog;
    long row=0;
    ContentValues cvLog;
    Long row_log;
    int sync_status =0;
    AlertDialog.Builder alertDialog ;
    String upLoadServerUri = null;
    String uploadFilePath = "";
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    private static final int CAMERA_REQUEST = 1888;
    private int PICK_IMAGE_REQUEST = 1;
    String temp_logo = "temp_image"+String.valueOf(System.currentTimeMillis())+".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_report);
        final App globalVariable = (App) getApplicationContext();
        upLoadServerUri ="http://yiidemo1.tlitech.net/api/meeting/save";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           /*  if(String.valueOf(globalVariable.MY_UUID_SECURE).equalsIgnoreCase("") ){
            logOut();

        }*/
        if (globalVariable.VillageName.equalsIgnoreCase("")) {
            toolbar.setTitle("VILLAGE : ABC ");
        }
        else {

            toolbar.setTitle("VILLAGE : " + globalVariable.VillageName);
        }
        setSupportActionBar(toolbar);
        AppDatabase dbhelper = AppDatabase.getInstance(MeetingReportActivity.this);
        if (appDatabase == null)
            appDatabase = dbhelper.getWritableDatabase();
        vill_representative = (EditText) findViewById(R.id.representative_full_name);
        no_of_participants = (EditText) findViewById(R.id.no_of_participants);
        outcome_of_meeting = (EditText) findViewById(R.id.outcome_of_meeting);
        radio_meeting = (RadioGroup) findViewById(R.id.radio_meeting);
        photo_view = (TextView) findViewById(R.id.photo_view);
        imageView = (ImageView) findViewById(R.id.imageView);
        sbmt_meeting = (Button) findViewById(R.id.sbmt_meeting);
        photo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
///////////////////////////////////////////

                final CharSequence[] options = { "Take Photo By Camera", "Choose from Gallery","Cancel" };
                // Toast.makeText(this,"upload_images1",Toast.LENGTH_LONG).show();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MeetingReportActivity.this);
                //  Toast.makeText(this,"upload_images2",Toast.LENGTH_LONG).show();
                builder.setTitle("Add Photo");
                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo By Camera"))
                        {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 1);


                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);

                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();


                /////////////////////////////////



             /*   //upload_image();
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
*/
            }
        });

        sbmt_meeting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int meetingTypeId = radio_meeting.getCheckedRadioButtonId();
                radio_meetingButton = (RadioButton) findViewById(meetingTypeId);
                if (ValidateForm() == true) {
                    dialog = ProgressDialog.show(MeetingReportActivity.this, "", "Saving Data...", true);

                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                }
                            });
                            Log.d("MainActivity", "run: uploadFilePath"+uploadFilePath);
                            uploadFile(uploadFilePath);

                        }
                    }).start();


                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("uploadFile", "requestCode :" + requestCode);
        Log.d("uploadFile", "resultCode :" + resultCode);
        Log.d("uploadFile", "RESULT_OK :" + RESULT_OK);
        Log.d("uploadFile", "data :" + data);
        if (requestCode ==1 && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            Uri imageUri = data.getData();
            uploadFilePath =  getPath(imageUri);

            }
     else   if (requestCode ==2 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //displaying selected image to imageview
                imageView.setImageBitmap(bitmap);
                uploadFilePath =    getPath(imageUri);
                //calling the method uploadBitmap to upload image

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("uploadFile", "uploadFilePath :" + uploadFilePath);
    }




    private boolean ValidateForm() {
        //  Toast.makeText(VillageInformationActivity.this,"getCheckedRadioButtonId."+radio_population.getCheckedRadioButtonId(),Toast.LENGTH_LONG).show();
        alertDialog = new AlertDialog.Builder(MeetingReportActivity.this);

        if(radio_meeting.getCheckedRadioButtonId() == -1){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Check Type Of Meeting.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    radio_meeting.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else if(vill_representative.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill  Representative Name.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    vill_representative.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(no_of_participants.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Fill No. Of Participants.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    no_of_participants.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }
        else if(outcome_of_meeting.getText().toString().equalsIgnoreCase("")){
            alertDialog.setTitle("VALIDATION");
            alertDialog.setMessage("Please Outcome Of Meeting.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    outcome_of_meeting.requestFocus();
                }
            });
            alertDialog.show();
            return  false;

        }

        else {
            return true;
        }
    }


    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.d("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + sourceFileUri);

            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        }
        else
        {
            try {
                final App globalVariable = (App) getApplicationContext();
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("imagefile", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                Log.d("uploadFile", "fileName :"
                        +fileName + "" + fileName);
                dos.writeBytes("Content-Disposition: form-data; name=\"imagefile\";filename="
                        + fileName + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.max(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                Log.d("uploadFile", "bytesRead Out While : "
                        + bytesRead + ": " + bytesRead);
                while (bytesRead > 0) {
                    Log.d("uploadFile", "bytesRead In While : : " + buffer);
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.max(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                Log.d("uploadFile", "lineEnd :"
                        +fileName + "" + bytesRead);
                dos.writeBytes(lineEnd);
             //   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "app_id" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(globalVariable.appID);
                dos.writeBytes(lineEnd);


                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "form_type" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("3");
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "form_version" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("1");
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "vuid" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(String.valueOf(globalVariable.MY_UUID_SECURE ));
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "imei_no" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(globalVariable.imei_no);
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "meet_vill_name" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(globalVariable.VillageName);
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "meet_vill_pin_code" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes( String.valueOf(globalVariable.VillagePincode));
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "type_of_meeting" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(  radio_meetingButton.getHint().toString());
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "meet_vill_representative" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(vill_representative.getText().toString());
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "no_of_participants" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes( no_of_participants.getText().toString());
                dos.writeBytes(lineEnd);

                //adding other parameters other than document ends
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "meet_outcome" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes( outcome_of_meeting.getText().toString());
                dos.writeBytes(lineEnd);
                //adding other parameters other than document ends

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.d("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                ////////////////////////////Saving Data Locally///////////////////////////////////////

                final JSONObject jsonDataObj1 = new JSONObject();
                try {
                    jsonDataObj1.put("form_type", "3");
                    jsonDataObj1.put("meet_vill_name", globalVariable.VillageName);
                    jsonDataObj1.put("meet_vill_pin_code", globalVariable.VillagePincode);
                    jsonDataObj1.put("type_of_meeting", radio_meetingButton.getHint().toString());
                    jsonDataObj1.put("meet_vill_representative", vill_representative.getText().toString());
                    jsonDataObj1.put("no_of_participants", no_of_participants.getText().toString());
                    jsonDataObj1.put("meet_outcome", outcome_of_meeting.getText().toString());
                    jsonDataObj1.put("imageUrl",uploadFilePath);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                cv = new ContentValues();
                cv.put("formID", 3);
                cv.put("UID", String.valueOf(globalVariable.MY_UUID_SECURE));
                cv.put("formVersion", 1);
                cv.put("villageName",  globalVariable.VillageName);
                cv.put("familyOwner",  "" );
                cv.put("numOfYouths",0);
                cv.put("formCompletionStatus",0);
                cv.put("formData",jsonDataObj1.toString());
                ////////////////////////////Saving Data Locally///////////////////////////////////////
                if(serverResponseCode == 200){
                    BufferedReader in = null;
                    String   response = "";
                    ///////////////////////////////
                    String line;
                    InputStream inputStream_reader = null;
                    try {
                        inputStream_reader = conn.getInputStream();
                    } catch (IOException e) {
                        inputStream_reader = conn.getErrorStream();
                    }
                    in = new BufferedReader(new InputStreamReader(inputStream_reader, "UTF-8"));
                    while ((line = in.readLine()) != null) {
                        response += line;
                    }
                    Log.d("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    if(jsonResponse.optString("status").equalsIgnoreCase("1")) {
                        cv.put("syncStatus",1);
                        row = appDatabase.insert("pratham_app_form", null, cv);
                        }
                    else {
                        cv.put("syncStatus",0);
                        row = appDatabase.insert("pratham_app_form", null, cv);

                    }


                    //////////////////////////////


                }

              else {
                    cv.put("syncStatus",0);
                    row = appDatabase.insert("pratham_app_form", null, cv);
                    }

                    if(row>0){
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(MeetingReportActivity.this, "Form Saved Successfully.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(MeetingReportActivity.this, "Form Saved Successfully.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();


            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MeetingReportActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MeetingReportActivity.this, "No Internet Connection.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file ", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
