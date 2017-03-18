package com.company;
import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner("");
        try{
            scanner = new Scanner(new File("/Users/YeJiang/IdeaProjects/p2_446/src/com/company/part-B.txt"));
        }catch(Exception e){e.printStackTrace();}

        // 1. tokenize
        Tokenizer t = new Tokenizer(scanner);
        List<String> tokens = t.tokenize();


        //2.remove stop words
        Removal removal = new Removal(tokens);
        List<String> afterRemove = removal.remove();

        //3. get stem of all the words
        Stemmer stemmer = new Stemmer(afterRemove);
        List<String> stems= stemmer.stem();

        //4. collect frequencies
        FrequencyFider finder = new FrequencyFider(stems);
        List<Map.Entry<String,Integer>> frequencies = finder.getFrequencies();

       for(int i=0;i<frequencies.size();i++){
           if(i>=200)break;
           System.out.println(i+1 + ". "+ frequencies.get(i).getKey()+ " "+frequencies.get(i).getValue());
       }
    }
}
