package com.example.a4.a7timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.*;

/**
 * Created by ASUS on 2018/5/7.
 */
public class ZRCookie{
    private SortedMap<String,List<String>> cookieTable = new TreeMap<String, List<String>>();

    public ZRCookie(){

    }

//    public ZRCookie(JSONObject jsonObject){
//        if(jsonObject != null){
//            Set<String> set = jsonObject.keySet();
//            for (String s : set) {
//                this.set(s,jsonObject.optString(s));
//            }
//        }
//    }


    public List<String> get(String host){
        if(host == null) return null;
        return cookieTable.get(host);
    }

    public List<String> get(URL url){
        if(url == null) return null;
        //System.out.println(url.getHost());
        return get(url.getHost());
    }

    public void set(String host,List<String> cookieList){
        if(host == null) return;
        cookieTable.put(host,cookieList);
    }

    public void set(URL url, List<String> cookieList){
        if(url == null) return;
        set(url.getHost(),cookieList);
    }

    @Override
    public String toString(){
        return this.toJSONObject().toString();
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        Set<String> set = cookieTable.keySet();
        for (String host: set) {
            try{
                List<String> cookieList = cookieTable.get(host);
                if(cookieList == null){
                    continue;
                }
                JSONArray jsonArray = new JSONArray();
                Iterator iterator = cookieList.iterator();
                while(iterator.hasNext()){
                    jsonArray.put(iterator.next());
                }
                jsonObject.put(host,jsonArray);
            }catch (JSONException je){
                je.printStackTrace();
            }
        }
        return jsonObject;
    }

}
