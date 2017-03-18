package com.company.supporter;
import  java.net.*;
import com.company.Crawler;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by YeJiang on 2/3/17.
 */
public class WebThread extends Thread {

    Set<String> urls;
    ConcurrentLinkedQueue<String> mainQueue;
    Queue<String> localQueue;
    java.util.concurrent.CopyOnWriteArrayList<String> domains;
    String domain;
    String start;
    int maxNum;
    Crawler master;
    Robot robot;

    public static int loadPageNum;
    public static int threadNum = 0;
    public WebThread(ConcurrentLinkedQueue q, Set urls, int num, String start, java.util.concurrent.CopyOnWriteArrayList<String> domains,String d , Crawler master,Robot r, int loadPageNum){
        this.mainQueue = q;
        localQueue = new LinkedList<>();
        this.urls = urls;
        this.domain = d;
        this.domains = domains;
        this.start = start;
        this.master = master;
        maxNum = num;
        robot = r;
        this.loadPageNum = loadPageNum;
        localQueue.add(start);
    }
    public void run(){
        while (!localQueue.isEmpty() && urls.size() < maxNum && !master.stop) {
            try {
                String cur = localQueue.poll();
                // skip some invalid web-page
                if (cur.contains("@") || cur.contains("jpg") || cur.contains("80") || cur.contains("adfad")) continue;
                URL url = new URL(cur);

                // download the page content
                Save.downLoadURLtoFile(url, "Contents.txt");
                loadPageNum++;
                if (cur.contains("pdf")) continue; // we only download but not parse pdf files
                List<String> links = Save.getLinks(cur);
                // for each link add into urls if not exist

                for (String link : links) {
                    //if have't exist
                    if (master.stop) return; // if master.stop == true, means we already completed the task

                    if (link.endsWith("/")) link = link.substring(0, link.length() - 1);

                    if (urls.add(link) && robot.allowed(link)) {

                        mainQueue.add(link);
                        if (link.contains(domain)) localQueue.add(link);

                        String curDomain = Save.getDomainName(link);
                        if (!domains.contains(curDomain)) {
                            // if there is newly added domain, we create a new thread and fetch it --> as we keep one domain one thread
                            domains.add(curDomain);
                            try {
                                URL newStart = new URL(link);
                                master.createNewThread(mainQueue, urls, maxNum, newStart.toString(), domains, curDomain, master, robot, this.loadPageNum);
                            } catch (Exception e) {
                                // do nothing, if it can not form a valid URL
                                System.err.println("new domain found but can not form valid url exception");
                            }
                        }
                    }
                    if (urls.size() > maxNum) master.stop = true;
                }
                System.out.println("(" + this.loadPageNum + "," + urls.size() + ")"); // -> get the data we need for drawing graph in report
                this.sleep(5000);    // sleep 5 s being polite
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;  // stop itself
    }
}
