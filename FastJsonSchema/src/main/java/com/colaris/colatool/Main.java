package com.colaris.colatool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String str = "{\"table\":\"WF_PROCESS_INSTANCE\",\"operationType\":0,\"operationTime\":\"2023-02-20 09:57:31\",\"CSN\":\"5987274162\",\"source\":\"10.162.252.81-3005\",\"source_type\":2,\"trailRba\":0,\"trailSeq\":5429,\"key_column\":[{\"name\":\"PROCESS_INSTANCE_ID\",\"newValue\":\"2023022009573167641322899500010164247315588754299833038630461440\",\"oldValue\":\"\"},{\"name\":\"ACTIVITY_ID\",\"newValue\":\"start\",\"oldValue\":\"\"}],\"columns\":[{\"name\":\"PROCESS_ID\",\"newValue\":\"2022112909553822032311879100001012423863618100003438733639386110\",\"oldValue\":\"\"},{\"name\":\"PROCESS_INSTANCE_ID\",\"newValue\":\"2023022009573167641322899500010164247315588754299833038630461440\",\"oldValue\":\"\"},{\"name\":\"ACTIVITY_ID\",\"newValue\":\"start\",\"oldValue\":\"\"},{\"name\":\"ACTIVITY_NAME\",\"newValue\":\"开始\",\"oldValue\":\"\"},{\"name\":\"STEP_ID\",\"newValue\":\"1\",\"oldValue\":\"\"},{\"name\":\"STATE\",\"newValue\":\"9\",\"oldValue\":\"\"},{\"name\":\"UPDATE_TIME\",\"newValue\":\"2023-02-20 09:57:31\",\"oldValue\":\"\"}]}";
        String tableFlag = "";
        JSONObject jsonObj = JSON.parseObject(str);
        for (String key : jsonObj.keySet()) {
            int score = LevenshteinDistance.getDefaultInstance().apply(key, "tableName");
            if(score<=4){
                tableFlag = key;
                break;
            }
        }
        List<String> keyColumn = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        if(StringUtils.isNotBlank(tableFlag)){
            for (String key : jsonObj.keySet()) {
                int keyScore = LevenshteinDistance.getDefaultInstance().apply(key, "key_column");

                if(keyScore<=2&&(jsonObj.get(key) instanceof List<?>)){
                    Object objects = jsonObj.get(key);
                    for (Object object : (List<?>)objects) {
                        keyColumn.add(jsonObj.getString(tableFlag)+"."+JSON.parseObject(object.toString()).getString("name"));
                    }
                }
                int columnsScore = LevenshteinDistance.getDefaultInstance().apply(key, "columns");
                if(columnsScore<=2&&(jsonObj.get(key) instanceof List<?>)){
                    Object objects = jsonObj.get(key);
                    for (Object object : (List<?>)objects) {
                        columns.add(jsonObj.getString(tableFlag)+"."+JSON.parseObject(object.toString()).getString("name"));
                    }
                }
            }
        }

        System.out.println(keyColumn);
        System.out.println(columns);
        System.out.println("=========方法耗时"+(System.currentTimeMillis()-startTime)+"ms==========");








//        for (String s : jsonObject.keySet()) {
//            String name = "";
//            if (jsonObject.get(s)!=null){
//                name = jsonObject.get(s).getClass().getName();
//            }else {
//                name = null;
//            }
//            System.out.println(s+":"+name);
//        }
    }
}