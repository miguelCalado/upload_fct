package org.example.snovaisg.myuploadFCT;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileInputStream;

public class JsonDic {

    private JSONObject myJson;

    public JsonDic(String filename, Context context) {
        String JsonData ="";
        try {
            FileInputStream fis = context.openFileInput(filename);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            JsonData = new String(buffer);
            Log.d("Hallo",JsonData);
            myJson = new JSONObject(JsonData);
            int a = 1 + 2;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("MEH","Something went wrong");
        }
    }

    public String sgetString(String Rest,String elem) throws JSONException {
        JSONObject REST = myJson.getJSONObject(Rest);
        String surname = REST.getString(elem);
        return surname;
    }

    public String []  getStringArray(String elem) throws JSONException {
        JSONArray jArr = myJson.getJSONArray(elem);
        String[] list = new String[jArr.length()];
        for (int i = 0; i < jArr.length(); i++) {
            list[i] = jArr.getString(i);
        }
        return list;
    }
    public String getWeekId() throws JSONException {
        return myJson.getString("WeekId");
    }

    public String []  getStringArray2(String Rest,String elem) throws JSONException {
        JSONObject REST = myJson.getJSONObject(Rest);
        JSONArray jArr = REST.getJSONArray(elem);
        String[] list = new String[jArr.length()];
        for (int i = 0; i < jArr.length(); i++) {
            list[i] = jArr.getString(i);
        }
        return list;
    }
    public JSONObject getMyJson(){
        return myJson;    }
    public String [] getDicArray(String Rest, String Dic,String elem) throws JSONException {
        JSONObject REST = myJson.getJSONObject(Rest);
        JSONObject caft = REST.getJSONObject(Dic);
        JSONArray jArr = caft.getJSONArray(elem);
        String[] list = new String[jArr.length()];
        for (int i = 0; i < jArr.length(); i++) {
            list[i] = jArr.getString(i);
        }
        return list;
    }

    public String parseJsonToString(){
        return myJson.toString();
    }

}