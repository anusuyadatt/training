package com.example.tli_6.prathamapp.logic;

import android.database.Cursor;

import com.example.tli_6.prathamapp.Constants;
import com.example.tli_6.prathamapp.application.App;
import com.example.tli_6.prathamapp.database.AppDatabase;
import com.example.tli_6.prathamapp.model.DailyActivityModel;
import com.example.tli_6.prathamapp.model.HttpResponseModel;
import com.example.tli_6.prathamapp.listener.HttpCommunicationListener;
import com.example.tli_6.prathamapp.model.StudentModel;
import com.example.tli_6.prathamapp.tasks.ServerInteractionTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by tli-6 on 5/17/17.
 */
public class HttpCommunicationLayer {
    ServerInteractionTask serverInteractionTask;
    HttpCommunicationListener httpCommunicationListener;
    private final String Base_uRL = "http://pratham.fieldata.in/pace/api";
            //;
    ///"http://tli1.pratham-sms/pace/api";

    public void connectToServer(int status, String mcode,String no) {

    String requeset_data = null;
    switch (status) {
        case Constants.REGISTER:
            requeset_data = createRegisterRequest(mcode, no);
            break;
    }
    if (requeset_data != null)
        createHttpConnection(requeset_data, status);

}

    public void connectToServer(int status, Object model) {

            String requeset_data = null;
            switch (status) {
                case Constants.DAILY_ACTIVITY_SUBMIT:
                    DailyActivityModel mainModel = (DailyActivityModel) model;
                    requeset_data = createDailyActivityRequest(mainModel);
                    break;
                case Constants.STUDENT_DETAILS_SUBMIT:
                    StudentModel mainModel1 = (StudentModel) model;
                    requeset_data = createStudentDetailRequest(mainModel1);
                    break;
            }
            if (requeset_data != null)
                createHttpConnection(requeset_data, status);

    }

    String createRegisterRequest(String code,String number) {
        String data;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mcode", code);
            jsonObject.put("mobile_number",number);
            data = jsonObject.toString();

        } catch (JSONException jsonex) {
            return null;
        }
        System.out.println("Post Requset " + data);
        return data;
    }

    String createDailyActivityRequest(DailyActivityModel model) {
        String data;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id",model.getDeviceId());
            jsonObject.put("mcode",model.getMcode());
            jsonObject.put("message_id",model.getMessageId());
            jsonObject.put("sender",model.getSender());
            jsonObject.put("mobile_timestamp",model.getMobileTimestamp());
            jsonObject.put("village",model.getVillage());
            jsonObject.put("students_reached",model.getStudentsReached());
            jsonObject.put("students_assessed",model.getStudentsAssessed());
            jsonObject.put("students_ready_to_join",model.getStudentsReadyToJoin());
            data = jsonObject.toString();

        } catch (JSONException jsonex) {
            return null;
        }
        System.out.println("Post Requset " + data);

        return data;
    }

    String createStudentDetailRequest(StudentModel model) {
        String data;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id",model.getDeviceId());
            jsonObject.put("mcode",model.getMcode());
            jsonObject.put("message_id",model.getMessageId());
            jsonObject.put("sender",model.getSender());
            jsonObject.put("mobile_timestamp",model.getMobileTmestamp());
            jsonObject.put("village",model.getVillage());
            jsonObject.put("student_name",model.getStudentName());
            jsonObject.put("program_code",model.getProgramCode());
            jsonObject.put("rojgar_mitra",model.getRojarMitra());
            data = jsonObject.toString();

        } catch (JSONException jsonex) {
            return null;
        }
        System.out.println("Post Requset " + data);

        return data;
    }



    void createHttpConnection(String rqdata, int url_code) {
        serverInteractionTask = new ServerInteractionTask();
        String url = null;
        switch (url_code) {
            case Constants.REGISTER:
                url = Base_uRL + "/mobilizer/registraition";
                break;
            case Constants.DAILY_ACTIVITY_SUBMIT:
                url = Base_uRL + "/mobilizer/dailyprogress";
                break;
            case Constants.STUDENT_DETAILS_SUBMIT:
                url = Base_uRL + "/mobilizer/studentreg";
                break;
        }
        if (url != null) {
            serverInteractionTask.setUrl(url);
            serverInteractionTask.setTaskInstance(serverInteractionTask);
            serverInteractionTask.setCommunicationLayerListener(this);
            serverInteractionTask.execute(rqdata);
        }
    }

    public void returnResponse(HttpResponseModel responseModel) {
        System.out.println("response " + responseModel.getResposne());
        System.out.println("response code " + responseModel.getResposneCode());
        if (httpCommunicationListener != null)
            httpCommunicationListener.onReceivingResponse(responseModel.getResposne(), responseModel.getResposneCode());
    }

    public void setHttpCommunicationListener(HttpCommunicationListener listener) {
        this.httpCommunicationListener = listener;
    }

    public void cancelTask() {
        if (!serverInteractionTask.isCancelled())
            serverInteractionTask.cancel(true);
    }
}
