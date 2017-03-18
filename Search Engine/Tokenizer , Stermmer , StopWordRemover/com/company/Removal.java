package com.company;
import java.io.File;
import java.util.*;

/**
 * Created by YeJiang on 2/17/17.
 */
public class Removal {

   Set<String> removeList;
    List<String> input;

    public  Removal(List<String> words){

        Scanner s = new Scanner("");
        try {
             s = new Scanner(new File("/Users/YeJiang/IdeaProjects/p2_446/src/com/company/words.txt"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNextLine()){
            list.add(s.nextLine());
        }
        s.close();

        removeList = new HashSet<>(list);
        input = words;
    }

    public List<String> remove(){

        List<String> res = new ArrayList<>();

        for(String s : input)if(!removeList.contains(s))res.add(s);

        return res;
    }

}
