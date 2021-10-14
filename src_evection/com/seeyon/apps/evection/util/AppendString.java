package com.seeyon.apps.evection.util;

public class AppendString {
    public static String evectionJson (String personId,String certNum,String startTime,String endTime){
        StringBuffer sb = new StringBuffer();
        sb.append("{\"addAttendanceList\":");
        sb.append("[{\"attendanceId\":\"\",");
        sb.append("\"personId\": ");
        sb.append("\"");
        sb.append(personId);
        sb.append("\",");
        sb.append("\"jobNo\": \"\",");
        sb.append("\"certNum\": ");
        sb.append("\"");
        sb.append(certNum);
        sb.append("\",");
        sb.append("\"outWorkType\": \"at002\",");
        sb.append("\"startTime\": ");
        sb.append("\"");
        sb.append(startTime);
        sb.append("\",");
        sb.append("\"endTime\": ");
        sb.append("\"");
        sb.append(endTime);
        sb.append("\",");
        sb.append("\"reason\": \"外地出差\"}]}");
        return sb.toString();
    }
}
