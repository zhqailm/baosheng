package com.seeyon.apps.evection.util;

import org.json.JSONObject;

import java.util.HashMap;

public class Connections {
    private static String getTokenUrl = "https://" + Constants.HOST+Constants.TOKEN_URL;
    private static String evectionUrl = "https://" + Constants.HOST+Constants.EVECTION_URL;
    private static String getPersonInformationUrl = "https://" + Constants.HOST+Constants.PERSON_INFORMATION_URL;
    private static String getToken (){
        HashMap<String,Object> param = new HashMap<>();
        param.put("client_id",Constants.CLIENT_ID);
        param.put("client_secret",Constants.CLIENT_SECRET);
        String result = null;
        try {
            result = HttpKit.post(getTokenUrl,param);
            JSONObject tokenJson = new JSONObject(result);
            String token = tokenJson.getString("access_token");
//            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAttendanceResult (String evectionString){
        try {
            String token = getToken();
            JSONObject evectionJson = new JSONObject(evectionString);
            String evectionResult = HttpKit.send(evectionUrl,evectionJson,"utf-8",token);
//            System.out.println(evectionResult);
            return evectionResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPersonId (String certNum){
        String token = getToken();
        String json = "{\"certificateNo\": \""+certNum+"\", \"certificateType\": 111}";
        try {
            JSONObject temp = new JSONObject(json);
            String infoJson = HttpKit.send(getPersonInformationUrl,temp,"utf-8",token);
            JSONObject js = new JSONObject(infoJson);
            if (js.get("data").equals(null)) {
            	return null;
            }
            JSONObject dataJs = (JSONObject) js.get("data");
            String personId = dataJs.getString("personId");
//            System.out.println(personId);
            return personId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
