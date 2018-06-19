package com.example.tli_6.prathamapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tli-6 on 5/18/17.
 */
public class DailyActivityModel  {
    String device_id;
    String mcode;
    String message_id;
    String sender;
   String  mobile_timestamp;
    String village;
    String students_reached;
    String students_assessed;
   String students_ready_to_join;
    String server_response;
    String activity_date;


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
    public void setDeviceId(String m){
        device_id=m;
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
public void setOriginalSender(String s){
    this.sender=s;
}
    public void setMobileTimestamp(String mobile_timestamp) {
        this.mobile_timestamp = mobile_timestamp;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setStudentsRached(String students_reached) {
        this.students_reached = students_reached;
    }

    public void setStudentsAsessed(String students_assessed) {
        this.students_assessed = students_assessed;
    }

    public void setStudentsReadyToJoin(String students_ready_to_join) {
        this.students_ready_to_join = students_ready_to_join;
    }

    public String getDeviceId() {
        return device_id;
    }

    public String getMcode() {
        return mcode;
    }

    public String getMessageId() {
        return message_id;
    }

    public String getSender() {
        return sender;
    }

    public String getMobileTimestamp() {
        return mobile_timestamp;
    }

    public String getVillage() {
        return village;
    }

    public String getStudentsReached() {
        return students_reached;
    }

    public String getStudentsAssessed() {
        return students_assessed;
    }

    public String getStudentsReadyToJoin() {
        return students_ready_to_join;
    }
}
