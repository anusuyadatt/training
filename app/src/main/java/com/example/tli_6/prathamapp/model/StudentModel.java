package com.example.tli_6.prathamapp.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tli-6 on 5/18/17.
 */
public class StudentModel {
  String  device_id;
    String  mcode;
    String message_id;
    String  sender;
    String  mobile_timestamp;
    String  village;
    String  student_name;
    String  program_code;
    String Rojar_mitra;
String server_response;
    String  activity_date;
    public void setActivityDate(String s){
        activity_date=s;
    }

    public String getActivityDate() {
        return activity_date;
    }
    public void setServerResponse(String m){


        try {
            JSONObject jsonObject=new JSONObject(m);
            if( jsonObject.has("response")){
                this.server_response=jsonObject.getString("response");
            }

        }
        catch (JSONException je){

        }
    }
    public void setOriginalServerResposne(String m){
        server_response=m;
    }
    public String getServerResponse(){

        return server_response;
    }
    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    public void setMessageId(String message_id) {
        this.message_id = message_id;
    }

    public void setSender(String sender) {
        this.sender = "+91"+sender;
    }
public  void setOriginalSender(String s){
    sender=s;
}
    public void setVillage(String village) {
        this.village = village;
    }

    public void setMobileTimestamp(String mobile_timestamp) {
        this.mobile_timestamp = mobile_timestamp;
    }

    public void setStudentName(String student_name) {
        this.student_name = student_name;
    }

    public void setProgramCode(String program_code) {
        this.program_code = program_code;
    }

    public void setRojarMitra(String rojar_mitra) {
        Rojar_mitra = rojar_mitra;
    }

    public String getDeviceId() {
        return device_id;
    }

    public String getMcode() {
        return mcode;
    }

    public String getVillage() {
        return village;
    }

    public String getSender() {
        return sender;
    }

    public String getStudentName() {
        return student_name;
    }

    public String getProgramCode() {
        return program_code;
    }

    public String getRojarMitra() {
        return Rojar_mitra;
    }

    public String getMessageId() {
        return message_id;
    }

    public String getMobileTmestamp() {
        return mobile_timestamp;
    }
}
