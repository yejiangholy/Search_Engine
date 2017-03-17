package com.company;
import java.util.*;

/**
 * Created by YeJiang on 2/17/17.
 */
public class Tokenizer {

    private Scanner scanner;
    private  List<String> tokens;

    private String seperator = ",:;&^#$^@?()*!/|[]{}' ()><+-`~. _ - \\ \"";


    // Question: 1. He's  --> hes  or he s   2. U.S.A --> USA or usa
    public Tokenizer(Scanner s){
        scanner = s;
        tokens = new ArrayList<>();
    }

    private boolean isSeperator(char c)
    {
        return seperator.contains(String.valueOf(c));
    }

    public List<String> tokenize(){
        while(scanner.hasNextLine()){
            String cur = scanner.nextLine();
            Tokenize(cur);
        }
        return tokens;
    }

    public  void Tokenize(String str){
        int start = 0;
        int end = 0;
        int len = str.length();

            while(end < len-1){

            while(end<len-1 && !isSeperator(str.charAt(end)) && str.charAt(end) != ' ')end++;

                String cur = "";

                if(end == len-1 && !isSeperator(str.charAt(len-1)))cur = str.substring(start,end+1);
                else cur = str.substring(start,end);

            tokens.add(cur.trim().toLowerCase());
            while(end<len-1 && (isSeperator(str.charAt(end)) || str.charAt(end) == ' '))end++;
            start = end;
        }
    }
}
