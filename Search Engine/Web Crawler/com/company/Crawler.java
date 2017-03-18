package com.company;
import com.company.supporter.Robot;
import com.company.supporter.Save;
import com.company.supporter.WebThread;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by YeJiang on 2/2/17.
 */
public class Crawler {
    String start;
    int maxNum;
    boolean restricted;
    int counter;
    public boolean stop;
    Robot robot;

    Set<String> urls;
    ConcurrentLinkedQueue<String> queue;


    public Crawler(String start, int max, boolean singleDomain) {
        this.start = start;
        maxNum = max;
        restricted = singleDomain;
        queue = new ConcurrentLinkedQueue();
        urls = Collections.newSetFromMap(new ConcurrentHashMap<>());
        stop = false;
        robot = new Robot();
    }

    public Set start() {

        // we are in -----PART A -----where we restricted in U-mass domain only
        if (restricted) {
            queue.add(start);
            urls.add(start);
            while (urls.size() < maxNum && !queue.isEmpty()) {
                try {
                    String cur = queue.poll();
                    if (cur.contains("@") || cur.contains("jpg") || cur.contains("80") || cur.contains("adfad"))
                        continue;
                    URL url = new URL(cur);

                    // downLoad current page to Contents.txt file
                     Save.downLoadURLtoFile(url,"Contents.txt");

                    if (cur.contains("pdf")) continue; // we only download but not parse pdf files

                    // extract all the links from this webpage
                    List<String> links = Save.getLinks(cur);

                    // for each link if it is in the UMass domain and haven't seen before, we add it into urls and queue
                    for (String link : links) {
                        if ((Save.getDomainName(link).startsWith("cs.umass.edu") || Save.getDomainName(link).startsWith("cics.umass.edu"))&&(!link.contains("@"))){

                            if (link.endsWith("/")) link = link.substring(0, link.length() - 1);

                            // if robot.txt allowed  &&  we never add this link before --> we add into queue
                            if (robot.allowed(link) && urls.add(link)) queue.add(link);
                        }
                    }
                    TimeUnit.SECONDS.sleep(5); // being polite
                    System.out.println("urls size = "+urls.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.stop = true;
            return urls;
        }

        // else in ------ part B -------we are not restricted in a single domain
        else {
            queue = new ConcurrentLinkedQueue();
            urls = Collections.newSetFromMap(new ConcurrentHashMap<>());
            this.maxNum = 1000;
            queue.add(start);
            urls.add(start);
            java.util.concurrent.CopyOnWriteArrayList<String> domain = new CopyOnWriteArrayList<>();
            domain.add("cics.umass.edu");
            int threadNum = 0;
            while (urls.size() < maxNum && !queue.isEmpty()) {
                WebThread thread = new WebThread(queue, urls, maxNum, queue.poll(), domain, domain.get(domain.size() - 1), this, robot,0);
                thread.run();
            }
            this.stop = true;
            System.out.println("we created total " + WebThread.threadNum + " number of threads");
            return urls;
        }
    }

    // when a thread found a new domain that not in our domains set, it will call this function to create a new thread to deal with it
    public void createNewThread(ConcurrentLinkedQueue queue, Set urls, int num, String startPoint, java.util.concurrent.CopyOnWriteArrayList<String> domains, String domain, Crawler master, Robot robot,int loadNum) {

        if (startPoint.contains("@") || startPoint.contains("pdf") || startPoint.contains("zip")|| startPoint.contains("jpg")||startPoint.contains("png")||startPoint.contains("rtf")) return;
        WebThread thread = new WebThread(queue, urls, num, startPoint, domains, domain, this, robot,loadNum);
        thread.run();
    }
}

