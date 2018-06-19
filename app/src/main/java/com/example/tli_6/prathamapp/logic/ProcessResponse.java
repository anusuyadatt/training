package com.example.tli_6.prathamapp.logic;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.R;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.model.DailyActivityModel;
import com.example.tli_6.prathamapp.model.HttpResponseModel;
import com.example.tli_6.prathamapp.model.ProgramListModel;
import com.example.tli_6.prathamapp.model.StudentModel;
import com.example.tli_6.prathamapp.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tli-6 on 5/18/17.
 */
public class ProcessResponse {
    public HttpResponseModel processRegisterationResponse() {
        cleanExistingDB();
        String request_data = App.getInstance().getResposneData();
        String msg = App.getInstance().getResources().getString(R.string.register_ok);
        int code = 1;
        HttpResponseModel response = new HttpResponseModel(msg, code);
        UserModel userModel = new UserModel();
        ArrayList<ProgramListModel> programList = new ArrayList<ProgramListModel>();
        try {

            JSONObject jsonObject = new JSONObject(request_data);
            if (jsonObject.has("device_id"))
                userModel.setDeviceId(jsonObject.get("device_id").toString());
            if (jsonObject.has("program_list")) {
                JSONArray jsonArray = new JSONArray(jsonObject.get("program_list").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    ProgramListModel programListModel = new ProgramListModel();
                    JSONObject jsonObject2 = new JSONObject(jsonArray.get(i).toString());

                    if (jsonObject2.has("program_name"))
                        programListModel.setProgramName(jsonObject2.get("program_name").toString());
                    if (jsonObject2.has("program_code"))
                        programListModel.setProgramCode(jsonObject2.get("program_code").toString());
                    System.out.println("program data " + programListModel.getProgramCode() + " -->" + programListModel.getProgramName());
                    programList.add(programListModel);

                }
                System.out.println("prohram list size " + programList.size());
            }
            if (jsonObject.has("user_profile")) {
                JSONObject jsonObject1 = new JSONObject(jsonObject.get("user_profile").toString());
                Iterator iterator = jsonObject1.keys();
                String key;
                while (iterator.hasNext()) {
                    key = iterator.next().toString();
                    if (key.equals("unit"))
                        userModel.setUnit(jsonObject1.get("unit").toString());
                    if (key.equals("sub_unit"))
                        userModel.setSubUnit(jsonObject1.get("sub_unit").toString());
                    if (key.equals("state"))
                        userModel.setState(jsonObject1.get("state").toString());
                    if (key.equals("mob_center"))
                        userModel.setMobCenter(jsonObject1.get("mob_center").toString());
                    if (key.equals("location"))
                        userModel.setLocation(jsonObject1.get("location").toString());
                    if (key.equals("coordinator"))
                        userModel.setCoordinator(jsonObject1.get("coordinator").toString());
                    if (key.equals("mob_name"))
                        userModel.setName(jsonObject1.get("mob_name").toString());
                    if (key.equals("mob_code"))
                        userModel.setMCode(jsonObject1.get("mob_code").toString());
                    if (key.equals("position"))
                        userModel.setPosition(jsonObject1.get("position").toString());
                    if (key.equals("appointment_date"))
                        userModel.setAppointDate(jsonObject1.get("appointment_date").toString());
                    if (key.equals("registeration_date"))
                        userModel.setRegistDate(jsonObject1.get("registeration_date").toString());
                    if (key.equals("mobile_number"))
                        userModel.setMobileNumber(jsonObject1.get("mobile_number").toString());
                }
            }


        } catch (JSONException js) {
            msg = "Resposne Processing Error";
            code = 0;

        }
        boolean flag = insertIntoDataBase(userModel, programList);
        if (flag) ;
        else {
            msg = App.getInstance().getString(R.string.db_error);
            code = 0;
        }
        response.setResposne(msg);
        response.setResposneCode(code);
        return response;
    }

    public String getMobileNumber(String reqdata) {
        String number = null;
        try {
            JSONObject jsonObject = new JSONObject(reqdata);
            if (jsonObject.has("user_profile")) {
                JSONObject jsonObject1 = new JSONObject(jsonObject.get("user_profile").toString());
                if (jsonObject1.has("mobile_number"))
                    number = jsonObject1.get("mobile_number").toString();

            }

        } catch (JSONException je) {

        }
        return number;
    }

    public String getOTP(String resdata) {
        String otp = null;
        try {
            JSONObject jsonObject = new JSONObject(resdata);
            if (jsonObject.has("otp")) {
                otp = jsonObject.getString("otp");
            }
        } catch (JSONException e) {

        }
        return otp;
    }


    boolean insertIntoDataBase(UserModel userModel, ArrayList<ProgramListModel> arrayList) {
        boolean flag = true;
        Cursor cursor = AppDatabase.getUserDatabaseInstance().query(Constants.USER_TABLE, null, null, null, null, null, null);
        System.out.println("Cursor "+cursor);
        if (cursor != null && cursor.getCount() > 0) {
            int deleteCount = AppDatabase.getUserDatabaseInstance().delete(Constants.USER_TABLE, null, null);
            System.out.println("deleted count " + deleteCount);
        }
        if (cursor != null)
            cursor.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mobiliser_code", userModel.getMCode());
        contentValues.put("mobiliser_name", userModel.getName());
        contentValues.put("mobile_no", userModel.getMobileNumber());
        contentValues.put("unit", userModel.getUnit());
        contentValues.put("sub_unit", userModel.getSubUit());
        contentValues.put("state", userModel.getState());
        contentValues.put("mobilisation_center", userModel.getMobCenter());
        contentValues.put("location", userModel.getLocation());
        contentValues.put("coordinator", userModel.getCoordinator());
        contentValues.put("position", userModel.getPosition());
        contentValues.put("appointment_date", userModel.getAppointDate());
        contentValues.put("registeration_date", userModel.getRegistDate());
        contentValues.put("device_id", userModel.getDeviceId());
        Long count = AppDatabase.getUserDatabaseInstance().insert(Constants.USER_TABLE, null, contentValues);
        System.out.println("Insertion Count " + count);
        if (count == -1)
            return false;
        for (ProgramListModel programListModel : arrayList) {
            ContentValues cv = new ContentValues();
            cv.put("program_code", programListModel.getProgramCode());
            cv.put("program_name", programListModel.getProgramName());
            Long count2 = AppDatabase.getDatabaseInstance().insert(Constants.PROGRAM_LIST_TABLE, null, cv);
            System.out.println("insertin program count " + count2);
            if (count2 == -1)
                return false;
        }
        return flag;

    }

    public HttpResponseModel processStudentResponse(StudentModel model) {
        String msg = App.getInstance().getResources().getString(R.string.submit_ok);
        int code = 1;
        boolean proceed = true;
        HttpResponseModel response = new HttpResponseModel(msg, code);
        Cursor cursor = AppDatabase.getDatabaseInstance().rawQuery("select max(ID) as lastid from student_details", null);
        System.out.println("Cursor "+cursor);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            String lastid = cursor.getString(cursor.getColumnIndex("lastid"));
            System.out.println("LastId " + lastid);
            if(lastid==null)
                lastid="";
            String selection = "ID=? and student_name=? and student_village=? and program_code=? and rojgar_mitra=? ";
            String[] args = {lastid, model.getStudentName(), model.getVillage(), model.getProgramCode(), model.getRojarMitra()};
            Cursor cursor1 = AppDatabase.getDatabaseInstance().query(Constants.STUDENT_DETAILS_TABLE, null, selection, args, null, null, null);
            if (cursor1 != null && cursor1.getCount() > 0)
                proceed = false;
            if (cursor1 != null)
                cursor1.close();
        }
        if (cursor != null)
            cursor.close();
        if (proceed) {
            ContentValues cv = new ContentValues();
            cv.put("device_id", model.getDeviceId());
            cv.put("student_name", model.getStudentName());
            cv.put("mcode", model.getMcode());
            cv.put("student_village", model.getVillage());
            cv.put("program_code", model.getProgramCode());
            cv.put("rojgar_mitra", model.getRojarMitra());
            cv.put("student_contact_no", model.getSender());
            cv.put("activity_datetime", model.getMobileTmestamp());
            cv.put("message_id", model.getMessageId());
            cv.put("server_response", model.getServerResponse());
            cv.put("activtity_date", model.getActivityDate());

            Long count = AppDatabase.getDatabaseInstance().insert(Constants.STUDENT_DETAILS_TABLE, null, cv);
            System.out.println("Insertion Count " + count);
            if (count == -1) {
                response.setResposne(App.getInstance().getString(R.string.db_error));
                response.setResposneCode(0);
            }
        }
        return response;
    }

    public HttpResponseModel processDailyActivityResponse(DailyActivityModel model) {
        String msg = App.getInstance().getResources().getString(R.string.submit_ok);
        int code = 1;
        boolean proceed = true;
        HttpResponseModel response = new HttpResponseModel(msg, code);
        Cursor cursor = AppDatabase.getDatabaseInstance().rawQuery("select max(ID) as lastid from mobiliser_activities", null);
       System.out.println("Cursor "+cursor);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            String lastid = cursor.getString(cursor.getColumnIndex("lastid"));
            System.out.println("LastId " + lastid);
            if(lastid==null)
                lastid="";
            String selection = "ID=? and sender=? and village_name=? and students_reached=? and students_assessed=? and students_ready_to_join=? and activtity_date=?";
            String[] args = {lastid, model.getSender(), model.getVillage(), model.getStudentsReached(), model.getStudentsAssessed(), model.getStudentsReadyToJoin()
            ,model.getActivityDate()};
            Cursor cursor1 = AppDatabase.getDatabaseInstance().query(Constants.MOBILISER_DAILY_ACTIVITY_TABLE, null, selection, args, null, null, null);
            if (cursor1 != null && cursor1.getCount() > 0)
                proceed = false;
            if (cursor1 != null)
                cursor1.close();
        }
        if (cursor != null)
            cursor.close();
        if (proceed) {
            ContentValues cv = new ContentValues();
            cv.put("sender", model.getSender());
            cv.put("village_name", model.getVillage());
            cv.put("students_reached", model.getStudentsReached());
            cv.put("students_assessed", model.getStudentsAssessed());
            cv.put("students_ready_to_join", model.getStudentsReadyToJoin());
            cv.put("activity_datetime", model.getMobileTimestamp());
            cv.put("message_id", model.getMessageId());
            cv.put("device_id", model.getDeviceId());
            cv.put("mcode", model.getMcode());
            cv.put("server_response", model.getServerResponse());
            cv.put("activtity_date", model.getActivityDate());

            Long count = AppDatabase.getDatabaseInstance().insert(Constants.MOBILISER_DAILY_ACTIVITY_TABLE, null, cv);
            System.out.println("Insertion Count " + count);
            if (count == -1) {
                response.setResposne(App.getInstance().getString(R.string.db_error));
                response.setResposneCode(0);
            }
        }
        return response;
    }

    void cleanExistingDB() {
        AppDatabase.getUserDatabaseInstance().delete(Constants.USER_TABLE, null, null);
        AppDatabase.getDatabaseInstance().delete(Constants.PROGRAM_LIST_TABLE, null, null);
        AppDatabase.getDatabaseInstance().delete(Constants.STUDENT_DETAILS_TABLE, null, null);
        AppDatabase.getDatabaseInstance().delete(Constants.MOBILISER_DAILY_ACTIVITY_TABLE, null, null);
    }
}
