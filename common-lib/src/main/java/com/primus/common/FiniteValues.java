package com.primus.common;

import java.util.*;

public class FiniteValues {

    public static  Map<String, Map<String,String>> FVMap = new LinkedHashMap<>();

    static {
        Map FreqMap= new LinkedHashMap();
        FreqMap.put("FREQMON","Monthly");
        FreqMap.put("FREQWEEK","Weekly");
        FreqMap.put("FREQDAILY","Daily");
        FreqMap.put("FREQQUART","Quarterly");
        FVMap.put("CHITFREQ",FreqMap);

    }

    public  static List<Map<String,String>> getFiniteValues(String groupCode )
    {
        List<Map<String,String>> ret = new ArrayList<>();
        Map<String,String> keyVal = FVMap.get(groupCode);
        keyVal.entrySet().forEach( entry -> {
            Map<String,String> tempMap = new HashMap<>();
            tempMap.put("Code",entry.getValue());
            tempMap.put("Value",entry.getValue());
            ret.add(tempMap);
        });

        return ret;
    }
}
