package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeJiang on 2/18/17.
 */
public class Stemmer {

    List<String> input;

    String vowel = "aeiou";

    public Stemmer(List<String> input)
    {
        this.input = input;
    }

    public List<String> stem()
    {
        List<String> res = new ArrayList<>();

        for(String s: input){
            res.add(getStem2(getStem1(s)));
        }
        return res;
    }

    private int firstVowelIndex(String s )
    {
        for(int i=0;i<s.length();i++)
        {
            char cur = s.charAt(i);
            if(vowel.contains(String.valueOf(cur)))return i;
        }
        return -1;
    }

    private int firstNonVowelIndex(String s)
    {
        for(int i=0;i<s.length();i++)
        {
            char cur = s.charAt(i);
            if(!vowel.contains(String.valueOf(cur)))return i;
        }
        return -1;
    }

    private String getStem1(String s)
    {
        // ends with ss  ||  us  --> do nothing
        if(s.endsWith("ss") || s.endsWith("us"))return s;

        // sses --> ss
        if(s.endsWith("sses"))s = s.substring(0,s.length()-2);

        // if end with s && there is a vowel and not before s --> we delete "s"
        if(s.endsWith("s") && firstVowelIndex(s) != -1 && firstVowelIndex(s) != s.length()-2)s= s.substring(0,s.length()-1);

        // if end with ied/ies && length >4  replace it with i  Else(lengh<=4) replace it with ie
        if(s.endsWith("ied") || s.endsWith("ies")){
            if(s.length()>4)s = s.substring(0,s.length()-2);
            else s = s.substring(0,s.length()-1);
        }
         return s;
    }

    private boolean doubleEndNoLSZ(String s)
    {
        if(s.length() <= 2)return false;

        String lsz = "lsz";

        if(s.charAt(s.length()-1)==s.charAt(s.length()-2) && !lsz.contains(String.valueOf(s.charAt(s.length()-1))))return true;

        return false;

    }

    private boolean isShort(String s)
    {
        return(s.endsWith("op")||
                s.endsWith("it")||
                s.endsWith("om")||
                s.endsWith("av")||
                s.endsWith("ak")||
                s.endsWith("us")||
                s.endsWith("ov")||
                s.endsWith("ng")||
                s.endsWith("ag")||
                s.endsWith("rc"))||
                s.endsWith("nc")||
                s.endsWith("li");
    }

    private String getStem2(String s)
    {

         if(s.endsWith("eed") || s.endsWith("eedly")){
             String temp = s.endsWith("eed")? s.substring(0,s.length()-3):s.substring(0,s.length()-5);
             int firstNonVowelIndex = firstNonVowelIndex(temp);
             if(firstNonVowelIndex != -1){
                 temp = temp.substring(firstNonVowelIndex+1,temp.length());
                 if(firstVowelIndex(temp) == -1) s = (s.endsWith("eed"))? s.substring(0,s.length()-1):s.substring(0,s.length()-3);
             }
         }

         if(s.endsWith("ed") || s.endsWith("edly") || s.endsWith("ing") || s.endsWith("ingly")){

             String temp = "";

             if(s.endsWith("d")){
                 temp = s.substring(0,s.length()-2);
                 if(firstVowelIndex(temp) != -1) {
                     s = temp;
                     if(s.endsWith("at")||s.endsWith("bl")||s.endsWith("iz")) {
                         s = s+"e";
                     }
                     else if(doubleEndNoLSZ(s)){
                         s = s.substring(0,s.length()-1);
                     }

                     // common shot word when we remove ed/ing need to add e
                     else if(isShort(s)){
                         s = s + "e";
                     }
                 }
             }
             else if (s.endsWith("g")){
                 temp = s.substring(0,s.length()-3);
                 if(firstVowelIndex(temp) != -1) {
                     s = temp;
                     if(s.endsWith("at")||s.endsWith("bl")||s.endsWith("iz")){
                         s = s+"e";
                     }
                     else if(doubleEndNoLSZ(s)){
                         s = s.substring(0,s.length()-1);
                     }

                     // common shot word when we remove ed/ing need to add e
                     else if(isShort(s)){
                         s = s + "e";
                     }
                 }
             }
             else if(s.endsWith("edly")){
                 temp = s.substring(0,s.length()-4);
                 if(firstVowelIndex(temp) != -1){
                     s = temp;
                     if(s.endsWith("at")||s.endsWith("bl")||s.endsWith("iz")) {
                         s = s+"e";
                     }
                     else if(doubleEndNoLSZ(s)){
                         s = s.substring(0,s.length()-1);
                     }

                     // common shot word when we remove ed/ing need to add e
                     else if(isShort(s)){
                         s = s + "e";
                     }
                 }
             }
             else {
                 temp = s.substring(0,s.length()-5);
                 if(firstVowelIndex(temp) != -1){
                     s = temp;
                     if(s.endsWith("at")||s.endsWith("bl")||s.endsWith("iz")) {
                         s = s+"e";
                     }
                     else if(doubleEndNoLSZ(s)){
                         s = s.substring(0,s.length()-1);
                     }

                     // common shot word when we remove ed/ing need to add e
                     else if(isShort(s)){
                         s = s + "e";
                     }
                 }
             }
         }
        return s;
    }
}
