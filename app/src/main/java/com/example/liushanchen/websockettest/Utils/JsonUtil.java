package com.example.liushanchen.websockettest.Utils;

import com.example.liushanchen.websockettest.domain.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liushanchen on 15/6/5.
 */
public  class JsonUtil {
    public static NotificationModel j2sMode1(String message) throws JSONException {
        NotificationModel nm=new NotificationModel();
        JSONObject dataJson = new JSONObject(message);//"你的Json数据"
        String a = dataJson.getString("a");
        if (a.equals("prescription.arriver")) {
            JSONObject d = dataJson.getJSONObject("d");
            String id = d.getString("id");
            String name = d.getString("name");
            JSONObject doctor = d.getJSONObject("doctor");
            String doctorName = doctor.getString("name");
            String doctorScore = doctor.getString("score");
//            outMsg(a, name + "|" + id, doctorName + "|" + doctorScore, count);
            nm.setMabstract(a);
            nm.setTittle(name + "|" + id);
            nm.setTittle(doctorName + "|" + doctorScore);
           return nm;
        } else if (a.equals("notice")) {
            JSONObject d = dataJson.getJSONObject("d");
            String title = d.getString("title");
            String version = d.getString("version");
            String url = d.getString("url");
//            outMsg(a, title, version + "|" + url);
            nm.setMabstract(a);
            nm.setTittle(title);
            nm.setTittle(version + "|" + url);
            return nm;
        }
        return null;
    }

    private static void j2s(String masage) throws JSONException {
        JSONObject dataJson = new JSONObject(masage);//"你的Json数据"
        JSONObject response = dataJson.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        JSONObject info = data.getJSONObject(0);
        String province = info.getString("province");
        String city = info.getString("city");
        String district = info.getString("district");
        String address = info.getString("address");
        System.out.println(province + city + district + address);
    }
}
