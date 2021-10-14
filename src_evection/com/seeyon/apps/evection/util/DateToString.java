package com.seeyon.apps.evection.util;

public class DateToString {
    public static String toDateString(Object date){
        String trimDate = String.valueOf(date).trim();
        StringBuffer sb = new StringBuffer();
        String[] s = trimDate.split(" ");
        sb.append(s[0]);
        sb.append("T");
        if(s.length>1){
            sb.append(s[1]);
        }else {
            sb.append("00:00:00");
        }
        sb.append("+08:00");
        return sb.toString();
    }
}
