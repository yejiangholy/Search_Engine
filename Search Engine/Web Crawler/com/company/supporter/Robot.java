package com.company.supporter;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Created by YeJiang on 2/3/17.
 *
 * a class for taking care of robot.txt files in the website we are going to fetch
 */
public class Robot {

private Map<String,ArrayList<String>> cache;

    public Robot(){
        cache = new HashMap<>();
    }

    public boolean  allowed(String u)
    {
        try{
            URL url = new URL(u);
            String host = url.getHost().toLowerCase();
            ArrayList disallowList = cache.get(host);
            if(disallowList == null){
                // if we haven't read robot.txt from this host

                disallowList = new ArrayList();

                try{
                    URL robotFile =  new URL("http://" + host + "/robots.txt");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(robotFile.openStream()));

                    String line;
                    while((line = reader.readLine())!=null){
                        if(line.indexOf("Disallow:")==0){
                            // find where disallow start
                            String disallowPath = line.substring("Disallow:".length());

                            // remove all the comment !
                            int commentIndex = disallowPath.indexOf("#");
                            if(commentIndex != -1)disallowPath = disallowPath.substring(0,commentIndex);

                            disallowPath = disallowPath.trim();
                            disallowList.add(disallowPath);

                            cache.put(host,disallowList);
                        }
                    }
                }catch (Exception e){
                    // if this host do not provide any robot.txt file, Exception will be throw
                    // therefore we are no restricted anymore
                    return true;
                }
            }
            // after previous if statement, we will have a disallowed list for current url
            //  check if this file is in the disallowed list
                String file = url.getFile();
                for(int i=0;i<disallowList.size();i++){
                    String disallow = (String)disallowList.get(i);
                    // as long as start with disallow, then we can not go deeper any more
                    if(file.startsWith(disallow))return false;
                }
                return true;
        }
        catch (Exception e){
            return false;

        }
    }
}
