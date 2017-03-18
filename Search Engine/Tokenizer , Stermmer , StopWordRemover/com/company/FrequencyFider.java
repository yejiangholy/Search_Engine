package com.company;

import java.util.*;

/**
 * Created by YeJiang on 2/18/17.
 */

//Users/YeJiang/IdeaProjects/p2_446/src/com/company/part-B.txt

public class FrequencyFider {

    Map<String,Integer> map;
    List<String> input;

    public FrequencyFider(List<String> input){
        this.input = input;
        map = new HashMap<>();
    }

    public List<Map.Entry<String,Integer>>  getFrequencies(){

        int words = 0;
        int voca = 0;

        for(String s: input){
            ++words;
            if(map.containsKey(s))map.put(s,map.get(s)+1);
            else {
                map.put(s, 1);
                ++voca;
                //System.out.println("(" + words + "," + voca + ")");
            }
        }

        List<Map.Entry<String,Integer>> entries = new ArrayList<>();
        entries.addAll(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>(){
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String,Integer> e2){
                return (e2.getValue()-e1.getValue());
            }
        });

        return entries;
    }


}
